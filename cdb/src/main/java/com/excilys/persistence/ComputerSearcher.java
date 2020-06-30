package com.excilys.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.model.sort.SortCriterion;
import com.excilys.model.sort.SortEntry;

/**
 * Classe permettant d'effectuer des requêtes sur des Computers.
 *
 * @author jguyot2
 */
@Repository
public class ComputerSearcher implements Searcher<Computer> {
    private class ComputerRowMapper implements RowMapper<Computer> {
        @Override
        public Computer mapRow(final ResultSet res, final int rowNum) throws SQLException {
            long computerId = res.getLong("computer.id");
            String computerName = res.getString("computer.name");

            Optional<LocalDate> introducedDateOpt = DateMapper
                    .sqlDateToLocalDate(res.getDate("introduced"));
            LocalDate introduced = introducedDateOpt.orElse(null);

            Optional<LocalDate> discontinuedDateOpt = DateMapper
                    .sqlDateToLocalDate(res.getDate("discontinued"));
            LocalDate discontinued = discontinuedDateOpt.orElse(null);

            String companyName = res.getString("company.name");
            Company company = companyName == null ? null
                    : new Company(companyName, res.getLong("company.id"));

            return new Computer(computerName, company, introduced, discontinued, computerId);
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(ComputerSearcher.class);

    private static final String QUERY_COMPUTER_SEARCH_WITH_NAME = "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id " + "WHERE computer.name LIKE :pattern";

    private static final String QUERY_COMPUTER_FROM_ID = "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id " + "WHERE computer.id = :id ";

    private static final String QUERY_COMPUTER_LIST = " SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id";

    private static final String REQUEST_NB_OF_ROWS = "SELECT count(id) FROM computer";

    private static final String REQUEST_NB_OF_ROWS_SEARCH = "SELECT count(id) FROM computer WHERE computer.name LIKE :pattern";

    private static final String ORDER_BY_WITH_NAME = "SELECT computer.id, computer.name,"
            + " introduced, discontinued, company.id, company.name FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id WHERE computer.name LIKE :pattern ORDER BY %s "
            + "LIMIT :limit OFFSET :offset";

    private static final String ORDER_BY_REQUEST = "SELECT computer.id, computer.name,"
            + " introduced, discontinued, company.id, company.name FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id ORDER BY %s LIMIT :limit OFFSET :offset";

    /**
     * Fonction de conversion d'un critère vers les noms de colonne des requêtes
     *
     * @param sq
     * @return
     */
    private static String sortCriterionToSqlColumn(@NonNull final SortCriterion sq) {
        switch (sq) {
        case COMPANY_ID:
            return "company.id";
        case COMPANY_NAME:
            return "company.name";
        case COMPUTER_NAME:
            return "computer.name";
        case DISCONTINUED:
            return "computer.discontinued";
        case INTRODUCED:
            return "computer.introduced";
        default:
            throw new RuntimeException("wtf");
        }
    }

    /**
     * Fonction de conversion d'un sortEntry vers une «clause» order by.
     *
     * @param se la sortEntry à ajouter à la requête
     * @return la chaîne à insérer dans la requête pour que le sortEntry soit pris
     *         en compte
     */
    private static String sortEntryToSqlOrderByClause(@NonNull final SortEntry se) {
        return sortCriterionToSqlColumn(se.getCriteria()) + " "
                + (se.isAscending() ? "ASC" : "DESC");
    }

    private final ComputerRowMapper rowMapper = new ComputerRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate template;

    /**
     * Recherche un ordinateur dans la base de donnée, à partir d'un identifiant.
     *
     * @param searchedId L'id de l'ordinateur recherché
     *
     * @return une instance de Optional vide si aucun ordinateur de l'id donné n'est
     *         présent dans la BD ou qu'une exception SQLException s'est produite/
     *         Une instance de Optional contenant le Computer trouvé sinon
     * @throws PersistanceException
     */
    @Override
    public Optional<Computer> fetchById(final long searchedId) throws PersistanceException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", searchedId);
        try {
            return Optional.of(
                    this.template.queryForObject(QUERY_COMPUTER_FROM_ID, params, this.rowMapper));
        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    /**
     * Cherche tous les ordinateurs dans la base, et retourne la liste correspondant
     * à ces derniers.
     *
     * @return La liste des ordinateurs présents dans la base de données
     *
     * @author jguyot2
     * @throws PersistanceException
     */
    @Override
    public List<Computer> fetchList() throws PersistanceException {
        try {
            return this.template.query(QUERY_COMPUTER_LIST, Collections.emptyMap(), this.rowMapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    /**
     * Renvoie la liste des Computer "compris" dans la page en paramètre.
     *
     * @param page
     *
     * @return La liste correspondant aux ordinateurs compris dans la page.
     * @throws PersistanceException
     */
    @Override
    public List<Computer> fetchList(@NonNull final Page page) throws PersistanceException {
        return fetchList(page, Arrays.asList());
    }

    public List<Computer> fetchList(@NonNull final Page page,
            @NonNull final List<SortEntry> entries) throws PersistanceException {

        StringBuilder orderByClause = new StringBuilder();
        for (SortEntry sortEntry : entries) {
            orderByClause.append(sortEntryToSqlOrderByClause(sortEntry) + ", ");
        }
        orderByClause.append("computer.id ");
        String request = String.format(ORDER_BY_REQUEST, orderByClause.toString());
        Map<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("offset", page.getOffset());
        requestParameters.put("limit", page.getPageLength());
        try {
            return this.template.query(request, requestParameters, this.rowMapper);

        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    public List<Computer> fetchList(@NonNull final Page p, @NonNull final String search)
            throws PersistanceException {
        return fetchList(p, search, Arrays.asList());
    }

    public List<Computer> fetchList(@NonNull final Page p, @NonNull final String search,
            @NonNull final List<SortEntry> entries) throws PersistanceException {

        StringBuilder orderByClause = new StringBuilder(" ");
        for (SortEntry sortEntry : entries) {
            orderByClause.append(sortEntryToSqlOrderByClause(sortEntry) + ", ");
        }
        orderByClause.append("computer.id ");
        String request = String.format(ORDER_BY_WITH_NAME, orderByClause.toString());
        String searchedPattern = "%" + search.replace("%", "\\%") + "%";

        Map<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("pattern", searchedPattern);
        requestParameters.put("limit", p.getPageLength());
        requestParameters.put("offset", p.getOffset());
        try {
            return this.template.query(request, requestParameters, this.rowMapper);

        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    /**
     * @return Le nombre d'éléments computer enregistrés dans la base.
     * @throws PersistanceException
     */
    @Override
    public int getNumberOfElements() throws PersistanceException {
        try {
            return this.template.queryForObject(REQUEST_NB_OF_ROWS, Collections.emptyMap(),
                    (res, rowNum) -> res.getInt(1));
        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return 0;
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    public int getNumberOfFoundElements(@NonNull final String search) throws PersistanceException {
        String searchPattern = "%" + search.replace("%", "\\%") + "%";
        Map<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("pattern", searchPattern);
        try {
            return this.template.queryForObject(REQUEST_NB_OF_ROWS_SEARCH, requestParameters,
                    (rs, rowNum) -> rs.getInt(1));

        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return 0;
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    public List<Computer> searchByName(@NonNull final String search) throws PersistanceException {
        String searchedPattern = "%" + search.replace("%", "\\%") + "%";
        Map<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("pattern", searchedPattern);
        try {
            return this.template.query(QUERY_COMPUTER_SEARCH_WITH_NAME, requestParameters,
                    this.rowMapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }
}
