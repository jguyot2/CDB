package com.excilys.servlets;

import java.io.IOException;
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

@WebServlet("/addComputer")
public class ComputerCreationServlet extends HttpServlet {
    private static final CompanyDTOValidator companyValidator = new CompanyDTOValidator();
    private static final ComputerDTOValidator computerValidator = new ComputerDTOValidator();

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerCreationServlet.class);

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Affichage de la page de création d'un ordinateur.
     *
     * @param request
     * @param response
     * @throws ServletException Si une exception quelconque s'est produite pendant
     *         l'appel,
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            LOG.info("Création d'un pc (get)");
            List<CompanyDTO> companyList = companyValidator.fetchList();
            companyList.add(0, new CompanyDTO("no company", "0")); // legit ?

            request.setAttribute("companyList", companyList);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/addComputer.jsp");
            rd.forward(request, response);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new ServletException(e);
        }
    }

    /**
     * Ajoute un ordinateur dans la base.
     *
     * @param request requête avec les paramètres suivants: - computerName : le nom
     *        de l'ordinateur - introduced : Date d'introduction (TODO : Vérifier le
     *        format) - discontinued : Date d'arrêt de production - companyId : id de
     *        l'entreprise
     * @param response
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            LOG.info("ajout d'un pc dans la base");
            ComputerDTO cpt = getComputerDTOFromParameters(request);
            LOG.info("PC ajouté : " + cpt);
            try {
                computerValidator.addComputerDTO(cpt);
            } catch (InvalidComputerDTOException e) {
                LOG.debug("Instance invalide de DTO passée en param");
                invalidComputerDTOCase(request, response, e);
                return;
            } catch (InvalidComputerInstanceException e) {
                this.invalidComputerCase(request, response, e);
                return;
            }
            response.sendRedirect("page");
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private CompanyDTO getCompanyDTOFromParameters(final HttpServletRequest request)
            throws NumberFormatException {
        String companyId = request.getParameter("companyId");
        CompanyDTO company = null;
        if (companyId != null && !"".equals(companyId) && !"0".equals(companyId)) {
            long id = Long.parseLong(companyId);
            company = companyValidator.findById(id).orElse(null);
        }
        return company;
    }

    private ComputerDTO getComputerDTOFromParameters(final HttpServletRequest request)
            throws NumberFormatException {
        String computerName = request.getParameter("computerName");
        LOG.debug("computer name =" + computerName);
        String introStr = request.getParameter("introduced");
        String discoStr = request.getParameter("discontinued");
        CompanyDTO company = getCompanyDTOFromParameters(request);
        return new ComputerDTO(computerName, null, company, introStr, discoStr);
    }

    private static void invalidComputerCase(final HttpServletRequest request, final HttpServletResponse response,
            final InvalidComputerInstanceException exnInvalidComputer) throws IOException, ServletException {
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
}
