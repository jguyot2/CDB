package com.excilys.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.ComputerDTO;
import com.excilys.model.Page;
import com.excilys.service.ComputerDTOValidator;

/**
 * Affichage de l'ensemble des ordinateurs dans une page unique.
 * @author jguyot2
 *
 */
@WebServlet("/list")
public class ShowComputerListServlet extends HttpServlet {
    /** */
    private static ComputerDTOValidator computerDTOValidator = new ComputerDTOValidator();

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(ShowComputerListServlet.class);

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Envoi de la page affichant tous les ordinateurs.
     *
     * @param request requête sans paramètre.
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {

            List<ComputerDTO> computerList = computerDTOValidator.fetchList();

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
            request.setAttribute("computerList", computerList);
            int nbElements = computerDTOValidator.getNumberOfElements();
            Page p = new Page(nbElements, 0, nbElements);
            request.setAttribute("page", p);
            List<Integer> pagesToShow = new ArrayList<>();

            request.setAttribute("pageList", pagesToShow);
            rd.forward(request, response);

        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new ServletException(e);
        }
    }
}
