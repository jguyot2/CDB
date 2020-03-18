package com.excilys.mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fonction de conversion de formats de date
 * 
 * @author jguyot2
 *
 */
public class DateMapper {
	private static final Logger logger = LoggerFactory.getLogger(DateMapper.class);

	@SuppressWarnings("deprecation")
	public static Optional<LocalDate> sqlDateToLocalDate(java.sql.Date sqlDate) {
		if (sqlDate == null)
			return Optional.empty();
		return Optional.of(LocalDate.of(sqlDate.getYear() + 1900, sqlDate.getMonth() + 1, sqlDate.getDate()));
	}

	public static Optional<Date> localDateToSqlDate(LocalDate localDate) {
		if (localDate == null)
			return Optional.empty();
		else
			return Optional.of(Date.valueOf(localDate));
	
	}

	/**
	 * Convertit une date au format 'jj/mm/aaaa' en une instance de LocalDate
	 * 
	 * @param dateRepr une chaine de caractères correspondant à une date au format
	 *                 'jj/mm/aaaa'
	 * @return une instance de LocalDate correspondante
	 * @throws DateTimeParseException si la date n'est pas au bon format
	 */
	public static Optional<LocalDate> stringToLocalDate(String dateRepr) {
		if(dateRepr == null)
			return Optional.empty();
		logger.info("string to date appellée : str=" + dateRepr);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		try {
			LocalDate date = LocalDate.parse(dateRepr, dateFormatter);
			return Optional.of(date);
		} catch (DateTimeParseException e) {
			logger.debug("Date en entrée invalide");
			return Optional.empty();
		}
	}
}
