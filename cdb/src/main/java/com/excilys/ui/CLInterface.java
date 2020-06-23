package com.excilys.ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.service.CompanyValidator;
import com.excilys.service.ComputerInstanceProblems;
import com.excilys.service.ComputerValidator;
import com.excilys.service.InvalidComputerInstanceException;
import com.excilys.springconfig.AppConfig;

/**
 * Interface utilisateur permettant l'interaction avec la BD par ligne de
 * commande
 *
 * @author jguyot2
 */

public class CLInterface {

    private static CompanyValidator companyValidator = AppConfig.getContext().getBean(CompanyValidator.class);

    private static ComputerValidator computerValidator = AppConfig.getContext().getBean(ComputerValidator.class);
    private static Scanner sc = new Scanner(System.in).useDelimiter("\n");

    /**
     * Fonction affichant un menu et exécutant une commande rentrée par
     * l'utilisateur
     */
    public static void getCommande() {
        printMenu();
        String strCommandId = sc.nextLine();
        int commandId = -1;
        try {
            commandId = Integer.parseInt(strCommandId);
        } catch (NumberFormatException e) {
            System.out.println("La commande rentrée est invalide");
        }

        try {
            CLICommand commandToExecute = CLICommand.getCommandeFromInput(commandId);
            executeCommand(commandToExecute);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur" + e.getMessage());
        }
    }

    public static void start() {
        while (true) {
            CLInterface.getCommande();
        }
    }

    private static void createComputerCommand() {
        System.out.println("Entrez le nom de l'ordinateur");

        String computerName = sc.nextLine().trim();
        System.out.println("Nom entré:'" + computerName + "'");

        System.out.println("Entrez l'identifiant de la compagnie associée (ou une ligne vide pour ne rien ajouter)");
        String strCompanyId = sc.nextLine().trim();
        System.out.println("ID entré:" + strCompanyId);

        Company company = null;

        if (!strCompanyId.isEmpty()) {
            long companyId = 0;
            try {
                companyId = Long.parseLong(strCompanyId);
            } catch (NumberFormatException e) {
                System.out.println("L'identifiant entré ne correspond pas à un ID. Fin de la saisie");
                return;
            }
            Optional<Company> companyOpt = companyValidator.findById(companyId);
            if (companyOpt.isPresent()) {
                company = companyOpt.get();
            } else {
                System.out.println("Entreprise non trouvée dans la bd");
            }
        }
        System.out.println("Company:" + company);
        System.out.println("Entrez la date d'introduction au format DD/MM/YYYY");
        String dateStr = sc.nextLine().trim();
        System.out.println("STR INTRO:" + dateStr);
        LocalDate introduced = null;
        if (!dateStr.isEmpty()) {
            try {
                Optional<LocalDate> introducedOpt = DateMapper.stringToLocalDate(dateStr);
                if (introducedOpt.isPresent()) {
                    introduced = introducedOpt.get();
                } else {
                    return;
                }
            } catch (DateTimeParseException e) {
                System.out.println(e.getMessage());
                System.out.println("Fin de la saisie, car date no parsable");
                return;
            }
        }
        System.out.println("Date:" + introduced);
        System.out.println("Entrez la date d'arrêt de production au format DD/MM/YYYY");
        String strDiscontinuation = sc.nextLine().trim();
        LocalDate discontinued = null;
        if (!strDiscontinuation.isEmpty()) {
            try {
                Optional<LocalDate> discontinuedOpt = DateMapper.stringToLocalDate(strDiscontinuation);
                if (!discontinuedOpt.isPresent()) {
                    return;
                }
                discontinued = discontinuedOpt.get();
            } catch (DateTimeParseException e) {
                System.out.println("Erreur : " + e.getMessage());
                System.out.println("Date saisie invalide, fin de l'entrée");
                return;
            }
        }

        Computer createdComputer = new Computer(computerName, company, introduced, discontinued);
        long newIdComputer;
        try {
            newIdComputer = computerValidator.addComputer(createdComputer);
        } catch (InvalidComputerInstanceException e) {
            System.out.println("Instance incorrecte de Computer crée");
            for (ComputerInstanceProblems problem : e.getProblems()) {
                System.out.println(problem.getExplanation());
            }
            System.out.println("Fin de la saisie");
            return;
        }
        if (newIdComputer == 0) {
            System.out.println("L'ordinateur n'a pas été enregistré");
        } else {
            System.out.println("L'ordinateur a été créé, et est d'id :" + newIdComputer);
        }
    }

    private static void deleteCompanyCommand() {
        System.out.println("écrivez l'id de l'entreprise à détruire");
        String line = sc.nextLine().trim();
        System.out.println("id de l'entreprise : " + line);
        try {
            long id = Long.parseLong(line);
            Optional<Company> comp = companyValidator.findById(id);
            if (comp.isPresent()) {
                System.out.println("Entreprise à détruire : " + comp.get());
                System.out.println("écrivez 1 pour confirmer, autre chose pour annuler");
                if ("1".equals(sc.nextLine().trim())) {
                    int nbdeletedCompany = companyValidator.deleteCompanyById(id);
                    if (nbdeletedCompany == 1) {
                        System.out.println("l'entreprise a été supprimée");
                    } else {
                        System.out.println("la suppression a foiré (nb = " + nbdeletedCompany + ")");
                    }
                }
                return;
            } else {
                System.out.println("Pas trouvé d'entreprise avec cet id");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Lecture utile : " + "https://tinyurl.com/y837kss2");
            return;
        }
    }

    private static void deleteComputerCommand() {
        System.out.println("Saisissez l'identifiant du pc à supprimer");
        String idString = sc.nextLine().trim();
        long id = 0;
        try {
            id = Long.parseLong(idString, 0);
        } catch (NumberFormatException e) {
            System.out.println("Identifiant invalide");
            return;
        }

        if (computerValidator.delete(id) > 0) {
            System.out.println("le PC a été supprimé");
        } else {
            System.out.println("L'ordinateur de n'a pas été supprimé dans la BD");
        }

    }

    /**
     * Exécution d'une commande donnée en paramètre
     *
     * @param commandToExecute La commande à exécuter
     */
    private static void executeCommand(final CLICommand commandToExecute) {
        switch (commandToExecute) {
        case LIST_COMPUTERS:
            listComputersCommand();
            break;
        case LIST_COMPANIES:
            listCompaniesCommand();
            break;
        case SHOW_DETAILS:
            getComputerDetailsCommand();
            break;
        case CREATE_COMPUTER:
            createComputerCommand();
            break;
        case UPDATE_COMPUTER:
            updateComputerCommand();
            break;
        case DELETE_COMPUTER:
            deleteComputerCommand();
            break;
        case EXIT:
            exitCommand();
            break;
        case COMPUTER_PAGINATION:
            ComputerPagination.paginate();
            break;
        case COMPANY_PAGINATION:
            CompanyPagination.paginate();
            break;
        case COMPANY_DELETE:
            deleteCompanyCommand();
            break;
        default:
            throw new RuntimeException("arrivée dans le default alors que c'est pas censé arriver");
        }
    }

    private static void exitCommand() {
        System.out.println("Sortie du programme");
        System.exit(0);
    }

    private static void getComputerDetailsCommand() {
        System.out.println("Entrez l'identifiant de l'ordinateur recherché");
        String strComputerId = sc.nextLine();
        long computerId = 0;
        try {
            computerId = Long.parseLong(strComputerId, 10);
        } catch (NumberFormatException e) {
            System.out.println("Ce qui a été entré ne correspond pas à un identifiant valide");
            return;
        }

        Optional<Computer> computerOpt = computerValidator.findById(computerId);
        if (!computerOpt.isPresent()) {
            System.out.println("L'ordinateur n'a pas été trouvé dans la BD");
        } else {
            Computer computer = computerOpt.get();
            System.out.println(computer);
        }
    }

    private static void listCompaniesCommand() {
        List<Company> companyList = companyValidator.fetchList();
        for (Company c : companyList) {
            System.out.println(c);
        }
    }

    private static void listComputersCommand() {
        List<Computer> computerList = computerValidator.fetchList();
        for (Computer c : computerList) {
            System.out.println(c.getShortDescription());
        }
    }

    private static void printMenu() {
        System.out.println("--------------------------------");
        System.out.println("Entrez la commande: ");
        System.out.println("0:\t Lister les ordinateurs");
        System.out.println("1:\t Lister les entreprises");
        System.out.println("2:\t Détails d'un ordinateur");
        System.out.println("3:\t Création d'un ordinateur");
        System.out.println("4:\t Mise à jour d'un ordinateur");
        System.out.println("5:\t Suppression d'un ordinateur");
        System.out.println("6:\t Sortie du programme");
        System.out.println("7:\t Pagination des ordinateurs");
        System.out.println("8:\t Pagination des entreprises");
        System.out.println("9:\t Suppression d'une entreprise");
        System.out.println("--------------------------------");
    }

    private static void updateComputerCommand() {
        System.out.println("Entrez l'id de l'ordinateur à modifier");
        String idString = sc.nextLine().trim();
        long id = 0;
        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException e) {
            System.out.println("La valeur entrée ne correspond pas à un ID. ");
            return;
        }

        Optional<Computer> optFoundComputer = computerValidator.findById(id);
        if (!optFoundComputer.isPresent()) {
            System.out.println("Ordinateur non trouvé.");
            return;
        }
        Computer foundComputer = optFoundComputer.get();

        System.out.println("Modification du nom: Ne rien entrer pour ne pas changer, entrer un nom sinon");
        System.out.println("Nom courant: " + foundComputer.getName());
        String newName = sc.nextLine().trim();
        if (!newName.isEmpty()) {
            foundComputer.setName(newName);
        }
        System.out.println("Modification de l'identifiant de l'entreprise: "
                + "Ne rien entrer pour ne pas changer, entrer 'NONE' pour supprimer l'entreprise");

        System.out.println("Entreprise courante:" + foundComputer.getManufacturer());
        String newEnterpriseIdStr = sc.nextLine().trim();
        if (!newEnterpriseIdStr.isEmpty()) {
            if (newEnterpriseIdStr.equals("NONE")) {
                foundComputer.setId(0);
            } else {
                long idComputer = 0;
                try {
                    idComputer = Long.parseLong(newEnterpriseIdStr);
                } catch (NumberFormatException e) {
                    System.out.println("Id invalide entré, fin de la saisie");
                    return;
                }
                foundComputer.setId(idComputer);
            }
        }
        System.out.println("Entrez la date d'introduction sous forme `JJ/MM/AAAA`: "
                + "Ne rien entrer pour laisser la date d'intro telle quelle, entrer `NONE` "
                + "pour supprimer cette date");
        System.out.println("Date d'intro courante: " + foundComputer.getIntroduction());

        String strIntro = sc.nextLine().trim();
        if (!strIntro.isEmpty()) {
            if ("NONE".equals(strIntro)) {
                foundComputer.setIntroduction(null);
            } else {
                Optional<LocalDate> introDateOpt = DateMapper.stringToLocalDate(strIntro);
                if (!introDateOpt.isPresent()) {
                    return;
                }
                foundComputer.setIntroduction(introDateOpt.get());
            }
        }

        System.out.println("Entrez la date de discontinuation sous forme `JJ/MM/AAAA`: "
                + "Ne rien entrer pour laisser la date d'intro telle quelle, entrer `NONE` "
                + "pour supprimer cette date");
        System.out.println("Date d'intro courante: " + foundComputer.getDiscontinuation());

        String strDiscontinuation = sc.nextLine().trim();
        if (!strDiscontinuation.isEmpty()) {
            if ("NONE".equals(strDiscontinuation)) {
                foundComputer.setDiscontinuation(null);
            } else {
                Optional<LocalDate> localDateOpt = DateMapper.stringToLocalDate(strDiscontinuation);
                if (!localDateOpt.isPresent()) {
                    return;
                }

                foundComputer.setDiscontinuation(localDateOpt.get());
            }
        }
        int updated;
        try {
            updated = computerValidator.update(foundComputer);
        } catch (InvalidComputerInstanceException e) {
            System.out.println("Instance incorrecte de Computer crée");
            for (ComputerInstanceProblems problem : e.getProblems()) {
                System.out.println(problem.getExplanation());
            }
            System.out.println("Fin de la saisie");
            return;
        }
        if (updated > 0) {
            System.out.println("La mise a jour a été effectuée");
        } else {
            System.out.println("La mise à jour n'a pas eu lieu");
        }
    }
}
