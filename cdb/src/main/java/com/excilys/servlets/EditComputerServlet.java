package com.excilys.servlets;

import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.ComputerDTO;
import com.excilys.service.ComputerDTOValidator;
import com.excilys.service.NotImplementedException;

@WebServlet("/edit")
public class EditComputerServlet extends HttpServlet {
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(EditComputerServlet.class);

    private static final long serialVersionUID = 1L;

    /** */
    private ComputerDTOValidator validator = new ComputerDTOValidator();

    /**
     * Affiche la page permettant la modification d'un ordinateur de la base (ou l'affichage d'une
     * erreur si l'entrée est invalide).
     * @param request requête get contenant le paramètre (nécessaire) "id" représentant
     * l'identifiant de l'ordinateur.
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        String strId = request.getParameter("id");
        long id = Long.parseLong(strId);
        Optional<ComputerDTO> computer = validator.findById(id);
        throw new NotImplementedException(); // TODO mais pas avant le sprint 3
    }

    /**
     *
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        // TODO mais pas avant le sprint 3
    }
}
