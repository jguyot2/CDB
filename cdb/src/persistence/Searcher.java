package persistence;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import model.Pagination;

public interface Searcher<T> {
	public List<T> fetchList() throws SQLException;

	public List<T> fetchWithOffset(Pagination p) throws SQLException;

	public Optional<T> fetchById(long id) throws SQLException;

	public int getNumberOfElements() throws SQLException;
}
