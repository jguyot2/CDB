package com.excilys.persistence;

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
 * Classe permettant d'effectuer des requêtes sur des
 * Computers.
 *
 * @author jguyot2
 */
public class ComputerSearcher implements Searcher<Computer> {
    /** */
    private static final Logger LOG = LoggerFactory
            .getLogger(ComputerSearcher.class);

    /** */
    private static final String QUERY_COMPUTER_LIST =
            " SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name " + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id";

    /** */
    private static final String QUERY_COMPUTER_FROM_ID =
            "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name "
            + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id "
            + "WHERE computer.id = ? ";

    /** */
    private static final String QUERY_COMPUTER_WITH_OFFSET =
            "SELECT computer.id, computer.name, introduced, discontinued, "
            + "company.id, company.name "
            + "FROM computer LEFT JOIN company "
            + "ON computer.company_id = company.id "
            + "ORDER BY computer.id " + "LIMIT ? OFFSET ?";

    /** */
    private static final String REQUEST_NB_OF_ROWS =
            "SELECT count(id) FROM computer";

    /** */
    public ComputerSearcher() {
    }

    /**
     * Recherche un ordinateur dans la base de donnée, à partir
     * d'un identifiant.
     *
     * @param searchedId L'id de l'ordinateur recherché
     *
     * @return une instance de Optional vide si aucun ordinateur
     *         de l'id donné n'est
     *         présent dans la BD ou qu'une exception
     *         SQLException s'est produite/
     *         Une instance de Optional contenant le Computer
     *         trouvé sinon
     */
    public Optional<Computer> fetchById(final long searchedId)
            throws SQLException {

        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(QUERY_COMPUTER_FROM_ID)) {

            stmt.setLong(1, searchedId);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {

                LOG.debug("La recherche avec l'id " + searchedId
                        + " n'a renvoyé aucun résultat");
                return Optional.empty();
            }
            Computer foundComputer = getComputerFromResultSet(res);
            return Optional.of(foundComputer);
        }
    }

    /**
     * Cherche tous les ordinateurs dans la base, et retourne la
     * liste correspondant
     * à ces derniers.
     *
     * @return La liste des ordinateurs présents dans la base de
     *         données
     *
     * @author jguyot2
     */
    public List<Computer> fetchList() throws SQLException {
        List<Computer> computerList = new ArrayList<>();
        try (Statement stmt = DBConnection.getConnection().createStatement();) {

            ResultSet res = stmt.executeQuery(QUERY_COMPUTER_LIST);
            while (res.next()) {

                Computer computer = getComputerFromResultSet(res);
                computerList.add(computer);
            }
        }
        return computerList;
    }

    /**
     * Renvoie la liste des Computer "compris" dans la page en
     * paramètre.
     *
     * @param page
     *
     * @return La liste correspondant aux ordinateurs compris
     *         dans la page.
     */
    public List<Computer> fetchWithOffset(final Page page) throws SQLException {

        List<Computer> computerList = new ArrayList<>();

        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(QUERY_COMPUTER_WITH_OFFSET)) {
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
     * Fonction permettant de récupérer une instance de Computer
     * à partir d'une
     * ligne de ResultSet à partir de requêtes comportant un
     * schéma défini.
     *
     * @param res l'instance de ResultSet résultant d'une
     *            requête sur les tables
     *            computer et company, qui pointe vers une des
     *            lignes renvoyée par
     *            la requête
     *
     * @return une instance de Computer correspondant à la ligne
     *         sur laquelle le
     *         curseur de res pointe
     *
     * @throws SQLException
     */
    private Computer getComputerFromResultSet(final ResultSet res)
            throws SQLException {
        long computerId = res.getLong("computer.id");
        String computerName = res.getString("computer.name");

        Optional<LocalDate> introducedDateOpt = DateMapper
                .sqlDateToLocalDate(res.getDate("introduced"));
        LocalDate introduced = null;
        if (introducedDateOpt.isPresent()) {
            introduced = introducedDateOpt.get();
        }
        Optional<LocalDate> discontinuedDateOpt = DateMapper
                .sqlDateToLocalDate(res.getDate("discontinued"));
        LocalDate discontinued = null;
        if (discontinuedDateOpt.isPresent()) {
            discontinued = discontinuedDateOpt.get();
        }
        String companyName = res.getString("company.name");

        Company company = null;
        if (companyName != null) {

            long companyId = res.getLong("company.id");
            company = new Company(companyName, companyId);
        }

        Computer computer = new Computer(computerName, company, introduced,
                discontinued, computerId);
        return computer;
    }

    /**
     * @return Le nombre d'éléments computer enregistrés dans la base
     */
    public int getNumberOfElements() throws SQLException {
        try (Statement stmt = DBConnection.getConnection().createStatement()) {

            ResultSet res = stmt.executeQuery(REQUEST_NB_OF_ROWS);
            if (res.next()) {
                return res.getInt(1);
            }
        }
        LOG.error("Récupération de la taille : Pas de résultat correct");
        return -1; // TODO : lancer une exception dans ce cas
    }
}
