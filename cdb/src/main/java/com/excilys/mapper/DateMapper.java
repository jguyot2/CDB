package com.excilys.mapper;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fonctions de conversion de formats de date, notamment entre LocalDate
 * et java.sql.Date
 *
 * @author jguyot2
 */
public final class DateMapper {
    /**
     *
     */
    private static final Logger LOG = LoggerFactory.getLogger(DateMapper.class);

    /**
     * Fonction de conversion d'une instance de LocalDate vers une
     * instance de java.sql.Date équivalente.
     * @param localDate la date à laquelle appliquer la conversion
     * @return une instance de java.sql.date correspondante
     */
    public static Optional<Date> localDateToSqlDate(final LocalDate localDate) {
        LOG.info("Conversion de date locale vers sqlDate. Date = " + localDate);
        if (localDate == null) {
            return Optional.empty();
        } else {
            return Optional.of(Date.valueOf(localDate));
        }
    }

    /**
     * Conversion d'une date en chaîne de caractères au format JJ/MM/YYYY.
     * @param ld la date à convertir
     * @return une instance de String correspondant à la date en paramètre
     * au format JJ/MM/AAAA
     */
    public static Optional<String> localDateToString(final LocalDate ld) {
        LOG.info("Conversion de localDate vers Str. localDate = " + ld);
        if (ld == null) {
            return Optional.empty();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return Optional.of(ld.format(df));
    }

    /**
     * Fonction de conversion d'une instance de
     * java.sql.Date
     * vers une instance de
     * LocalDate.
     *
     * @param sqlDate
     *
     * @return La date LocalDate correspondant à la
     *         date passée en param
     */
    @SuppressWarnings("deprecation")
    public static Optional<LocalDate> sqlDateToLocalDate(final Date sqlDate) {
        LOG.info("Conversion de java.sql.Date vers localDate. Param=" + sqlDate);
        if (sqlDate == null) {
            return Optional.empty();
        }
        //TODO : vérifier cet appel.
        return Optional.of(LocalDate.of(sqlDate.getYear() + 1900,
            sqlDate.getMonth() + 1, sqlDate.getDate()));
    }

    /**
     * Convertit une date au format 'jj/mm/aaaa' en
     * une instance de LocalDate.
     *
     * @param dateRepr une chaine de caractères
     *                 correspondant à une date au
     *                 format
     *                 'jj/mm/aaaa'
     *
     * @return une instance de LocalDate
     *         correspondante
     *
     * @throws DateTimeParseException si la date n'est
     *                                pas au bon
     *                                format
     */
    public static Optional<LocalDate> stringToLocalDate(final String dateRepr) {
        LOG.info("Conversion de str vers localDate. str =" + dateRepr);
        if (dateRepr == null) {
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
