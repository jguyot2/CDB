package com.excilys.mapper;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.excilys.model.Company;
import com.excilys.model.CompanyDto;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDto;

public class ComputerMapperTest {
    private static final Company[] fakeCompanyList = { new Company("POUET", 1L),
            new Company("J'AIME L'OCA", 2L), new Company("Café Oz", 5L),
            new Company("Chocolatine", 10L) };

    private static final LocalDate[] localDates = { LocalDate.of(1985, 1, 1),
            LocalDate.of(1985, 1, 19), LocalDate.of(2000, 7, 19), LocalDate.of(2048, 8, 16) };
    private static final Computer[] fakeComputerList = {
            new Computer("PouetComputer", fakeCompanyList[0], null, null, 42),
            new Computer("Raclette", null, localDates[0], localDates[1], 12),
            new Computer("PIZZA", fakeCompanyList[1], localDates[0], null, 3),
            new Computer("PATES", null, null, null, 921),
            new Computer("RIZ", null, localDates[3], null, 245) };

    private static final CompanyDto[] CompanyDtos = { new CompanyDto("POUET", 1L),
            new CompanyDto("J'AIME L'OCA", 2L), new CompanyDto("Café Oz", 5L),
            new CompanyDto("Chocolatine", 10L) };

    private static final String[] strDates = new String[localDates.length];
    static {
        for (int i = 0; i < localDates.length; ++i) {
            strDates[i] = DateMapper.localDateToString(localDates[i]).get();
        }
    }

    private static final ComputerDto[] ComputerDtoList = {
            new ComputerDto("PouetComputer", "42", CompanyDtos[0], null, null),
            new ComputerDto("Raclette", "12", null, strDates[0], strDates[1]),
            new ComputerDto("PIZZA", "3", CompanyDtos[1], strDates[0], null),
            new ComputerDto("PATES", "921", null, null, null),
            new ComputerDto("RIZ", "245", null, strDates[3], null) };

    @Before
    public void init() {

    }

    @Test
    public void computerToDTOTest() {
        for (int i = 0; i < fakeComputerList.length; ++i) {
            assertEquals(ComputerMapper.computerToDTO(fakeComputerList[i]).get(),
                    ComputerDtoList[i]);
        }
    }

    @Test
    public void dtoToComputerTest() {
        for (int i = 0; i < fakeComputerList.length; ++i) {
            assertEquals(ComputerMapper.computerDTOToComputer(ComputerDtoList[i]).get(),
                    fakeComputerList[i]);
        }
    }

    @Test
    public void doubleCallTest() {
        for (int i = 0; i < fakeComputerList.length; ++i) {
            assertEquals(ComputerMapper
                    .computerToDTO(ComputerMapper.computerDTOToComputer(ComputerDtoList[i]).get())
                    .get(), ComputerDtoList[i]);
            assertEquals(
                    ComputerMapper.computerDTOToComputer(
                            ComputerMapper.computerToDTO(fakeComputerList[i]).get()).get(),
                    fakeComputerList[i]);
        }
    }
}
