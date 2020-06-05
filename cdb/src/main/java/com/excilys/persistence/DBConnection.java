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
    /////////////////////////////////////////////////////////
    private static HikariConfig config = new HikariConfig();

    /** */
    private static final String DB_NAME = "computer-database-db";
    /** */
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static HikariDataSource ds;

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerUpdater.class);

    /** */
    private static final String PASSWORD = "qwerty1234";

    /** */
    private static final String URL_DB = "jdbc:mysql://localhost:3306/";
    /** */
    private static final String USERNAME = "admincdb";

    static {
        config.setDriverClassName(DRIVER_NAME);
        config.setJdbcUrl(URL_DB + DB_NAME + "?useLegacyDatetimeCode=false&serverTimezone=Europe/Paris");
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
