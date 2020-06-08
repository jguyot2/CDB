package cdb.mapper;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import com.excilys.mapper.ComputerMapper;
import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDTO;

public class ComputerMapperTest {
    private static final Company[] fakeCompanyList = { new Company("POUET", 1),
            new Company("J'AIME L'OCA", 2), new Company("Café Oz", 5), new Company("Chocolatine", 10) };

    private static final LocalDate[] localDates = { LocalDate.of(1985, 1, 1), LocalDate.of(1985, 1, 19),
            LocalDate.of(2000, 7, 19), LocalDate.of(2048, 8, 16) };
    private static final Computer[] fakeComputerList = {
            new Computer("PouetComputer", fakeCompanyList[0], null, null, 42),
            new Computer("Raclette", null, localDates[0], localDates[1], 12),
            new Computer("PIZZA", fakeCompanyList[1], localDates[0], null, 3),
            new Computer("PATES", null, null, null, 921),
            new Computer("RIZ", null, localDates[3], null, 245) };

    private static final CompanyDTO[] companyDTOs = { new CompanyDTO("POUET", "1"),
            new CompanyDTO("J'AIME L'OCA", "2"), new CompanyDTO("Café Oz", "5"),
            new CompanyDTO("Chocolatine", "10") };

    private static final String[] strDates = new String[localDates.length];
    static {
        for (int i = 0; i < localDates.length; ++i) {
            strDates[i] = DateMapper.localDateToString(localDates[i]).get();
        }
    }

    private static final ComputerDTO[] computerDTOList = {
            new ComputerDTO("PouetComputer", "42", companyDTOs[0], null, null),
            new ComputerDTO("Raclette", "12", null, strDates[0], strDates[1]),
            new ComputerDTO("PIZZA", "3", companyDTOs[1], strDates[0], null),
            new ComputerDTO("PATES", "921", null, null, null),
            new ComputerDTO("RIZ", "245", null, strDates[3], null) };

    @Before
    public void init() {

    }

    @Test
    public void computerToDTOTest() {
        for (int i = 0; i < fakeComputerList.length; ++i) {
            assertEquals(ComputerMapper.computerToDTO(fakeComputerList[i]).get(), computerDTOList[i]);
        }
    }

    @Test
    public void dtoToComputerTest() {
        for (int i = 0; i < fakeComputerList.length; ++i) {
            assertEquals(ComputerMapper
                .computerDTOToComputer(computerDTOList[i]).get(), fakeComputerList[i]);
        }
    }

    @Test
    public void doubleCallTest() {
        for (int i = 0; i < fakeComputerList.length; ++i) {
            assertEquals(ComputerMapper
                    .computerToDTO(ComputerMapper.computerDTOToComputer(computerDTOList[i]).get()).get(),
                    computerDTOList[i]);
            assertEquals(ComputerMapper
                    .computerDTOToComputer(ComputerMapper.computerToDTO(fakeComputerList[i]).get()).get(),
                    fakeComputerList[i]);
        }
    }
}
