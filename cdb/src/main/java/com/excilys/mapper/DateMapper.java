package com.excilys.mapper;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.service.CompanyValidator;

/**
 * Fonction de conversion de formats de date
 * @author jguyot2
 *
 */
public class DateMapper {
	
	private static final Logger logger = LoggerFactory.getLogger(DateMapper.class);
	
	public static Optional<LocalDate> sqlDateToLocalDate(java.sql.Date sqlDate) {
		if(sqlDate == null)
			return Optional.empty();
		return Optional.of(LocalDate.of(sqlDate.getYear()+1900, sqlDate.getMonth()+1, sqlDate.getDay()+1));
	}
	
	public static Optional<java.sql.Date> localDateToSqlDate(LocalDate localDate){
		if (localDate == null)
			return Optional.empty();
		else
			return Optional.of(new java.sql.Date(localDate.getYear()-1900, localDate.getMonthValue(), localDate.getDayOfMonth()));
	}
	
	public static LocalDate stringToLocalDate(String dateRepr) throws DateTimeParseException {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date = LocalDate.parse(dateRepr,dateFormatter);
		return date;
	}
}
