package com.excilys.servlets;

import java.util.Optional;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.model.ComputerDTO;
import com.excilys.service.ComputerDTOValidator;
import com.excilys.service.NotImplementedException;

@WebServlet("/edit")
public class EditComputerServlet extends HttpServlet {
    ComputerDTOValidator validator = new ComputerDTOValidator();
    private static final long serialVersionUID = 1L;

    /**
     * Affiche la page permettant la modification d'un ordinateur de la base (ou l'affichage d'une
     * erreur si l'entré est invalide)
     * @param request requête get contenant le paramètre (nécessaire) "id" représentant
     * l'identifiant de l'ordinateur.
     * @param response
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String strId = request.getParameter("id");
        long id = Long.parseLong(strId);
        Optional<ComputerDTO> computer = validator.findById(id);
        throw new NotImplementedException(); // A faire au sprint 3
    }

    /**
     * 
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
