package com.excilys.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.SortCriterion;
import com.excilys.model.sort.SortEntry;
import com.excilys.persistence.ComputerSearcher;
import com.excilys.persistence.ComputerUpdater;
import com.excilys.persistence.DaoException;

public class ComputerServiceTest {

    private static final LocalDate[] localDates = { LocalDate.of(1985, 1, 1), LocalDate.of(1985, 1, 19),
            LocalDate.of(2000, 7, 19), LocalDate.of(2028, 8, 16) };

    private static final Company[] fakeCompanyList = { new Company("POUET", 1L),
            new Company("J'AIME L'OCA", 2L),
            new Company("Café Oz", 5L), new Company("Chocolatine", 10L) };
    private static final Computer[] fakeComputerList = {
            Computer.getBuilder()
                    .setName("PouetComputer")
                    .setManufacturer(fakeCompanyList[0]).setId(42L).build(),

            Computer.getBuilder()
                    .setName("Raclette")
                    .setIntro(localDates[0])
                    .setDisco(localDates[1])
                    .setId(12L)
                    .build(),

            Computer.getBuilder().setName("PIZZA")
                    .setManufacturer(fakeCompanyList[1])
                    .setIntro(localDates[0])
                    .setId(3L)
                    .build(),

            Computer.getBuilder()
                    .setName("PATES")
                    .setId(921L).build(),

            Computer.getBuilder()
                    .setName("RIZ")
                    .setIntro(localDates[3])
                    .setId(245L)
                    .build()
    };

    private static final Computer[] invalidComputerInstanceList = {
            Computer.getBuilder().setName("").setId(214L).build(),
            Computer.getBuilder().setId(0L).build(),
            Computer.getBuilder()
                    .setName("nom").setIntro(localDates[1]).setDisco(localDates[0]).setId(12L).build(),
            Computer.getBuilder().setName("pouet").setDisco(localDates[0]).setId(12L).build(),
            Computer.getBuilder().setName("").setDisco(localDates[0]).setId(1L).build(),
            Computer.getBuilder().setName("").setIntro(localDates[2]).setDisco(localDates[0]).setId(12L)
                    .build()
    };

    private ComputerSearcher computerSearcherMock;
    private ComputerUpdater computerUpdaterMock;

    private ComputerValidator validator = new ComputerValidator();

    private ComputerService service;

    @Test // TODO refacto cette fonction de test
    public void createComputerTest() throws SQLException, DaoException {
        Mockito.when(this.computerUpdaterMock.createComputer(Matchers.any(Computer.class))).thenReturn(24L);
        System.out.println(this.service);
        System.out.println(fakeComputerList);
        for (Computer c : fakeComputerList) {
            try {
                Assert.assertEquals(24L, this.service.addComputer(c));
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
                this.service.addComputer(c);
                Assert.fail();
            } catch (InvalidComputerException exn) {
                Assert.assertEquals(exn.getProblems().size(), 1);
                Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            }
        }

        Computer c2 = invalidComputerInstanceList[2];
        try {
            this.service.addComputer(c2);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }

        Computer c3 = invalidComputerInstanceList[3];
        try {
            this.service.addComputer(c3);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(
                    exn.getProblems().contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c4 = invalidComputerInstanceList[4];
        try {
            this.service.addComputer(c4);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(
                    exn.getProblems().contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c5 = invalidComputerInstanceList[5];
        try {
            this.service.addComputer(c5);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }
    }

    @Test
    public void deleteComputerTest() throws SQLException, DaoException {
        Mockito.when(this.computerUpdaterMock.deleteById(56L)).thenReturn(1);
        Assert.assertEquals(1, this.service.delete(56L));
    }

    @Test
    public void deleteComputerDbExceptionTest() throws SQLException, DaoException {
        Mockito.when(this.computerUpdaterMock.deleteById(56L)).thenThrow(new DaoException());
        Assert.assertEquals(-1, this.service.delete(56L));
    }

    @Test
    public void fetchListTest() throws DaoException {
        List<Computer> computerList = new ArrayList<>();
        for (Computer c : fakeComputerList) {
            computerList.add(c);
        }
        Mockito.when(this.computerSearcherMock.fetchList()).thenReturn(computerList);
        List<Computer> l = this.service.fetchList();
        Assert.assertEquals(l.size(), fakeComputerList.length);
        for (Computer comp : fakeComputerList) {
            Assert.assertTrue(l.contains(comp));
        }
    }

    @Test
    public void fetchListDbExceptionTest() throws DaoException {
        Mockito.when(this.computerSearcherMock.fetchList()).thenThrow(new DaoException());
        List<Computer> l = this.service.fetchList();
        Assert.assertTrue(l.isEmpty());
    }

    @Test
    public void fetchWithOffsetTest() throws DaoException {
        Page p = new Page(fakeComputerList.length);

        List<Computer> computerList = new ArrayList<>();
        for (Computer c : fakeComputerList) {
            computerList.add(0, c);
        }

        Mockito.when(this.computerSearcherMock.fetchList(p)).thenReturn(computerList);

        Assert.assertEquals(this.service.fetchList(p), computerList);
    }

    @Test
    public void fetchWithOffsetDbExceptionTest() throws DaoException {
        Page p = new Page(fakeComputerList.length);
        Mockito.when(this.computerSearcherMock.fetchList(p)).thenThrow(new DaoException());
        Assert.assertTrue(this.service.fetchList(p).isEmpty());
    }

    @Test
    public void findByIdTest() throws DaoException {
        Computer c = fakeComputerList[0];
        Optional<Computer> computerOpt = Optional.of(c);
        Optional<Computer> emptyComputer = Optional.empty();

        Mockito.when(this.computerSearcherMock.fetchById(c.getId())).thenReturn(computerOpt);
        Mockito.when(this.computerSearcherMock.fetchById(0)).thenReturn(emptyComputer);

        Assert.assertEquals(computerOpt, this.service.findById(c.getId()));
        Assert.assertEquals(emptyComputer, this.service.findById(0));

    }

    @Test
    public void findByIdDbExceptionTest() throws DaoException {
        Computer c = fakeComputerList[0];
        Mockito.when(this.computerSearcherMock.fetchById(c.getId())).thenThrow(new DaoException());
        Assert.assertEquals(Optional.empty(), this.service.findById(c.getId()));
    }

    @Test
    public void getNumberOfElementsTest() throws DaoException {
        Mockito.when(this.computerSearcherMock.getNumberOfElements()).thenReturn(523);
        Assert.assertEquals(this.service.getNumberOfElements(), 523);
    }

    @Test
    public void getNumberOfElementsDbExceptionTest() throws DaoException {
        Mockito.when(this.computerSearcherMock.getNumberOfElements()).thenThrow(new DaoException());
        Assert.assertEquals(this.service.getNumberOfElements(), -1);
    }

    @Before
    public void init() {
        this.computerUpdaterMock = Mockito.mock(ComputerUpdater.class);
        this.computerSearcherMock = Mockito.mock(ComputerSearcher.class);
        this.service = new ComputerService();
        this.service.setComputerSearcher(this.computerSearcherMock, this.computerUpdaterMock);
        this.service.setValidator(this.validator);

    }

    @Test // TODO refacto de cette fonction
    public void updateComputerTest() {
        try {
            Mockito.when(this.computerUpdaterMock.updateComputer(Matchers.any(Computer.class))).thenReturn(1);
        } catch (DaoException e) {
            Assert.fail();
        }
        for (Computer c : fakeComputerList) {
            try {
                Assert.assertEquals(1, this.service.update(c));
            } catch (InvalidComputerException e) {
                Assert.fail();
            }
        }

        for (int i = 0; i < 2; ++i) {
            Computer c = invalidComputerInstanceList[i];
            try {
                this.service.update(c);
                Assert.fail();
            } catch (InvalidComputerException exn) {
                Assert.assertEquals(exn.getProblems().size(), 1);
                Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            }
        }

        Computer c2 = invalidComputerInstanceList[2];
        try {
            this.service.update(c2);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }

        Computer c3 = invalidComputerInstanceList[3];
        try {
            this.service.update(c3);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 1);
            Assert.assertTrue(
                    exn.getProblems().contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c4 = invalidComputerInstanceList[4];
        try {
            this.service.update(c4);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(
                    exn.getProblems().contains(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
        }
        Computer c5 = invalidComputerInstanceList[5];
        try {
            this.service.update(c5);
            Assert.fail();
        } catch (InvalidComputerException exn) {
            Assert.assertEquals(exn.getProblems().size(), 2);
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_NAME));
            Assert.assertTrue(exn.getProblems().contains(ComputerProblems.INVALID_DISCONTINUATION_DATE));
        }
    }

    @Test
    public void fetchListWithSortAndSearchTest() throws DaoException, DuplicatedSortEntriesException {
        Mockito.when(this.computerSearcherMock.fetchList(Matchers.any(Page.class), Matchers.any(String.class),
                Matchers.any()))
                .thenReturn(Arrays.asList(fakeComputerList));
        Assert.assertEquals(Arrays.asList(fakeComputerList),
                this.service.fetchList(new Page(), "", Arrays.asList()));
    }

    @Test
    public void fetchListWithSortAndSearchDbExceptionTest()
            throws DaoException, DuplicatedSortEntriesException {

        Mockito.when(
                this.computerSearcherMock.fetchList(Matchers.any(Page.class), Matchers.any(), Matchers.any()))
                .thenThrow(new DaoException());
        Assert.assertEquals(Arrays.asList(), this.service.fetchList(new Page(), "", Arrays.asList()));

    }

    @Test
    public void fetchListWithDuplicatedEntriesTest() throws DaoException {
        Mockito.when(this.computerSearcherMock.fetchList(Matchers.any(Page.class), Matchers.any(String.class),
                Matchers.any()))
                .thenReturn(Arrays.asList(fakeComputerList));
        SortEntry sortParam1 = new SortEntry(SortCriterion.COMPUTER_NAME, true);
        SortEntry sortParam2 = new SortEntry(SortCriterion.COMPUTER_NAME, false);
        try {
            this.service.fetchList(new Page(), "", Arrays.asList(sortParam1, sortParam2));
            Assert.fail();
        } catch (DuplicatedSortEntriesException e) {

        }
    }

    @Test
    public void addComputerDbExceptionTest() throws DaoException, InvalidComputerException {
        Mockito.when(this.computerUpdaterMock.createComputer(Matchers.any())).thenThrow(new DaoException());
        Assert.assertEquals(0, this.service.addComputer(fakeComputerList[0]));
    }

    @Test
    public void testFetchListPageSortEntriesDbExceptionTest()
            throws DaoException, DuplicatedSortEntriesException {
        Mockito.when(this.computerSearcherMock.fetchList(Matchers.any(Page.class), Matchers.anyList()))
                .thenThrow(new DaoException());
        Assert.assertEquals(Arrays.asList(), this.service.fetchList(new Page(), Arrays.asList()));
    }

    @Test
    public void testFetchListPageSortEntriesTest()
            throws DaoException, DuplicatedSortEntriesException {
        Mockito.when(this.computerSearcherMock.fetchList(Matchers.any(Page.class), Matchers.anyList()))
                .thenReturn(Arrays.asList(fakeComputerList));
        Assert.assertEquals(Arrays.asList(fakeComputerList),
                this.service.fetchList(new Page(), Arrays.asList()));
    }

    @Test
    public void testFetchListPageSortEntriesDuplicatedTest()
            throws DaoException, DuplicatedSortEntriesException {

        SortEntry s1 = new SortEntry(SortCriterion.COMPUTER_NAME, true);
        SortEntry s2 = new SortEntry(SortCriterion.COMPUTER_NAME, true);
        Mockito.when(this.computerSearcherMock.fetchList(Matchers.any(Page.class), Matchers.anyList()))
                .thenReturn(Arrays.asList());
        try {
            this.service.fetchList(new Page(), Arrays.asList(s1, s2));
            Assert.fail();
        } catch (DuplicatedSortEntriesException e) {

        }
    }
}
