package com.excilys.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;


/**
 * Fonctions de conversion des dates.
 *
 * @author jguyot2
 */
public final class DateMapper {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final Logger LOG = LoggerFactory.getLogger(DateMapper.class);


    /**
     * Conversion d'une date en chaîne de caractères au format donné dans DATE_PATTERN
     *
     * @param ld la date à convertir
     *
     * @return un optional contenant chaîne correspondant à la date en paramètre au format correspondant à
     *         DATE_PATTERN, ou Optional.empty() si le paramètre est nul
     */
    public static Optional<String> localDateToString(final LocalDate ld) {
        DateMapper.LOG.trace("Conversion de localDate vers Str. localDate = " + ld);
        if (ld == null) {
            return Optional.empty();
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DateMapper.DATE_PATTERN);
        return Optional.of(ld.format(df));
    }

    /**
     * Convertit une date au format donné dans DATE_PATTERN en une instance de LocalDate.
     *
     * @param dateRepr une chaine de caractères correspondant à une date au format correspondant à
     *                 <DATE_PATTERN>
     *
     * @return un optional contenant la date correspondante à la chaîne en param, ou vide dans le cas où le
     *         paramètre est nul ou ne correspondant pasau format attendu
     *
     */
    public static Optional<LocalDate> stringToLocalDate(@Nullable final String dateRepr) {
        DateMapper.LOG.trace("Conversion de str vers localDate. str =" + dateRepr);
        if (dateRepr == null) {
            return Optional.empty();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateMapper.DATE_PATTERN);
        try {
            LocalDate date = LocalDate.parse(dateRepr, formatter);
            return Optional.of(date);
        } catch (DateTimeParseException e) {
            DateMapper.LOG.debug("Date en entrée invalide");
            return Optional.empty();
        }
    }

    private DateMapper() {
    }
}
