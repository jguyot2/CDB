package com.excilys.service;

import java.util.List;
import java.util.Optional;

import com.excilys.model.Page;

public interface SearchValidator<T> {
    /**
     * Récupération de la liste des éléments.
     *
     * @return La liste des éléments présents dans la base
     */
    List<T> fetchList();

    /**
     * Récupération des éléments présents sur la page en paramètre.
     *
     * @param page
     * @return la liste des éléments contenu dans la page en paramètre
     */
    List<T> fetchWithOffset(Page page);

    /**
     * Recherche d'un élément par identifiant.
     *
     * @param id
     * @return Un optional contenant une valeur si la valeur a été retrouvée,
     *         Optional.empty() sinon
     */
    Optional<T> findById(long id);

    /**
     * @return le nombre d'éléments dans la base de données.
     */
    int getNumberOfElements();
}
