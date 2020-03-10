package mapper;

import java.time.LocalDate;
import java.util.Date;

public class DateMapper {

	/**
	 * Fonction de conversion d'une instance de java.util.date vers une instance de java.sql.Date
	 * @param date La date à convertir
	 * @return La date correspondant au paramètre, mais qui est une instance de java.sql.Date
	 */
	public static java.sql.Date utilDateToSqlDate(Date date) {
		if(date == null)
			return null;
		return new java.sql.Date(date.getTime());
	}

	// Format : DD/MM/YYYY
	public static Date stringToUtilDate(String dateRepr) throws IllegalArgumentException {
		if(dateRepr == null || dateRepr.trim().isEmpty())
			throw new IllegalArgumentException("demande de date à partir d'une chaîne vide");
		String[] dateArray = dateRepr.split("/");
		if(dateArray.length != 3)
			throw new IllegalArgumentException("pas le bon nombre de / ou de champs");
		
		int day = Integer.parseInt(dateArray[0], 10);
		int month = Integer.parseInt(dateArray[1], 10);
		int year = Integer.parseInt(dateArray[2].trim(), 10);
		
		return new Date(year-1900, month, day);
		
	}
}
