package com.excilys.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.adapters.ComputerAdapter;

@Controller
public class DeleteComputerController {
	private static final Logger LOG = LoggerFactory.getLogger(DeleteComputerController.class);

	/**
	 * Récupération du message à partir de la liste des ordis non supprimés.
	 *
	 * @param notDeletedId La liste des id des pc pas détruits
	 * @return Le message
	 */
	private static String getMessageFromNotDeletedId(@NonNull final List<Long> notDeletedId) {
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

	@Autowired
	private ComputerAdapter validator;

	/**
	 * Suppression des ordinateurs en paramètre.
	 *
	 * @param selectedIdentifiers la liste des identifiants des paramètres à
	 *                            détruire.
	 * @return
	 */
	@PostMapping("/page")
	public String computerDeletion(@RequestParam(name = "selection") final Long[] selectedIdentifiers) {

		List<Long> identifiers = Arrays.asList(selectedIdentifiers);
		List<Long> notDeletedId = deleteComputers(identifiers);
		String message = DeleteComputerController.getMessageFromNotDeletedId(notDeletedId);
		String urlParameterMessage = UrlEncoding.encode(message);
		return "redirect:/page?message=" + urlParameterMessage;
	}

	/**
	 * Suppression des oridnateurs en paramètre
	 *
	 * @param computersIdToDelete la liste des identifiants à détruire
	 * @return la liste des identifiants des ordinateurs qui n'ont pas été détruits
	 */ // TODO : màj pour détruire tous les ordinateurs à la fois. => changement du
		// comportement
	private List<Long> deleteComputers(@NonNull final List<Long> computersIdToDelete) {
		List<Long> notDeletedIds = new ArrayList<>();
		for (long id : computersIdToDelete) {
			int res = this.validator.delete(id);
			if (res == 0) {
				DeleteComputerController.LOG.info("Deletion : ID not found ( " + id + ")");
				notDeletedIds.add(id);
			} else if (res == -1) {
				DeleteComputerController.LOG
						.error("-1 retourné lors du deleteComputer => problème lors de l'exécution de la requête");
				notDeletedIds.add(id);
			}
		}
		return notDeletedIds;
	}
}
