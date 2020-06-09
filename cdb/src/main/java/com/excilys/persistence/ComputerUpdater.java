package com.excilys.persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mapper.DateMapper;
import com.excilys.model.Computer;

/**
 * Classe permettant d'effectuer des mises à jour sur les éléments de la table
 * computer à partir d'instances de Computer.
 *
 * @author jguyot2
 */
public class ComputerUpdater {
    /** */
    private static final String CREATE_COMPUTER = "INSERT INTO computer(name, introduced, discontinued, company_id) "
            + "VALUES (?, ?, ?, ?)";

    /** */
    private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerUpdater.class);

    /** */
    private static final String UPDATE_COMPUTER = "UPDATE computer SET "
            + "name = ?, introduced = ?, discontinued = ?, company_id = ?" + " WHERE id = ?";

    /** */
    public ComputerUpdater() {
    }

    /**
     * Ajoute une ligne à la base de données, correspondant à l'instance de Computer
     * donnée en paramètre.
     *
     * @param newComputer l'instance de Computer à enregistrer dans la base de
     *        données
     *
     * @return le nouvel identifiant correspondant à la ligne ajoutée si l'ajout a
     *         réussi, 0 si l'ajout a raté
     */
    public long createComputer(final Computer newComputer) throws SQLException {
        LOG.info("Création de l'instance de Computer suivante: " + newComputer);
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(CREATE_COMPUTER,
                Statement.RETURN_GENERATED_KEYS)) {

            Optional<Date> introDateOpt = DateMapper.localDateToSqlDate(newComputer.getIntroduction());
            Date introDate = introDateOpt.orElse(null);

            Optional<Date> discoDateOpt = DateMapper.localDateToSqlDate(newComputer.getDiscontinuation());
            Date discoDate = discoDateOpt.orElse(null);

            stmt.setString(1, newComputer.getName());
            stmt.setDate(2, introDate);
            stmt.setDate(3, discoDate);

            if (newComputer.getManufacturer() == null) {
                stmt.setNull(4, java.sql.Types.BIGINT);
            } else {
                stmt.setLong(4, newComputer.getManufacturer().getId());
            }
            stmt.executeUpdate();

            ResultSet keySet = stmt.getGeneratedKeys();
            if (!keySet.next()) {
                LOG.error("Pas de PC créé");
                return 0;
            }
            return keySet.getLong(1);
        }
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
    public int deleteById(final long id) throws SQLException {
        LOG.info("Suppression du pc d'id : " + id);
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(DELETE_COMPUTER)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        }
    }

    /**
     * Met à jour l'ordinateur en paramètre, dont l'identifiant est intialisé.
     *
     * @param newComputer la valeur de l'ordinateur à laquelle on veut faire
     *        correspondre la ligne
     *
     * @return 1 si la mise à jour a eu lieu, 0 sinon
     */
    public int updateComputer(final Computer newComputer) throws SQLException {
        LOG.info("Mise à jour de l'instance de Computer suivante " + newComputer.toString());

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(UPDATE_COMPUTER)) {

            long id = newComputer.getId();
            Optional<Date> introDateOpt = DateMapper.localDateToSqlDate(newComputer.getIntroduction());
            Date introDate = introDateOpt.orElse(null);
            Optional<Date> discoDateOpt = DateMapper.localDateToSqlDate(newComputer.getDiscontinuation());
            Date discoDate = discoDateOpt.orElse(null);

            stmt.setString(1, newComputer.getName());
            stmt.setDate(2, introDate);
            stmt.setDate(3, discoDate);
            if (newComputer.getManufacturer() == null) {
                stmt.setNull(4, java.sql.Types.BIGINT);
            } else {
                stmt.setLong(4, newComputer.getManufacturer().getId());
            }
            stmt.setLong(5, id);
            return stmt.executeUpdate();
        }
    }
}
