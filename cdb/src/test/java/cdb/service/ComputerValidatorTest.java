package cdb.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.persistence.ComputerSearcher;
import com.excilys.persistence.ComputerUpdater;
import com.excilys.service.ComputerInstanceProblems;
import com.excilys.service.ComputerValidator;
import com.excilys.service.InvalidComputerInstanceException;

public class ComputerValidatorTest {

	private ComputerUpdater computerUpdaterMock = Mockito.mock(ComputerUpdater.class);
	private ComputerSearcher computerSearcherMock = Mockito.mock(ComputerSearcher.class);
	private ComputerValidator validator;

	private static final Company[] fakeCompanyList = { new Company("POUET", 1), new Company("J'AIME L'OCA", 2),
			new Company("Café Oz", 5), new Company("Chocolatine", 10) };
	
	private static final LocalDate[] localDates = { LocalDate.of(1985, 1, 1), LocalDate.of(1985, 1, 19),
			LocalDate.of(2000, 7, 19), LocalDate.of(2048, 8, 16) };
	private static final Computer[] fakeComputerList = {
			new Computer("PouetComputer", fakeCompanyList[0], null, null, 42),
			new Computer("Raclette", null, localDates[0], localDates[1], 12),
			new Computer("PIZZA", fakeCompanyList[1], localDates[0], null, 3),
			new Computer("PATES", null, null, null, 921), new Computer("RIZ", null, localDates[3], null, 245) };

	private static final Computer[] invalidComputerInstanceList = { new Computer("", null, null, null, 214),
			new Computer(null, null, null, null, 0), new Computer("nom", null, localDates[1], localDates[0], 12),
			new Computer("pouet", null, null, localDates[0], 12), new Computer("", null, null, localDates[0], 1),
			new Computer("", null, localDates[2], localDates[0], 12) };

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		validator = new ComputerValidator();
		validator.setComputerSearcher(computerSearcherMock, computerUpdaterMock);
	}

	@Test
	public void findByIdTest() throws SQLException {
		Computer c = fakeComputerList[0];
		Optional<Computer> computerOpt = Optional.of(c);
		Optional<Computer> emptyComputer = Optional.empty();

		Mockito.when(computerSearcherMock.fetchById(c.getId())).thenReturn(computerOpt);
		Mockito.when(computerSearcherMock.fetchById(0)).thenReturn(emptyComputer);

		Assert.assertEquals(computerOpt, this.validator.findById(c.getId()));
		Assert.assertEquals(emptyComputer, this.validator.findById(0));

	}

	@Test
	public void fetchListTest() throws SQLException {
		List<Computer> computerList = new ArrayList<>();
		for (Computer c : fakeComputerList)
			computerList.add(c);

		Mockito.when(computerSearcherMock.fetchList()).thenReturn(computerList);

		List<Computer> l = this.validator.fetchList();
		Assert.assertEquals(l.size(), fakeComputerList.length);
		for (Computer comp : fakeComputerList) {
			Assert.assertTrue(l.contains(comp));
		}
	}

	@Test
	public void fetchWithOffsetTest() throws SQLException {
		Page p = new Page(fakeComputerList.length);

		List<Computer> computerList = new ArrayList<>();
		for (Computer c : fakeComputerList)
			computerList.add(0, c);

		Mockito.when(computerSearcherMock.fetchWithOffset(p)).thenReturn(computerList);

		Assert.assertEquals(validator.fetchWithOffset(p), computerList);
	}

	@Test
	public void getNumberOfElementsTest() throws SQLException {

		Mockito.when(computerSearcherMock.getNumberOfElements()).thenReturn(523);

		Assert.assertEquals(validator.getNumberOfElements(), 523);
	}

	//// Partie mise à jour

	@Test
	public void createComputerTest() throws SQLException {

		Mockito.when(computerUpdaterMock.createComputer(Mockito.any(Computer.class))).thenReturn(24L);

		for (Computer c : fakeComputerList) // Normalement tout passe
			try {
				Assert.assertEquals(24L, validator.createComputer(c));
			} catch (InvalidComputerInstanceException e) {
				System.out.println(c);
				Assert.fail();
			}

		for (int i = 0; i < 2; ++i) {
			Computer c = invalidComputerInstanceList[i];
			try {
				validator.createComputer(c);
				Assert.fail();
			} catch (InvalidComputerInstanceException exn) {
				Assert.assertEquals(exn.getProblems().size(), 1);
				Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_NAME));
			}
		}

		Computer c2 = invalidComputerInstanceList[2];
		try {
			validator.createComputer(c2);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 1);
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE));
		}

		Computer c3 = invalidComputerInstanceList[3];
		try {
			validator.createComputer(c3);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 1);
			Assert.assertTrue(
					exn.getProblems().contains(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
		}
		Computer c4 = invalidComputerInstanceList[4];
		try {
			validator.createComputer(c4);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 2);
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_NAME));
			Assert.assertTrue(
					exn.getProblems().contains(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
		}
		Computer c5 = invalidComputerInstanceList[5];
		try {
			validator.createComputer(c5);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 2);
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_NAME));
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE));
		}
	}

	@Test
	public void deleteComputerTest() throws SQLException {

		Mockito.when(computerUpdaterMock.deleteById(56)).thenReturn(1);

		Assert.assertEquals(1, validator.deleteComputer(56));
	}

	@Test
	public void updateComputerTest() throws SQLException {

		Mockito.when(computerUpdaterMock.updateComputer(Mockito.any(Computer.class))).thenReturn(1);

		for (Computer c : fakeComputerList) // Normalement tout passe
			try {
				Assert.assertEquals(1, validator.updateComputer(c));
			} catch (InvalidComputerInstanceException e) {
				Assert.fail();
			}

		for (int i = 0; i < 2; ++i) {
			Computer c = invalidComputerInstanceList[i];
			try {
				validator.updateComputer(c);
				Assert.fail();
			} catch (InvalidComputerInstanceException exn) {
				Assert.assertEquals(exn.getProblems().size(), 1);
				Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_NAME));
			}
		}

		Computer c2 = invalidComputerInstanceList[2];
		try {
			validator.updateComputer(c2);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 1);
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE));
		}

		Computer c3 = invalidComputerInstanceList[3];
		try {
			validator.updateComputer(c3);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 1);
			Assert.assertTrue(
					exn.getProblems().contains(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
		}
		Computer c4 = invalidComputerInstanceList[4];
		try {
			validator.updateComputer(c4);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 2);
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_NAME));
			Assert.assertTrue(
					exn.getProblems().contains(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION));
		}
		Computer c5 = invalidComputerInstanceList[5];
		try {
			validator.updateComputer(c5);
			Assert.fail();
		} catch (InvalidComputerInstanceException exn) {
			Assert.assertEquals(exn.getProblems().size(), 2);
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_NAME));
			Assert.assertTrue(exn.getProblems().contains(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE));
		}
	}

}
