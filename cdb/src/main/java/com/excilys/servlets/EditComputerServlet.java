package com.excilys.servlets;

import java.util.List;
import java.io.IOException;
import java.util.Optional;

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
 * Servlet affichant la page qui permet de modifier un ordinateur de la base
 * (GET) ou effectuant la modification (POST).
 * 
 * @author jguyot2
 *
 */
@WebServlet("/editComputer")
public class EditComputerServlet extends HttpServlet {
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(EditComputerServlet.class);
    private static CompanyDTOValidator companyValidator = new CompanyDTOValidator();
    private static ComputerDTOValidator computerValidator = new ComputerDTOValidator();
    private static final long serialVersionUID = 1L;

    /** */
    private ComputerDTOValidator validator = new ComputerDTOValidator();

    /**
     * Affiche la page permettant la modification d'un ordinateur de la base (ou
     * l'affichage d'une erreur si l'entrée est invalide).
     *
     * @param request requête get contenant le paramètre (nécessaire) "id"
     *        représentant l'identifiant de l'ordinateur.
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            LOG.trace("doGEt");
            String strId = request.getParameter("id");
            long id = Long.parseLong(strId);
            
            Optional<ComputerDTO> computer = this.validator.findById(id);
            if (!computer.isPresent()) {
                LOG.debug("The computer was not found");
                request.setAttribute("errorCause", "The Computer was not found");

                RequestDispatcher rd = request.getRequestDispatcher("/400");
                rd.forward(request, response);
                return;
            }
            List<CompanyDTO> companyList = companyValidator.fetchList();
            request.setAttribute("computer", computer.get());
            request.setAttribute("companyList", companyList);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/editComputer.jsp");
            rd.forward(request, response);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Paramètres : id computerName introduced discontinued companyId
     * 
     * @throws ServletException
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            try {
                ComputerDTO computer = getComputerDTOFromParameters(request);
                int updatedComputer = computerValidator.updateComputer(computer);
                if (updatedComputer == 0) {
                    RequestDispatcher rd = request.getRequestDispatcher("/400");
                    LOG.debug("update failed. DTO =" + computer);
                    request.setAttribute("errorCause",
                            "L'ordi a pas été mis à jour, probablement parce qu'il a un id inexistant");
                    rd.forward(request, response);
                    return;
                }
                if (updatedComputer == -1) {
                    LOG.error("-1 returned when updating the computer");
                    throw new ServletException("update : -1 retourné");
                }
            } catch (InvalidComputerDTOException e) {
                invalidComputerDTOCase(request, response, e);
                return;
            } catch (InvalidComputerInstanceException e) {
                invalidComputerCase(request, response, e);
                return;
            } catch (NumberFormatException e) {
                RequestDispatcher rd = request.getRequestDispatcher("/400");
                request.setAttribute("errorCause", "Invalid company identifier value");
                rd.forward(request, response);
                return;
            } catch (IllegalArgumentException e) {
                RequestDispatcher rd = request.getRequestDispatcher("/400");
                request.setAttribute("errorCause",  e.getMessage());
                rd.forward(request, response);
                return;
            }
            response.sendRedirect("/page");
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    /**
     * 
     * @param request
     * @return
     * @throws NumberFormatException quand le 
     * @throws IllegalArgumentException
     */
    private ComputerDTO getComputerDTOFromParameters(HttpServletRequest request)
            throws NumberFormatException, IllegalArgumentException {
        String idStr = request.getParameter("id"); // TODO : exception si l'id est invalide
        if (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim())) {
            throw new IllegalArgumentException("No valid identifier");
        }
        String name = request.getParameter("computerName");
        String intro = request.getParameter("introduced");
        String disco = request.getParameter("discontinued");
        String companyId = request.getParameter("companyId");
        CompanyDTO company = null;
        if (companyId != null && !"0".equals(companyId)) {
            company = companyValidator.findById(Long.parseLong(companyId)).orElse(null); // TODO
        }
        return new ComputerDTO(name, idStr, company, intro, disco);
    }

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
