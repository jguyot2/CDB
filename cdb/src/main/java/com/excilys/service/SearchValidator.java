package com.excilys.service;

import java.util.List;
import java.util.Optional;

import com.excilys.model.Page;

public interface SearchValidator<T>{
	public List<T> fetchList();
	public List<T> fetchWithOffset(Page page);
	public Optional<T> findById(long id);
	public int getNumberOfElements();
}
