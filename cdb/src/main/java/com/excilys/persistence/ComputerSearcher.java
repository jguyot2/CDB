package com.excilys.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Page;

/**
 * Classe permettant d'effectuer des requêtes sur des Computers.
 *
 * @author jguyot2
 */
public class ComputerSearcher implements Searcher<Computer> {
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerSearcher.class);
    private static final String QUERY_COMPUTER_SEARCH_WITH_NAME = "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id " + "WHERE computer.name LIKE ?";

    /** */
    private static final String QUERY_COMPUTER_FROM_ID = "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id " + "WHERE computer.id = ? ";

    /** */
    private static final String QUERY_COMPUTER_LIST = " SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id";

    /** */
    private static final String QUERY_COMPUTER_WITH_OFFSET = "SELECT computer.id, computer.name,"
            + " introduced, discontinued, " + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id " + "ORDER BY computer.id " + "LIMIT ? OFFSET ?";

    private static final String QUERY_COMPUTER_SEARCH_NAME_WITH_OFFSET = "SELECT computer.id, computer.name,"
            + " introduced, discontinued, " + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "WHERE computer.name LIKE ?" + "ON computer.company_id = company.id " + "ORDER BY computer.id "
            + "LIMIT ? OFFSET ?";
    /** */
    private static final String REQUEST_NB_OF_ROWS = "SELECT count(id) FROM computer";
    private static final String REQUEST_NB_OF_ROWS_SEARCH = "SELECT count(id) FROM computer WHERE computer.name LIKE ?";

    /**
     * Fonction permettant de récupérer une instance de Computer à partir d'une ligne
     * de ResultSet à partir de requêtes comportant un schéma défini.
     *
     * @param res l'instance de ResultSet résultant d'une requête sur les tables
     *        computer et company, qui pointe vers une des lignes renvoyée par la
     *        requête
     *
     * @return une instance de Computer correspondant à la ligne sur laquelle le
     *         curseur de res pointe
     *
     * @throws SQLException
     */
    private static Computer getComputerFromResultSet(final ResultSet res) throws SQLException {
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

    /** */
    public ComputerSearcher() {
    }

    /**
     * Recherche un ordinateur dans la base de donnée, à partir d'un identifiant.
     *
     * @param searchedId L'id de l'ordinateur recherché
     *
     * @return une instance de Optional vide si aucun ordinateur de l'id donné n'est
     *         présent dans la BD ou qu'une exception SQLException s'est produite/
     *         Une instance de Optional contenant le Computer trouvé sinon
     */
    @Override
    public Optional<Computer> fetchById(final long searchedId) throws SQLException {

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QUERY_COMPUTER_FROM_ID)) {
            stmt.setLong(1, searchedId);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                LOG.debug("La recherche avec l'id " + searchedId + " n'a renvoyé aucun résultat");
                return Optional.empty();
            }
            Computer foundComputer = getComputerFromResultSet(res);
            return Optional.of(foundComputer);
        }
    }

    /**
     * Cherche tous les ordinateurs dans la base, et retourne la liste correspondant
     * à ces derniers.
     *
     * @return La liste des ordinateurs présents dans la base de données
     *
     * @author jguyot2
     */
    @Override
    public List<Computer> fetchList() throws SQLException {
        List<Computer> computerList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet res = stmt.executeQuery(QUERY_COMPUTER_LIST);
            while (res.next()) {
                Computer computer = getComputerFromResultSet(res);
                computerList.add(computer);
            }
        }
        return computerList;
    }

    /**
     * Renvoie la liste des Computer "compris" dans la page en paramètre.
     *
     * @param page
     *
     * @return La liste correspondant aux ordinateurs compris dans la page.
     */
    @Override
    public List<Computer> fetchWithOffset(final Page page) throws SQLException {
        List<Computer> computerList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QUERY_COMPUTER_WITH_OFFSET)) {

            stmt.setInt(1, page.getPageLength());
            stmt.setInt(2, page.getOffset());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                Computer computer = getComputerFromResultSet(res);
                computerList.add(computer);
            }
        }
        return computerList;
    }

    /**
     * @return Le nombre d'éléments computer enregistrés dans la base.
     */
    @Override
    public int getNumberOfElements() throws SQLException {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement()) {

            ResultSet res = stmt.executeQuery(REQUEST_NB_OF_ROWS);
            if (res.next()) {
                return res.getInt(1);
            }
        }
        LOG.error("Récupération de la taille : Pas de résultat correct");
        return -1;
    }

    public int getNumberOfFoundElements(String search) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(REQUEST_NB_OF_ROWS_SEARCH)) {
            String searchedPattern = "%" + search.replace("%", "\\%") + "%";
            stmt.setString(1, searchedPattern);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                LOG.error("no elements found");
                return 0;
            }
            return res.getInt(1);
        }

    }

    public List<Computer> searchByName(String researchedString) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QUERY_COMPUTER_SEARCH_WITH_NAME);) {
            String searchedPattern = "%" + researchedString.replace("%", "\\%") + "%";
            stmt.setString(1, searchedPattern);
            List<Computer> result = new ArrayList<>();
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                result.add(getComputerFromResultSet(res));
            }
            return result;
        }
    }

    public List<Computer> searchByNameWithPage(String researchedString, Page p) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QUERY_COMPUTER_SEARCH_NAME_WITH_OFFSET);) {
            String searchedPattern = "%" + researchedString.replace("%", "\\%") + "%";
            stmt.setString(1, searchedPattern);
            stmt.setInt(2, p.getLimit());
            stmt.setInt(3, p.getOffset());
            ResultSet res = stmt.executeQuery();
            List<Computer> result = new ArrayList<>();
            while (res.next()) {
                result.add(getComputerFromResultSet(res));
            }

            return result;
        }
    }
}
