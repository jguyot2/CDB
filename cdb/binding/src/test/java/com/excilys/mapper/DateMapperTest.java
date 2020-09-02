package com.excilys.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;


public class DateMapperTest {

    private static final LocalDate[] localDates = { LocalDate.of(1981, 5, 11), LocalDate.of(2021, 2, 6),
            LocalDate.of(2020, 2, 29) };
    private static final Date[] sqlDates = { Date.valueOf(localDates[0]), Date.valueOf(localDates[1]),
            Date.valueOf(localDates[2]) };
    private static final String[] strDates = { "1981-05-11", "2021-02-06", "2020-02-29" };


    @Test
    public void stringToLocalDateTest() {
        assertEquals(Optional.of(localDates[0]), DateMapper.stringToLocalDate(strDates[0]));
        assertEquals(Optional.of(localDates[1]), DateMapper.stringToLocalDate(strDates[1]));
        assertEquals(Optional.of(localDates[2]), DateMapper.stringToLocalDate(strDates[2]));

        assertEquals(Optional.empty(), DateMapper.stringToLocalDate(null));
        assertEquals(Optional.empty(), DateMapper.stringToLocalDate(""));
        assertEquals(Optional.empty(), DateMapper.stringToLocalDate("Je suis un poulet"));
        assertEquals(Optional.empty(), DateMapper.stringToLocalDate("1/2/3/2"));
        assertEquals(Optional.empty(), DateMapper.stringToLocalDate("1984/3/12"));
    }
}
