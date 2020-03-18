package cdb.service;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.model.Company;
import com.excilys.model.Page;
import com.excilys.persistence.CompanySearcher;
import com.excilys.service.CompanyValidator;

public class CompanyValidatorTest {
	private static final Company[] fakeCompanyList ={ 
			new Company("POUET", 1),
			new Company("J'AIME L'OCA", 2), 
			new Company("Café Oz", 5),
			new Company("Chocolatine", 10)
		};
	
	private CompanySearcher companySearcherMock = Mockito.mock(CompanySearcher.class);
	private CompanyValidator companyValidator = new CompanyValidator();
	
	@Before
	public void testInitialization() {
		MockitoAnnotations.initMocks(this);
		companyValidator.setCompanySearcher(companySearcherMock);
	}


	@Test
	public void findByIdTest() {
		Company company42 = new Company("companyWithId42", 42);
		Optional<Company> company42Opt = Optional.of(company42);
		Optional<Company> emptyCompany = Optional.empty();
		try {
			Mockito.when(companySearcherMock.fetchById(42)).thenReturn(company42Opt);
			Mockito.when(companySearcherMock.fetchById(0)).thenReturn(emptyCompany);
		} catch (SQLException e) {}
		
		Assert.assertEquals(company42Opt, this.companyValidator.findById(42));
		Assert.assertEquals(emptyCompany, this.companyValidator.findById(0));
		
	}
	
	@Test
	public void fetchListTest() {
		List<Company> companyList = new ArrayList<>();
		for(Company c : fakeCompanyList)
			companyList.add(c);
		
		try {
			Mockito.when(companySearcherMock.fetchList()).thenReturn(companyList);
		} catch (SQLException e) {
		}
		
		List <Company> l = this.companyValidator.fetchList();
		Assert.assertEquals(l.size(), fakeCompanyList.length);
		for(Company comp : fakeCompanyList) {
			Assert.assertTrue(l.contains(comp));
		}
	}
	
	@Test
	public void fetchWithOffsetTest() {
		Page p = new Page();
		
		List<Company> companyList = new ArrayList<>();
		for(Company c : fakeCompanyList)
			companyList.add(0, c);
		try {
			Mockito.when(companySearcherMock.fetchWithOffset(p)).thenReturn(companyList);
		} catch (SQLException e) {}
		
		Assert.assertEquals(companyValidator.fetchWithOffset(p), companyList);
	}
	
	@Test
	public void getNumberOfElementsTest() {
		try {
			Mockito.when(companySearcherMock.getNumberOfElements()).thenReturn(523);
		} catch (SQLException e) {}
		
		Assert.assertEquals(companyValidator.getNumberOfElements(), 523);
	}
}
