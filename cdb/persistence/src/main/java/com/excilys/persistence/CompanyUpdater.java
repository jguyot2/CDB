package com.excilys.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Gestion de la mise à jour des entreprises dans la base
 *
 * @author jguyot2
 */
@Repository
public class CompanyUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(CompanyUpdater.class);
    private static final String REQUEST_DELETE_COMPANY = "DELETE FROM company WHERE id = ?";

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private ComputerUpdater computerUpdater;

    /**
     * Suppression d'une entreprise à partir de son identifiant, ainsi que tous les ordinateurs liés
     * à cette entreprise
     *
     * @param companyId l'identifiant de l'entreprise à supprimer.
     * @return 1 si l'entreprise a été supprimée, 0 si l'id n'existe pas.
     * @throws SQLException si erreur dans la base
     */
    public int deleteCompany(final long companyId) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(this.template.getDataSource());
            conn.setAutoCommit(false);
            int nbComputersDeleted = this.computerUpdater.deleteComputersFromManufacturerIdWithConnection(companyId,
                    conn);
            LOG.info(nbComputersDeleted + " computers deleted");
            int numberofDeletedCompanies = this.deleteCompany(companyId, conn);
            // Test si une entreprise a été supprimée ?
            conn.commit();
            return numberofDeletedCompanies;
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            if (conn != null) {
                LOG.info("rollback");
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Suppression de l'entreprise dont l'id est en paramètre, en utilisant la connexion en
     * paramètre.
     *
     * @param id   l'identifiant de l'entreprise à supprimer
     * @param conn la connexion à utiliser pour l'identifiant
     * @return 0 si aucune entreprise n'a été supprimée, 1 si l'etnreprise a été supprimée
     * @throws SQLException si erreur dans la base
     */
    private int deleteCompany(final long id, @NonNull final Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(REQUEST_DELETE_COMPANY)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        }
    }
}
