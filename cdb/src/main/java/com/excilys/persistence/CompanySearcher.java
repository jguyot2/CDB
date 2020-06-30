package com.excilys.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.excilys.model.Company;
import com.excilys.model.Page;

/**
 * Classe utilisée pour les requêtes associées à la table company.
 *
 * @author jguyot2
 */
@Repository
public class CompanySearcher implements Searcher<Company> {

    private static class CompanyRowMapper implements RowMapper<Company> {
        @Override
        public Company mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            String name = rs.getString("name");
            long id = rs.getLong("id");
            return new Company(name, id);
        }
    }

    private static final CompanyRowMapper rowMapper = new CompanyRowMapper();
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(CompanySearcher.class);
    /** */
    private static final String REQUEST_COMPANIES = "SELECT name, id FROM company";
    /**  */
    private static final String REQUEST_COMPANIES_OFFSET = "SELECT name, id FROM company ORDER BY id LIMIT :limit OFFSET :offset";
    /** */
    private static final String REQUEST_NB_OF_ROWS = "SELECT count(id) FROM company";

    private static final String REQUEST_BY_ID = "SELECT name, id FROM company WHERE id = :id";

    @Autowired
    NamedParameterJdbcTemplate template;

    /**
     * Recherche un fabricant à partir de son identifiant dans la base de données.
     *
     * @param searchedId l'identifiant de l'entreprise identifiée
     *
     * @return Optional.empty() si aucune entreprise n'a été trouvée, ou une
     *         instance de Optional contenant l'entreprise trouvée
     * @throws PersistanceException
     */
    @Override
    public Optional<Company> fetchById(final long searchedId) throws PersistanceException {

        Map<String, Object> m = new HashMap<>();
        m.put("id", searchedId);
        try {
            Company ret = this.template.queryForObject(REQUEST_BY_ID, m, rowMapper);
            return Optional.of(ret);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    /**
     * Rend la liste des entreprises présentes dans la base de données, sous la
     * forme d'instances de Company.
     *
     * @return La liste des entreprises présentes dans la base de données
     */
    @Override
    public List<Company> fetchList() throws PersistanceException {

        try {
            return this.template.query(REQUEST_COMPANIES, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    /**
     * Recherche une "page" de la liste des entreprises, en fonction du paramètre.
     *
     * @param page la page à afficher
     *
     * @return la liste des Company de la BD comprises dans la page en paramètre
     * @throws PersistanceException
     */
    @Override
    public List<Company> fetchList(@NonNull final Page page) throws PersistanceException {
        Map<String, Object> m = new HashMap<>();
        m.put("offset", page.getOffset());
        m.put("limit", page.getPageLength());
        try {
            return this.template.query(REQUEST_COMPANIES_OFFSET, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return new ArrayList<>();
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }

    /**
     * renvoie la taille de la table company.
     *
     * @return le nombre d'entreprises enregistrées dans la base
     * @throws PersistanceException
     */
    @Override
    public int getNumberOfElements() throws PersistanceException {
        try {
            return this.template.queryForObject(REQUEST_NB_OF_ROWS, Collections.emptyMap(),
                    (res, rowNum) -> new Integer(res.getInt(1)));

        } catch (EmptyResultDataAccessException e) {
            LOG.info("", e);
            return 0;
        } catch (DataAccessException e) {
            throw new PersistanceException(e);
        }
    }
}
