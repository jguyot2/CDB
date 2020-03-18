package cdb.mapper;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Optional;
import java.sql.Date;
import org.junit.Test;

import com.excilys.mapper.DateMapper;

public class DateMapperTest {
	private static final LocalDate[] localDates = { LocalDate.of(1981, 5, 11), LocalDate.of(2021, 2, 6),
			LocalDate.of(2020, 2, 29) };
	private static final Date[] sqlDates = {Date.valueOf(localDates[0]), Date.valueOf(localDates[1]), Date.valueOf(localDates[2]) };
	private static final String[] strDates = {"11/05/1981", "06/02/2021", "29/02/2020"};
	
	@Test
	public void localDateToSqlDateTest() {
		assertEquals(Optional.empty(), DateMapper.localDateToSqlDate(null));
		assertEquals(Optional.of(sqlDates[0]), DateMapper.localDateToSqlDate(localDates[0]));
		assertEquals(Optional.of(sqlDates[1]), DateMapper.localDateToSqlDate(localDates[1]));
		assertEquals(Optional.of(sqlDates[2]), DateMapper.localDateToSqlDate(localDates[2]));
	}

	@Test
	public void sqlDateToLocalDateTest() {
		assertEquals(Optional.empty(), DateMapper.sqlDateToLocalDate(null));
		assertEquals(Optional.of(localDates[0]), DateMapper.sqlDateToLocalDate(sqlDates[0]));
		assertEquals(Optional.of(localDates[1]), DateMapper.sqlDateToLocalDate(sqlDates[1]));
		assertEquals(Optional.of(localDates[2]), DateMapper.sqlDateToLocalDate(sqlDates[2]));
	}

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
