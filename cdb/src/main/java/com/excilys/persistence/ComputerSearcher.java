package com.excilys.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.excilys.main.NotImplementedException;
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

            Optional<LocalDate> introducedDateOpt = DateMapper.sqlDateToLocalDate(res.getDate("introduced"));
            LocalDate introduced = introducedDateOpt.orElse(null);

            Optional<LocalDate> discontinuedDateOpt = DateMapper.sqlDateToLocalDate(res.getDate("discontinued"));
            LocalDate discontinued = discontinuedDateOpt.orElse(null);

            String companyName = res.getString("company.name");
            Company company = companyName == null ? null : new Company(companyName, res.getLong("company.id"));

            return new Computer(computerName, company, introduced, discontinued, computerId);
        }

    }

    @Autowired
    private EntityManagerFactory emf;

    private EntityManager em;

    private static final Logger LOG = LoggerFactory.getLogger(ComputerSearcher.class);

    private static final String QUERY_COMPUTER_SEARCH_WITH_NAME = "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company " + "ON computer.company_id = company.id "
            + "WHERE computer.name LIKE :pattern";

    private static final String QUERY_COMPUTER_FROM_ID = "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company " + "ON computer.company_id = company.id "
            + "WHERE computer.id = :id ";

    private static final String QUERY_COMPUTER_LIST = " SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company " + "ON computer.company_id = company.id";

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
     * @return la chaîne à insérer dans la requête pour que le sortEntry soit pris en compte
     */
    private static String sortEntryToSqlOrderByClause(@NonNull final SortEntry se) {
        return sortCriterionToSqlColumn(se.getCriteria()) + " " + (se.isAscending() ? "ASC" : "DESC");
    }

    private final ComputerRowMapper rowMapper = new ComputerRowMapper();

    @Autowired
    private NamedParameterJdbcTemplate template;

    /**
     * Recherche un ordinateur dans la base de donnée, à partir d'un identifiant.
     *
     * @param searchedId L'id de l'ordinateur recherché
     *
     * @return une instance de Optional vide si aucun ordinateur de l'id donné n'est présent dans la
     *         BD ou qu'une exception SQLException s'est produite/ Une instance de Optional
     *         contenant le Computer trouvé sinon
     * @throws PersistanceException
     */
    @Override
    public Optional<Computer> fetchById(final long searchedId) throws PersistanceException {
        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(r);
        ct.where(cb.equal(r.get("id"), searchedId));
        TypedQuery<Computer> q = this.em.createQuery(ct);
        // Sale
        List<Computer> res = q.getResultList();
        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    /**
     * ct.orderBy(getCriteriaList(cb, r, entries)); Cherche tous les ordinateurs dans la base, et
     * retourne la liste correspondant à ces derniers.
     *
     * @return La liste des ordinateurs présents dans la base de données
     *
     * @author jguyot2
     * @throws PersistanceException
     */
    @Override
    public List<Computer> fetchList() throws PersistanceException {
        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(r);
        TypedQuery<Computer> q = this.em.createQuery(ct);
        return q.getResultList();
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

    public List<Computer> fetchList(@NonNull final Page page, @NonNull final List<SortEntry> entries)
            throws PersistanceException {
        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(r);
        ct.orderBy(getCriteriaList(cb, r, entries));
        TypedQuery<Computer> q = this.em.createQuery(ct).setFirstResult(page.getOffset())
                .setMaxResults(page.getPageLength());

        return q.getResultList();
    }

    public List<Computer> fetchList(@NonNull final Page p, @NonNull final String search) throws PersistanceException {
        return fetchList(p, search, Arrays.asList());
    }

    private <T> Order getCriteriaBuilderOrderByFromSortEntry(CriteriaBuilder cb, Root<T> r, SortEntry se) {
        SortCriterion criterion = se.getCriteria();
        String columnName;
        switch (criterion) {
            case COMPUTER_NAME:
                columnName = "name";
                break;

            // TODO
            case COMPANY_ID:
                columnName = "manufacturer.id";
                break;
            case COMPANY_NAME:
                columnName = "manufacturer.name";
                break;
            case DISCONTINUED:
                columnName = "discontinued";
                break;
            case INTRODUCED:
                columnName = "introduced";
                break;
            default:
                throw new NotImplementedException();
        }

        return se.isAscending() ? cb.asc(r.get(columnName)) : cb.desc(r.get(columnName));
    }

    private <T> Order[] getCriteriaList(CriteriaBuilder cb, Root<T> r, List<SortEntry> se) {
        Order[] order = new Order[se.size() + 1];
        for (int i = 0; i < se.size(); ++i) {
            order[i] = getCriteriaBuilderOrderByFromSortEntry(cb, r, se.get(i));
        }
        order[order.length - 1] = cb.asc(r.get("id"));
        return order;
    }

    public List<Computer> fetchList(@NonNull final Page page, @NonNull final String search,
            @NonNull final List<SortEntry> entries) throws PersistanceException {

        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(r);
        ct.where(cb.like(r.get("name"), "%" + search.replace("%", "\\%") + "%"));
        ct.orderBy(getCriteriaList(cb, r, entries));

        TypedQuery<Computer> q = this.em.createQuery(ct).setFirstResult(page.getOffset())
                .setMaxResults(page.getPageLength());

        return q.getResultList();
    }

    /**
     * @return Le nombre d'éléments computer enregistrés dans la base.
     * @throws PersistanceException
     */
    @Override
    public int getNumberOfElements() throws PersistanceException {
        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> ct = cb.createQuery(Long.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(cb.count(r));

        TypedQuery<Long> q = this.em.createQuery(ct);
        return q.getSingleResult().intValue();
    }

    public int getNumberOfFoundElements(@NonNull final String search) throws PersistanceException {
        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Long> ct = cb.createQuery(Long.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(cb.count(r));

        TypedQuery<Long> q = this.em.createQuery(ct);
        return q.getSingleResult().intValue();
    }

    public List<Computer> searchByName(@NonNull final String search) throws PersistanceException {
        this.em = this.emf.createEntityManager();
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
        Root<Computer> r = ct.from(Computer.class);
        ct.select(r);
        ct.where(cb.like(r.get("name"), "%" + search.replace("%", "\\%") + "%"));
        TypedQuery<Computer> q = this.em.createQuery(ct);

        return q.getResultList();
    }
}
