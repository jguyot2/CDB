package com.excilys.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.CompanyDTO;
import com.excilys.model.ComputerDTO;
import com.excilys.service.CompanyDTOValidator;
import com.excilys.service.ComputerDTOProblems;
import com.excilys.service.ComputerDTOValidator;
import com.excilys.service.ComputerInstanceProblems;
import com.excilys.service.InvalidComputerDTOException;
import com.excilys.service.InvalidComputerInstanceException;

/**
 * Servlet de création d'ordinateur
 *
 * @author jguyot2
 *
 */
@WebServlet("/addComputer")
public class AddComputerServlet extends HttpServlet {
    private static CompanyDTOValidator companyValidator = new CompanyDTOValidator();
    private static ComputerDTOValidator computerValidator = new ComputerDTOValidator();

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(AddComputerServlet.class);

    /** */
    private static final long serialVersionUID = 1L;

    public static void setCompanyValidator(final CompanyDTOValidator dtv) {
        companyValidator = dtv;
    }

    public static void setComputerValidator(final ComputerDTOValidator dtv) {
        computerValidator = dtv;
    }

    /**
     * Crée une instance de CompanyDTO a partir de la requête POST pour créer un
     * ordinateur.
     *
     * @param request la requête ayant provoqué
     * @return une instance de CompanyDTO qui peut être nulle
     * @throws NumberFormatException si l'identifiant du DTO est incorrect (= n'est
     *         pas un nombre)
     * @throws InvalidComputerDTOException si l'identifiant de l'entreprise ne
     *         représente aucune entreprise.
     */
    private static CompanyDTO getCompanyDTOFromCompanyIdParameter(final HttpServletRequest request)
            throws NumberFormatException, InvalidComputerDTOException {
        String companyId = request.getParameter("companyId");
        CompanyDTO company = null;
        if (companyId != null && !"".equals(companyId) && !"0".equals(companyId)) {
            long id = Long.parseLong(companyId);
            company = companyValidator.findById(id).orElseThrow(() -> new InvalidComputerDTOException(
                    Arrays.asList(ComputerDTOProblems.INEXISTENT_MANUFACTURER)));
        }
        return company;
    }

    /**
     * Gestion du cas où l'instance de Computer correspondant aux paramètres entrés
     * n'est pas correcte => redirection vers une page d'erreur comportant un
     * message.
     *
     * @param request
     * @param response
     * @param exnInvalidComputer l'exception récupérée lors de la tentative d'ajout
     *        de l'ordinateur à la base
     * @throws IOException
     * @throws ServletException
     */
    private static void invalidComputerCase(final HttpServletRequest request,
            final HttpServletResponse response, final InvalidComputerInstanceException exnInvalidComputer)
            throws IOException, ServletException {
        List<ComputerInstanceProblems> problems = exnInvalidComputer.getProblems();
        StringBuilder problemsDescription = new StringBuilder();
        for (ComputerInstanceProblems problem : problems) {
            problemsDescription.append(problem.getExplanation() + "\n");
            LOG.debug("Cause : " + problem.getExplanation());
        }
        RequestDispatcher rd = request.getRequestDispatcher("/400");
        request.setAttribute("errorCause", problemsDescription.toString());

        rd.forward(request, response);
    }

    /**
     * Gestion du cas où l'instance de DTOComputer ajoutée à la base est incohérente
     * => Redirection vers une page d'erreur avec le message approprié
     *
     * @param request
     * @param response
     * @param catchedException
     * @throws IOException
     * @throws ServletException
     */
    private static void invalidComputerDTOCase(final HttpServletRequest request,
            final HttpServletResponse response, final InvalidComputerDTOException catchedException)
            throws IOException, ServletException {
        LOG.debug("DTO invalide");
        List<ComputerDTOProblems> problems = catchedException.getProblems();
        StringBuilder problemsDescription = new StringBuilder();
        for (ComputerDTOProblems problem : problems) {
            problemsDescription.append(problem.getExplanation() + "\n");
            LOG.debug("Cause : " + problem.getExplanation());
        }
        RequestDispatcher rd = request.getRequestDispatcher("/400");
        request.setAttribute("errorCause", problemsDescription.toString());

        rd.forward(request, response);
    }

    /**
     * Affichage de la page de création d'un ordinateur.
     *
     * @param request
     * @param response
     * @throws ServletException Si une exception quelconque s'est produite pendant
     *         l'appel
     * @throws IOException
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        LOG.info("Création d'un pc (get)");
        List<CompanyDTO> companyList = companyValidator.fetchList();
        request.setAttribute("companyList", companyList);
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/addComputer.jsp");
        rd.forward(request, response);

    }

    /**
     * Ajoute un ordinateur dans la base.
     *
     * @param request requête avec les paramètres suivants: - computerName : le nom
     *        de l'ordinateur - introduced : Date d'introduction - discontinued :
     *        Date d'arrêt de production - companyId : id de l'entreprise
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        try {
            LOG.info("ajout d'un pc dans la base");
            ComputerDTO cpt = getComputerDTOFromParameters(request);
            LOG.info("PC ajouté : " + cpt);
            computerValidator.addComputerDTO(cpt);
        } catch (InvalidComputerDTOException e) {
            LOG.debug("Instance invalide de DTO passée en param");
            invalidComputerDTOCase(request, response, e);
            return;
        } catch (InvalidComputerInstanceException e) {
            invalidComputerCase(request, response, e);
            return;
        }
        response.sendRedirect("page");

    }

    /**
     * Récupération d'une instance de ComputerDTO à partir des paramètres de requête
     * pour ajouter un ordinateur à la base
     *
     * @param request la requête (POST)
     * @return
     * @throws NumberFormatException
     * @throws InvalidComputerDTOException
     */
    private ComputerDTO getComputerDTOFromParameters(final HttpServletRequest request)
            throws NumberFormatException, InvalidComputerDTOException {
        String computerName = request.getParameter("computerName");
        LOG.debug("computer name =" + computerName);
        String introStr = request.getParameter("introduced");
        String discoStr = request.getParameter("discontinued");
        CompanyDTO company = getCompanyDTOFromCompanyIdParameter(request);
        return new ComputerDTO(computerName, null, company, introStr, discoStr);
    }
}
