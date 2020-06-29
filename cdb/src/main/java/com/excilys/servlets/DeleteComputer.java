package com.excilys.servlets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.service.ComputerDTOValidator;

@Controller
public class DeleteComputer {
    private static final String CHARSET = "UTF-8";
    private static final Logger LOG = LoggerFactory.getLogger(DeleteComputer.class);
    @Autowired
    private ComputerDTOValidator validator;

    @PostMapping("/page")
    public String computerDeletion(
            @RequestParam(name = "selection") final Long[] selectedIdentifiers) {

        List<Long> identifiers = Arrays.asList(selectedIdentifiers);
        List<Long> notDeletedId = deleteComputers(identifiers);
        String message = getMessageFromNotDeletedId(notDeletedId);
        String urlParameterMessage;
        try {
            urlParameterMessage = URLEncoder.encode(message, CHARSET);
        } catch (UnsupportedEncodingException e) {
            urlParameterMessage = URLEncoder.encode(message);
        }
        // TODO : pê dégager ça
        return "redirect:/page?message=" + urlParameterMessage;
    }

    private List<Long> deleteComputers(final List<Long> computersIdToDelete) {
        List<Long> notDeletedIds = new ArrayList<>();
        for (long id : computersIdToDelete) {
            int res = this.validator.delete(id);
            if (res == 0) {
                LOG.info("Deletion : ID not found ( " + id + ")");
                notDeletedIds.add(id);
            } else if (res == -1) {
                LOG.error(
                        "-1 retourné lors du deleteComputer => problème lors de l'exécution de la requête");
                notDeletedIds.add(id);
            }
        }
        return notDeletedIds;
    }

    public String getMessageFromNotDeletedId(final List<Long> notDeletedId) {
        String message;
        if (notDeletedId.isEmpty()) {
            message = "Les ordinateurs ont été supprimés de la base";
        } else {
            message = "Les ordinateurs avec les identifiants suivants dans la base n'ont pas été supprimés : ";
            for (Long i : notDeletedId) {
                message += " " + i;
            }
        }
        return message;
    }
}
