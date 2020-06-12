package com.excilys.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Classe permettant de récupérer la connexion à la base de données.
 *
 * @author jguyot2
 *
 */
public final class DBConnection {

    private static HikariDataSource ds;

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerUpdater.class);

    /** */

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            init();
        }
        LOG.trace("Récupération d'une connexion");
        return ds.getConnection();
    }

    private static void init() {
        LOG.trace("intiialisation du dataSource");
        HikariConfig config = new HikariConfig("/hikaricp.properties");
        ds = new HikariDataSource(config);
    }
}
