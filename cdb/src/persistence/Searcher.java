package persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import model.Page;

public interface Searcher<T> {
	/**
	 * Récupération de la liste des éléments correspondant à la table
	 * @return la liste des instances correspondant aux lignes de la table.
	 * @throws SQLException 
	 */
	public List<T> fetchList() throws SQLException;

	public List<T> fetchWithOffset(Page p) throws SQLException;

	public Optional<T> fetchById(long id) throws SQLException;

	public int getNumberOfElements() throws SQLException;
}
