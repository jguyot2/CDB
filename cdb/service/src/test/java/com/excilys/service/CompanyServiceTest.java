package com.excilys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;

import com.excilys.model.Company;
import com.excilys.model.Page;
import com.excilys.persistence.CompanySearcher;
import com.excilys.persistence.CompanyUpdater;
import com.excilys.persistence.DaoException;
import com.excilys.persistence.config.PersistenceConfig;
import com.excilys.serviceconfig.ServiceConfig;

@RunWith(BlockJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class, PersistenceConfig.class })
public class CompanyServiceTest {

    private static final Company[] fakeCompanyList = { new Company("POUET", 1L),
            new Company("J'AIME L'OCA", 2L),
            new Company("Café Oz", 5L), new Company("Chocolatine", 10L) };

    private CompanySearcher companySearcherMock = Mockito.mock(CompanySearcher.class);

    private CompanyUpdater companyUpdaterMock = Mockito.mock(CompanyUpdater.class);

    private CompanyService companyValidator = new CompanyService();

    @Test
    public void deleteCompanyTest() throws SQLException, DaoException {
        Mockito.doThrow(new DaoException(new Exception())).when(this.companyUpdaterMock).deleteCompany(122);
        Assert.assertEquals(this.companyValidator.deleteCompanyById(2), 1);
        Assert.assertEquals(this.companyValidator.deleteCompanyById(122), -1);
    }

    @Before
    public void init() {

        this.companyValidator = new CompanyService();
        this.companyValidator.setCompanySearcher(this.companySearcherMock);
        this.companyValidator.setCompanyUpdater(this.companyUpdaterMock);
    }

    @Test
    public void fetchListTest() throws DaoException {
        List<Company> companyList = new ArrayList<>();
        for (Company c : fakeCompanyList) {
            companyList.add(c);
        }

        Mockito.when(this.companySearcherMock.fetchList()).thenReturn(companyList);

        List<Company> l = this.companyValidator.fetchList();
        Assert.assertEquals(l.size(), fakeCompanyList.length);
        for (Company comp : fakeCompanyList) {
            Assert.assertTrue(l.contains(comp));
        }
    }

    @Test
    public void fetchListWithDbExceptionTest() throws DaoException {
        List<Company> companyList = new ArrayList<>();
        for (Company c : fakeCompanyList) {
            companyList.add(c);
        }
        Mockito.when(this.companySearcherMock.fetchList()).thenThrow(new DaoException());
        List<Company> l = this.companyValidator.fetchList();
        Assert.assertTrue(l.isEmpty());
    }

    @Test
    public void fetchWithOffsetTest() throws DaoException {
        Page p = new Page(4);

        List<Company> companyList = new ArrayList<>();
        for (Company c : fakeCompanyList) {
            companyList.add(0, c);
        }
        Mockito.when(this.companySearcherMock.fetchList(p)).thenReturn(companyList);
        Assert.assertEquals(this.companyValidator.fetchList(p), companyList);
    }

    @Test
    public void fetchWithOffsetAndDbExceptionTest() throws DaoException {
        Page p = new Page(4);

        List<Company> companyList = new ArrayList<>();
        for (Company c : fakeCompanyList) {
            companyList.add(0, c);
        }
        Mockito.when(this.companySearcherMock.fetchList(p)).thenThrow(new DaoException());
        Assert.assertEquals(this.companyValidator.fetchList(p), Arrays.asList());
    }

    @Test
    public void findByIdTest() throws DaoException {
        Company company42 = new Company("companyWithId42", 42L);
        Optional<Company> company42Opt = Optional.of(company42);
        Optional<Company> emptyCompany = Optional.empty();

        Mockito.when(this.companySearcherMock.fetchById(42)).thenReturn(company42Opt);
        Mockito.when(this.companySearcherMock.fetchById(0)).thenReturn(emptyCompany);

        Assert.assertEquals(company42Opt, this.companyValidator.findById(42));
        Assert.assertEquals(emptyCompany, this.companyValidator.findById(0));
    }

    @Test
    public void findByIdDbExceptionsTest() throws DaoException {
        Company company42 = new Company("companyWithId42", 42L);
        Optional<Company> company42Opt = Optional.of(company42);
        Optional<Company> emptyCompany = Optional.empty();

        Mockito.when(this.companySearcherMock.fetchById(42)).thenThrow(new DaoException());
        Mockito.when(this.companySearcherMock.fetchById(0)).thenThrow(new DaoException());

        Assert.assertEquals(emptyCompany, this.companyValidator.findById(42));
        Assert.assertEquals(emptyCompany, this.companyValidator.findById(0));
    }

    @Test
    public void getNumberOfElementsTest() throws DaoException {
        Mockito.when(this.companySearcherMock.getNumberOfElements()).thenReturn(523);

        Assert.assertEquals(this.companyValidator.getNumberOfElements(), 523);
    }

    @Test
    public void getNumberOfElementsDbExceptionTest() throws DaoException {
        Mockito.when(this.companySearcherMock.getNumberOfElements()).thenThrow(new DaoException());

        Assert.assertEquals(this.companyValidator.getNumberOfElements(), -1);
    }
}
