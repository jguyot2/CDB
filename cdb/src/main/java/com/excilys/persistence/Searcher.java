package com.excilys.persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.excilys.model.Page;

/**
 * Interface forçant à utiliser les mêmes noms de méthodes pour les diverses
 * classes de recherche (i.e qui recherchent des objets/informations dans la
 * base)
 *
 * @author jguyot2
 *
 * @param <T> Le type d'éléments concernés par la recherche (Company ou Computer)
 */
interface Searcher<T> {
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
     * Récupération d'une liste d'éléments «dans» une page en param
     *
     * @param p
     * @return la liste des objets correspondant à la page en paramètre
     * @throws SQLException
     */
    List<T> fetchList(Page p) throws SQLException;

    /**
     * @return le nombre d'éléments contenu dans la table
     * @throws SQLException
     */
    int getNumberOfElements() throws SQLException;
}
