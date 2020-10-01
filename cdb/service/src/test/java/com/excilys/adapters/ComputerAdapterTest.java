package com.excilys.adapters;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.excilys.model.CompanyDto;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDto;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.SortEntry;
import com.excilys.service.ComputerService;
import com.excilys.service.InvalidComputerException;

public class ComputerAdapterTest {

    ComputerService service = Mockito.mock(ComputerService.class);

    // TODO mock + test de ce truc là dans un autre fichier
    ComputerDtoValidator validator;

    ComputerAdapter adapter;

    @Before
    public void init() {
        this.adapter = new ComputerAdapter();
        this.service = Mockito.mock(ComputerService.class);
        this.validator = new ComputerDtoValidator();
        this.adapter.setService(this.service);
        this.adapter.setValidator(this.validator);
    }

    Computer c1 = Computer.getBuilder()
            .setName("pouet").setId(12L).build();
    Computer c2 = Computer.getBuilder()
            .setName("plopiplop").setId(122L).build();

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

    @Test
    public void testEditComputerDtoInvalidInstances() throws InvalidComputerException {
        try {
            this.adapter.updateComputer(this.emptyName);
            Assert.fail();
        } catch (InvalidComputerDtoException e) {
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.INVALID_NAME));
        }
        try {
            this.adapter.updateComputer(this.nullDto);
            Assert.fail();
        } catch (InvalidComputerDtoException e) {
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.NULL_DTO));

        }
        try {
            this.adapter.updateComputer(this.invalidDates);
            Assert.fail();
        } catch (InvalidComputerDtoException e) {
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.INVALID_DATE_INTRO));
            Assert.assertTrue(e.getProblems().contains(ComputerDTOProblems.INVALID_DATE_DISCO));
        }
    }

    @Test
    public void testUpdateComputerDto() throws InvalidComputerException, InvalidComputerDtoException {
        Mockito.when(this.service.update(this.c1)).thenReturn(1);
        Assert.assertEquals(this.adapter.updateComputer(this.dto1), 1);
    }
}
