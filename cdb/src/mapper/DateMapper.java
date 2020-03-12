package mapper;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class DateMapper {

	/**
	 * Fonction de conversion d'une instance de java.util.date vers une instance de
	 * java.sql.Date
	 * 
	 * @param date La date à convertir
	 * @return La date correspondant au paramètre, mais qui est une instance de
	 *         java.sql.Date
	 */
	public static Optional<java.sql.Date> utilDateToSqlDate(Date date) {
		if (date == null)
			return Optional.empty();
		return Optional.of(new java.sql.Date(date.getTime()));
	}

	// Format : DD/MM/YYYY
	/**
	 * Fonction de conversion d'une chaîne de caractères au format DD/MM/YYYY vers
	 * une instance de Date
	 * 
	 * @param dateRepr la représentation par une chaine de caractères sous la forme
	 *                 DD/MM/YYYY (ou JJ/MM/AAAA en français.
	 * @return une instance de Date correspondant à la chaîne de caractères donnée
	 *         en paramètre
	 * @throws IllegalArgumentException si la chaîne de caractères est au mauvais
	 *                                  format.
	 */
	public static Date stringToUtilDate(String dateRepr) throws IllegalArgumentException {
		if (dateRepr == null || dateRepr.trim().isEmpty())
			throw new IllegalArgumentException("La chaîne est vide");
		String[] dateArray = dateRepr.trim().split("/");
		if (dateArray.length != 3)
			throw new IllegalArgumentException("pas le bon nombre de / ou de champs");

		int day = Integer.parseInt(dateArray[0], 10);
		int month = Integer.parseInt(dateArray[1], 10);
		int year = Integer.parseInt(dateArray[2].trim(), 10);

		return new Date(year - 1900, month, day);

	}
}
