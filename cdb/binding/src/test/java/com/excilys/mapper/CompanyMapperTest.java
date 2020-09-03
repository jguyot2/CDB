package com.excilys.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.excilys.model.Company;
import com.excilys.model.CompanyDto;


public class CompanyMapperTest {

    private final Company[] validCompanies = { new Company("Company1", 1L),
            new Company("Company2", 2L), new Company("Fromage", 543L), new Company("Camembert"),
            new Company("pouet") };

    private final CompanyDto[] validCompanyDtos = { new CompanyDto("Company1", 1L),
            new CompanyDto("Company2", 2L), new CompanyDto("Fromage", 543L),
            new CompanyDto("Camembert", null), new CompanyDto("pouet", 0L) };


    @Test
    public void companyToDTOTest() {
        for (int i = 0; i < this.validCompanies.length; ++i) {
            assertEquals(this.validCompanyDtos[i],
                    CompanyMapper.companyToDTO(this.validCompanies[i]).get());
            assertTrue(
                    this.validCompanies[i].getId() == 0 || new Long(this.validCompanies[i].getId())
                            .equals(this.validCompanyDtos[i].getId()));
        }
        assertEquals(Optional.empty(), CompanyMapper.companyToDTO(null));

    }

    @Test
    public void doubleCallTest() {
        for (int i = 0; i < this.validCompanyDtos.length; ++i) {
            assertEquals(this.validCompanyDtos[i],
                    CompanyMapper.companyToDTO(
                            CompanyMapper.companyDTOToCompany(this.validCompanyDtos[i]).get())
                            .get());
            assertEquals(this.validCompanies[i],
                    CompanyMapper
                            .companyDTOToCompany(
                                    CompanyMapper.companyToDTO(this.validCompanies[i]).get())
                            .get());
            assertEquals(this.validCompanies[i].getId(), CompanyMapper
                    .companyDTOToCompany(CompanyMapper.companyToDTO(this.validCompanies[i]).get())
                    .get().getId());
        }
    }

    @Test
    public void dtoToCompanyTest() {
        for (int i = 0; i < this.validCompanyDtos.length; ++i) {
            assertEquals(CompanyMapper.companyDTOToCompany(this.validCompanyDtos[i]).get(),
                    this.validCompanies[i]);
            assertEquals(CompanyMapper.companyDTOToCompany(this.validCompanyDtos[i]).get().getId(),
                    this.validCompanies[i].getId());
        }
    }
}
