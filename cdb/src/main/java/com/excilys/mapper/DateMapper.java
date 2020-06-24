package com.excilys.mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fonctions de conversion de formats de date, notamment entre LocalDate et
 * java.sql.Date .
 *
 * @author jguyot2
 */
public final class DateMapper {
    /**
     * Pattern représentant la manière dont les dates sont écrites.
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(DateMapper.class);

    /**
     * Fonction de conversion d'une instance de LocalDate vers une instance de
     * java.sql.Date équivalente.
     *
     * @param localDate la date à laquelle appliquer la conversion
     * @return une instance de java.sql.date correspondante
     */
    public static Optional<Date> localDateToSqlDate(final LocalDate localDate) {
        LOG.trace("Conversion de date locale vers sqlDate. Date = " + localDate);
        if (localDate == null) {
            return Optional.empty();
        } else {
            return Optional.of(Date.valueOf(localDate));
        }
    }

    /**
     * Conversion d'une date en chaîne de caractères au format donné dans
     * DATE_PATTERN
     *
     * @param ld la date à convertir
     * @return un optional contenant chaîne correspondant à la date en paramètre au
     *         format correspondant à DATE_PATTERN, ou Optional.empty() si le
     *         paramètre est nul
     */
    public static Optional<String> localDateToString(final LocalDate ld) {
        LOG.trace("Conversion de localDate vers Str. localDate = " + ld);
        if (ld == null) {
            return Optional.empty();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return Optional.of(ld.format(df));
    }

    /**
     * Fonction de conversion d'une instance de java.sql.Date vers une instance de
     * LocalDate.
     *
     * @param sqlDate
     *
     * @return un Optional<LocalDate> contenant la date passée en param, ou vide si
     *         le param est nul.
     */
    @SuppressWarnings("deprecation")
    public static Optional<LocalDate> sqlDateToLocalDate(final Date sqlDate) {
        LOG.trace("Conversion de java.sql.Date vers localDate. Param=" + sqlDate);
        if (sqlDate == null) {
            return Optional.empty();
        }
        return Optional.of(LocalDate.of(sqlDate.getYear() + 1900, sqlDate.getMonth() + 1, sqlDate.getDate()));
    }

    /**
     * Convertit une date au format donné dans DATE_PATTERN en une instance de
     * LocalDate.
     *
     * @param dateRepr une chaine de caractères correspondant à une date au format
     *                 correspondant à <DATE_PATTERN>
     *
     * @return un optional contenant la date correspondante à la chaîne en param, ou
     *         vide dans le cas où le paramètre est nul ou ne correspondant pasau
     *         format attendu
     *
     * @throws DateTimeParseException si la date n'est pas au bon format
     */
    public static Optional<LocalDate> stringToLocalDate(final String dateRepr) {
        LOG.trace("Conversion de str vers localDate. str =" + dateRepr);
        if (dateRepr == null) {
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        try {
            LocalDate date = LocalDate.parse(dateRepr, formatter);
            return Optional.of(date);
        } catch (DateTimeParseException e) {
            LOG.debug("Date en entrée invalide");
            return Optional.empty();
        }
    }

    private DateMapper() {
    }
}
