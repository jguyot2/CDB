package com.excilys.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyUpdater {
    private static final Logger LOG = LoggerFactory.getLogger(CompanyUpdater.class);
    private static final String REQUEST_DELETE_COMPANY = "DELETE FROM company WHERE id = ?";
    private ComputerUpdater computerUpdater = new ComputerUpdater();

    public int deleteCompany(long companyId) throws SQLException {
        LOG.trace("Deletion of company nb. " + companyId);
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            int nbComputersDeleted = this.computerUpdater
                    .deleteComputersFromManufacturerIdWithConnection(companyId, conn);
            LOG.info(nbComputersDeleted + " computers deleted");
            int numberofDeletedCompanies = this.deleteCompany(companyId, conn);
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

    private int deleteCompany(long id, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(REQUEST_DELETE_COMPANY)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate();
        }
    }
}
