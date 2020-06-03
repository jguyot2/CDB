package com.excilys.servlets;

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
import com.excilys.service.ComputerDTOValidator;

// TODO : Pê dégager cette classe
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
     * @param request
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        try {
            List<ComputerDTO> computerList = computerDTOValidator.fetchList();
            // TODO : modifier pour que ça affiche tous les ordis
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
            request.setAttribute("computerList", computerList);
            if (computerList == null) {
                throw new AssertionError();
            }
            assert (rd != null);
            rd.forward(request, response);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ServletException(e);
        }
    }
}
