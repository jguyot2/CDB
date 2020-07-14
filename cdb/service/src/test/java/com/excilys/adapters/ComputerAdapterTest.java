package com.excilys.adapters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.model.CompanyDto;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDto;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.SortEntry;
import com.excilys.persistence.config.PersistenceConfig;
import com.excilys.service.ComputerService;
import com.excilys.service.InvalidComputerException;
import com.excilys.serviceconfig.ServiceConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ServiceConfig.class, PersistenceConfig.class })
public class ComputerAdapterTest {
    @Mock
    ComputerService service;

    @Autowired
    ComputerAdapter adapter;

    @Before
    public void init() {
        this.service = Mockito.mock(ComputerService.class);
        this.adapter.setService(this.service);
    }

    Computer c1 = new Computer("pouet", null, null, null, 12);
    Computer c2 = new Computer("plopiplop", null, null, null, 122);
    ComputerDto dto1 = new ComputerDto("pouet", 12L, (CompanyDto) null, null, null);
    ComputerDto dto2 = new ComputerDto("plopiplop", 122L, (CompanyDto) null, null, null);
    ComputerDto emptyName = new ComputerDto("", 12L, (CompanyDto) null, null, null);
    ComputerDto nullDto = null;
    ComputerDto invalidDates = new ComputerDto("plopiplop", 12L, (CompanyDto) null, "pouet", "pouet");

    @Test
    public void testFetchList() {
        Mockito.when(this.service.fetchList()).thenReturn(Arrays.asList(this.c1, this.c2));
        Assert.assertEquals(Arrays.asList(this.dto1, this.dto2), this.adapter.fetchList());
    }

    @Test
    public void testAddComputerDto() throws InvalidComputerException, InvalidComputerDtoException {
        Mockito.when(this.service.addComputer(this.c1)).thenReturn(12L);
        Assert.assertEquals(this.adapter.addComputerDTO(this.dto1), 12L);
    }

    @Test
    public void testAddComputerDtoInvalidInstances() throws InvalidComputerException {
        try {
            this.adapter.addComputerDTO(this.emptyName);
            Assert.fail();
        } catch (InvalidComputerDtoException e) {
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.INVALID_NAME));
        }
        try {
            this.adapter.addComputerDTO(this.nullDto);
            Assert.fail();
        } catch (InvalidComputerDtoException e) {
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.NULL_DTO));

        }
        try {
            this.adapter.addComputerDTO(this.invalidDates);
            Assert.fail();
        } catch (InvalidComputerDtoException e) {
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.INVALID_DATE_INTRO));
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.INVALID_DATE_DISCO));
        }
    }

    @Test
    public void testFindById() {
        Mockito.when(this.service.findById(12L)).thenReturn(Optional.of(this.c1));
        Mockito.when(this.service.findById(69L)).thenReturn(Optional.empty());

        Assert.assertEquals(Optional.of(this.dto1), this.adapter.findById(12L));
        Assert.assertEquals(Optional.empty(), this.adapter.findById(69L));
    }

    @Test
    public void testAllFetchList() throws DuplicatedSortEntriesException {
        List<Computer> computerList = Arrays.asList(this.c1, this.c2);
        List<ComputerDto> dtoList = Arrays.asList(this.dto1, this.dto2);
        Page p = new Page();
        List<SortEntry> sortEntryList = Arrays.asList();
        String search = "plopiplop";
        Mockito.when(this.service.fetchList()).thenReturn(computerList);
        Mockito.when(this.service.fetchList(p)).thenReturn(computerList);
        Mockito.when(this.service.fetchList(search)).thenReturn(computerList);

        Mockito.when(this.service.fetchList(p, search)).thenReturn(computerList);
        Mockito.when(this.service.fetchList(p, sortEntryList)).thenReturn(computerList);
        Mockito.when(this.service.fetchList(p, search, sortEntryList)).thenReturn(computerList);

        Assert.assertEquals(dtoList, this.adapter.fetchList());
        Assert.assertEquals(dtoList, this.adapter.fetchList(p));
        Assert.assertEquals(dtoList, this.adapter.fetchList(search));

        Assert.assertEquals(dtoList, this.adapter.fetchList(p, search));
        Assert.assertEquals(dtoList, this.adapter.fetchList(p, sortEntryList));
        Assert.assertEquals(dtoList, this.adapter.fetchList(p, search, sortEntryList));
    }
}
