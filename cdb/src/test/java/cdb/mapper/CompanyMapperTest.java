package cdb.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.excilys.mapper.CompanyMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;

public class CompanyMapperTest {

    private final Company[] validCompanies = { new Company("Company1", 1L),
            new Company("Company2", 2L), new Company("Fromage", 543L), new Company("Camembert"),
            new Company("pouet") };

    private final CompanyDTO[] validCompanyDTOs = { new CompanyDTO("Company1", 1L),
            new CompanyDTO("Company2", 2L), new CompanyDTO("Fromage", 543L),
            new CompanyDTO("Camembert", null), new CompanyDTO("pouet", 0L) };

    @Test
    public void companyToDTOTest() {
        for (int i = 0; i < this.validCompanies.length; ++i) {
            assertEquals(this.validCompanyDTOs[i],
                    CompanyMapper.companyToDTO(this.validCompanies[i]).get());
            assertTrue(
                    this.validCompanies[i].getId() == 0 || new Long(this.validCompanies[i].getId())
                            .equals(this.validCompanyDTOs[i].getId()));
        }
        assertEquals(Optional.empty(), CompanyMapper.companyToDTO(null));

    }

    @Test
    public void doubleCallTest() {
        for (int i = 0; i < this.validCompanyDTOs.length; ++i) {
            assertEquals(this.validCompanyDTOs[i],
                    CompanyMapper.companyToDTO(
                            CompanyMapper.companyDTOToCompany(this.validCompanyDTOs[i]).get())
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
        for (int i = 0; i < this.validCompanyDTOs.length; ++i) {
            assertEquals(CompanyMapper.companyDTOToCompany(this.validCompanyDTOs[i]).get(),
                    this.validCompanies[i]);
            assertEquals(CompanyMapper.companyDTOToCompany(this.validCompanyDTOs[i]).get().getId(),
                    this.validCompanies[i].getId());
        }
    }
}
