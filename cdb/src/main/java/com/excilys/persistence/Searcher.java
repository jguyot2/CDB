package com.excilys.persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.model.Page;

public interface Searcher<T> {
    /**
     *
     * @param id
     * @return l'objet associé à l'identifient id en paramètre
     * @throws SQLException
     */
    Optional<T> fetchById(long id) throws SQLException;

    /**
     * Récupération de la liste des éléments correspondant à la table.
     *
     * @return la liste des instances correspondant aux lignes de la table.
     * @throws SQLException
     */
    List<T> fetchList() throws SQLException;

    /**
     *
     * @param p
     * @return la liste des objets correspondant à la page en paramètre
     * @throws SQLException
     */
    List<T> fetchWithOffset(Page p) throws SQLException;

    /**
     * @return le nombre d'éléments contenu dans la table
     * @throws SQLException
     */
    int getNumberOfElements() throws SQLException;
}
