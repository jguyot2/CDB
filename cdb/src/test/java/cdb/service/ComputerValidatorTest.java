package cdb.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.persistence.ComputerSearcher;
import com.excilys.persistence.ComputerUpdater;
import com.excilys.persistence.PersistanceException;
import com.excilys.service.ComputerProblems;
import com.excilys.service.ComputerService;
import com.excilys.service.InvalidComputerException;

public class ComputerValidatorTest {

    private static final LocalDate[] localDates = { LocalDate.of(1985, 1, 1),
            LocalDate.of(1985, 1, 19), LocalDate.of(2000, 7, 19), LocalDate.of(2028, 8, 16) };

    private static final Company[] fakeCompanyList = { new Company("POUET", 1L),
            new Company("J'AIME L'OCA", 2L), new Company("Café Oz", 5L),
            new Company("Chocolatine", 10L) };
    private static final Computer[] fakeComputerList = {
            new Computer("PouetComputer", fakeCompanyList[0], null, null, 42),
            new Computer("Raclette", null, localDates[0], localDates[1], 12),
            new Computer("PIZZA", fakeCompanyList[1], localDates[0], null, 3),
            new Computer("PATES", null, null, null, 921),
            new Computer("RIZ", null, localDates[3], null, 245) };
    private static final Computer[] invalidComputerInstanceList = {
            new Computer("", null, null, null, 214), new Computer(null, null, null, null, 0),
            new Computer("nom", null, localDates[1], localDates[0], 12),
            new Computer("pouet", null, null, localDates[0], 12),
            new Computer("", null, null, localDates[0], 1),
            new Computer("", null, localDates[2], localDates[0], 12) };
    private ComputerSearcher computerSearcherMock = Mockito.mock(ComputerSearcher.class);
    private ComputerUpdater computerUpdaterMock = Mockito.mock(ComputerUpdater.class);

    private ComputerService validator;

    @Test
    public void createComputerTest() throws SQLException {
        Mockito.when(this.computerUpdaterMock.createComputer(Matchers.any(Computer.class)))
                .thenReturn(24L);

        for (Computer c : fakeComputerList) {
            try {
                Assert.assertEquals(24L, this.validator.addComputer(c));
            } catch (InvalidComputerException e) {
                System.out.println(c);
                e.printStackTrace();
                System.out.println(e.getProblems().get(0).getExplanation());
                Assert.fail();
            }
        }

        for (int i = 0; i < 2; ++i) {
            Computer c = invalidComputerInstanceList[i];
            try {
                this.validator.addComputer(c);
                Assert.fail();
            } catch (InvalidComputerException exn) {
                Assert.assertEquals(exn.getProblems().size(), 1);
                Assert.assertTrue(
                        exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            }
        }

        Computer c2 = invalidComputerInstanceList[2];
        try {
            this.validator.addComputer(c2);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }

        Computer c3 = invalidComputerInstanceList[3];
        try {
            this.validator.addComputer(c3);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c4 = invalidComputerInstanceList[4];
        try {
            this.validator.addComputer(c4);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c5 = invalidComputerInstanceList[5];
        try {
            this.validator.addComputer(c5);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }
    }

    @Test
    public void deleteComputerTest() throws SQLException {

        Mockito.when(this.computerUpdaterMock.deleteById(56)).thenReturn(1);

        Assert.assertEquals(1, this.validator.delete(56));
    }

    @Test
    public void fetchListTest() throws PersistanceException {
        List<Computer> computerList = new ArrayList<>();
        for (Computer c : fakeComputerList) {
            computerList.add(c);
        }

        Mockito.when(this.computerSearcherMock.fetchList()).thenReturn(computerList);

        List<Computer> l = this.validator.fetchList();
        Assert.assertEquals(l.size(), fakeComputerList.length);
        for (Computer comp : fakeComputerList) {
            Assert.assertTrue(l.contains(comp));
        }
    }

    @Test
    public void fetchWithOffsetTest() throws PersistanceException {
        Page p = new Page(fakeComputerList.length);

        List<Computer> computerList = new ArrayList<>();
        for (Computer c : fakeComputerList) {
            computerList.add(0, c);
        }

        Mockito.when(this.computerSearcherMock.fetchList(p)).thenReturn(computerList);

        Assert.assertEquals(this.validator.fetchList(p), computerList);
    }

    @Test
    public void findByIdTest() throws PersistanceException {
        Computer c = fakeComputerList[0];
        Optional<Computer> computerOpt = Optional.of(c);
        Optional<Computer> emptyComputer = Optional.empty();

        Mockito.when(this.computerSearcherMock.fetchById(c.getId())).thenReturn(computerOpt);
        Mockito.when(this.computerSearcherMock.fetchById(0)).thenReturn(emptyComputer);

        Assert.assertEquals(computerOpt, this.validator.findById(c.getId()));
        Assert.assertEquals(emptyComputer, this.validator.findById(0));

    }

    //// Partie mise à jour

    @Test
    public void getNumberOfElementsTest() throws PersistanceException {

        Mockito.when(this.computerSearcherMock.getNumberOfElements()).thenReturn(523);

        Assert.assertEquals(this.validator.getNumberOfElements(), 523);
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.validator = new ComputerService();
        this.validator.setComputerSearcher(this.computerSearcherMock, this.computerUpdaterMock);
    }

    @Test
    public void updateComputerTest() {

        Mockito.when(this.computerUpdaterMock.updateComputer(Matchers.any(Computer.class)))
                .thenReturn(1);

        for (Computer c : fakeComputerList) {
            try {
                Assert.assertEquals(1, this.validator.update(c));
            } catch (InvalidComputerException e) {
                Assert.fail();
            }
        }

        for (int i = 0; i < 2; ++i) {
            Computer c = invalidComputerInstanceList[i];
            try {
                this.validator.update(c);
                Assert.fail();
            } catch (InvalidComputerException exn) {
                Assert.assertEquals(exn.getProblems().size(), 1);
                Assert.assertTrue(
                        exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            }
        }

        Computer c2 = invalidComputerInstanceList[2];
        try {
            this.validator.update(c2);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }

        Computer c3 = invalidComputerInstanceList[3];
        try {
            this.validator.update(c3);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c4 = invalidComputerInstanceList[4];
        try {
            this.validator.update(c4);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c5 = invalidComputerInstanceList[5];
        try {
            this.validator.update(c5);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(exn.getProblems()
                    .contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }
    }

}
