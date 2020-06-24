package com.excilys.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.Computer;

/**
 * Classe permettant d'effectuer des mises à jour sur les éléments de la table
 * computer à partir d'instances de Computer.
 *
 * @author jguyot2
 */
@Repository
public class ComputerUpdater {

    /** */
    private static final String CREATE_COMPUTER = "INSERT INTO computer(name, introduced, discontinued, company_id) "
            + "VALUES (:name, :introduced, :discontinued, :companyId)";

    /** */
    private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = :id";

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerUpdater.class);

    /** */
    private static final String UPDATE_COMPUTER = "UPDATE computer SET "
            + "name = :name, introduced = :introduced, discontinued = :discontinued, company_id = :companyId"
            + " WHERE id = :id";

    private static final String REQUEST_DELETE_COMPUTER_FROM_COMPANY_ID = "DELETE FROM computer WHERE company_id = ?";

    @Autowired
    private NamedParameterJdbcTemplate template;

    /** */
    public ComputerUpdater() {
    }

    /**
     * Ajoute une ligne à la base de données, correspondant à l'instance de Computer
     * donnée en paramètre.
     *
     * @param newComputer l'instance de Computer à enregistrer dans la base de
     *                    données
     *
     * @return le nouvel identifiant correspondant à la ligne ajoutée si l'ajout a
     *         réussi, 0 si l'ajout a raté
     */ // REFACTO
    public long createComputer(final Computer newComputer) {
        LOG.trace("Création de l'instance de Computer suivante: " + newComputer);
        Date introDate = DateMapper.localDateToSqlDate(newComputer.getIntroduction()).orElse(null);
        Date discoDate = DateMapper.localDateToSqlDate(newComputer.getDiscontinuation()).orElse(null);

        Map<String, Object> requestParameters = new HashMap<>();

        requestParameters.put("name", newComputer.getName());
        requestParameters.put("introduced", introDate);
        requestParameters.put("discontinued", discoDate);
        Company manufacturer = newComputer.getManufacturer();
        requestParameters.put("companyId", manufacturer == null ? null : manufacturer.getId());
        KeyHolder kh = new GeneratedKeyHolder();
        SqlParameterSource sqlParamSource = new MapSqlParameterSource(requestParameters);
        int nbRows = this.template.update(CREATE_COMPUTER, sqlParamSource, kh, new String[] { "id" });
        return kh.getKey().longValue();
    }

    /**
     * Suppression d'un ordinateur de la base à partir de son identifiant.
     *
     * @param id l'identifiant
     *
     * @return 1 si la suppression est effective, 0 sinon
     *
     * @throws SQLException
     */
    public int deleteById(final long id) {
        LOG.trace("Suppression du pc d'id : " + id);
        Map<String, Object> requestParameters = new HashMap<>();
        requestParameters.put("id", id);
        return this.template.update(DELETE_COMPUTER, requestParameters);
    }

    /**
     * Suppression des ordinateurs d'un fabricant dont l'identifiant est en
     * paramètre
     *
     * @param manufacturerId l'identifiant du fabricant dont il faut supprimer les
     *                       ordinateurs.
     * @param conn           la connexion à utiliser
     * @return le nombre de PC supprimés
     * @throws SQLException
     */
    public int deleteComputersFromManufacturerIdWithConnection(final long manufacturerId, final Connection conn)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(REQUEST_DELETE_COMPUTER_FROM_COMPANY_ID)) {
            stmt.setLong(1, manufacturerId);
            return stmt.executeUpdate();
        }
    }

    /**
     * Met à jour l'ordinateur en paramètre, dont l'identifiant est intialisé.
     *
     * @param newComputer la valeur de l'ordinateur à laquelle on veut faire
     *                    correspondre la ligne
     *
     * @return 1 si la mise à jour a eu lieu, 0 sinon
     */
    public int updateComputer(final Computer newComputer) {
        LOG.trace("Mise à jour de l'instance de Computer suivante " + newComputer.toString());

        LOG.trace("Création de l'instance de Computer suivante: " + newComputer);
        Date introDate = DateMapper.localDateToSqlDate(newComputer.getIntroduction()).orElse(null);
        Date discoDate = DateMapper.localDateToSqlDate(newComputer.getDiscontinuation()).orElse(null);

        Map<String, Object> requestParameters = new HashMap<>();

        requestParameters.put("name", newComputer.getName());
        requestParameters.put("introduced", introDate);
        requestParameters.put("discontinued", discoDate);
        Company manufacturer = newComputer.getManufacturer();
        requestParameters.put("companyId", manufacturer == null ? null : manufacturer.getId());
        requestParameters.put("id", newComputer.getId());
        return this.template.update(UPDATE_COMPUTER, requestParameters);
    }
}
