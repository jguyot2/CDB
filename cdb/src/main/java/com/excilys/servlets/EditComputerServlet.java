package com.excilys.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
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
import com.excilys.springconfig.AppConfig;

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

    private static CompanyDTOValidator companyValidator = AppConfig.getContext().getBean(CompanyDTOValidator.class);

    private static ComputerDTOValidator computerValidator = AppConfig.getContext().getBean(ComputerDTOValidator.class);

    private static final long serialVersionUID = 1L;

    private static void forwardToError400Page(final HttpServletRequest request, final HttpServletResponse response,
            final String errorCause) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/400");
        request.setAttribute("errorCause", errorCause);
        rd.forward(request, response);
    }

    private static void invalidComputerCase(final HttpServletRequest request, final HttpServletResponse response,
            final InvalidComputerInstanceException exnInvalidComputer) throws IOException, ServletException {

        List<ComputerInstanceProblems> problems = exnInvalidComputer.getProblems();
        StringBuilder problemsDescription = new StringBuilder();
        for (ComputerInstanceProblems problem : problems) {
            problemsDescription.append(problem.getExplanation() + "\n");
            LOG.debug("Cause : " + problem.getExplanation());
        }
        forwardToError400Page(request, response, problemsDescription.toString());
    }

    private static void invalidComputerDTOCase(final HttpServletRequest request, final HttpServletResponse response,
            final InvalidComputerDTOException catchedException) throws IOException, ServletException {
        LOG.debug("DTO invalide");
        List<ComputerDTOProblems> problems = catchedException.getProblems();
        StringBuilder problemsDescription = new StringBuilder();
        for (ComputerDTOProblems problem : problems) {
            problemsDescription.append(problem.getExplanation() + "\n");
            LOG.debug("Cause : " + problem.getExplanation());
        }
        forwardToError400Page(request, response, problemsDescription.toString());
    }

    /**
     * Affiche la page permettant la modification d'un ordinateur de la base (ou
     * l'affichage d'une erreur si l'entrée est invalide).
     *
     * @param request  requête get contenant le paramètre (nécessaire) "id"
     *                 représentant l'identifiant de l'ordinateur.
     * @param response
     * @throws IOException
     */ // REFACTO
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd;
        try {
            LOG.trace("doGet");
            String strId = request.getParameter("id");
            long id = Long.parseLong(strId);
            LOG.debug("parsed number : " + strId);
            Optional<ComputerDTO> computer = computerValidator.findById(id);

            if (!computer.isPresent()) {
                LOG.debug("The computer was not found");
                forwardToError400Page(request, response, "The computer was not found");
            }
            List<CompanyDTO> companyList = companyValidator.fetchList();
            request.setAttribute("computer", computer.get());
            if (computer.get().getCompany() != null) {
                companyList.remove(computer.get().getCompany());
            }

            request.setAttribute("companyList", companyList);
            rd = request.getRequestDispatcher("WEB-INF/views/editComputer.jsp");
            rd.forward(request, response);
            return;
        } catch (NumberFormatException e) {
            LOG.debug("Catched NFE");
            String msg = "Invalid identifier parameter";
            forwardToError400Page(request, response, msg);
            return;
        }
    }

    /**
     * Paramètres : id computerName introduced discontinued companyId représentant
     * la nouvelle valeur de l'ordinateur modifié
     *
     * @throws ServletException
     */ // REFACTO
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ComputerDTO computer = getComputerDTOFromParameters(request);
            int updatedComputer = computerValidator.updateComputer(computer);
            if (updatedComputer == 0) {
                LOG.debug("update failed. DTO =" + computer);
                forwardToError400Page(request, response,
                        "L'ordi a pas été mis à jour, probablement parce qu'il a un id inexistant");
                return;
            }
            if (updatedComputer == -1) { // TODO : pê une redirection avec un message d'erreur
                LOG.error("-1 returned when updating the computer");
                throw new ServletException("update : -1 retourné");
            }
        } catch (InvalidComputerDTOException e) {
            LOG.debug("invalid computer dto");
            invalidComputerDTOCase(request, response, e);
            return;
        } catch (InvalidComputerInstanceException e) {
            LOG.debug("invalid computer instance");
            invalidComputerCase(request, response, e);
            return;
        } catch (NumberFormatException e) {
            LOG.debug("invalid number");
            forwardToError400Page(request, response, "Invalid company identifier value");
            return;
        } catch (IllegalArgumentException e) {
            LOG.debug("illegal identifier number");
            forwardToError400Page(request, response, e.getMessage());
            return;
        }
        response.sendRedirect(getServletContext().getContextPath() + "/page?message=" + MESSAGE_UPDATE_COMPUTER);
    }

    private static final String MESSAGE_UPDATE_COMPUTER = URLEncoder.encode("L'ordinateur a été mis à jour");

    /**
     * Construction d'un ComputerDTO à partir des params de requête POST de la page
     *
     * @param request
     * @return
     * @throws NumberFormatException       quand le
     * @throws IllegalArgumentException
     * @throws InvalidComputerDTOException
     */
    private ComputerDTO getComputerDTOFromParameters(final HttpServletRequest request)
            throws NumberFormatException, IllegalArgumentException, InvalidComputerDTOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty() || "0".equals(idStr.trim())) {
            throw new IllegalArgumentException("No valid identifier");
        }
        String name = request.getParameter("computerName");
        String intro = request.getParameter("introduced");
        String disco = request.getParameter("discontinued");
        String companyId = request.getParameter("companyId");
        CompanyDTO company = null;
        if (companyId != null && !"0".equals(companyId)) {
            company = companyValidator.findById(Long.parseLong(companyId)).orElseThrow(
                    () -> new InvalidComputerDTOException(Arrays.asList(ComputerDTOProblems.INEXISTANT_COMPANY_ID))); // TODO
        }
        return new ComputerDTO(name, idStr, company, intro, disco);
    }
}
