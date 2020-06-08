package cdb.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.excilys.mapper.CompanyMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;

public class CompanyMapperTest {

    private final Company[] validCompanies = { new Company("Company1", 1), new Company("Company2", 2),
            new Company("Fromage", 543), new Company("Camembert"), new Company("pouet") };

    private final CompanyDTO[] validCompanyDTOs = { new CompanyDTO("Company1", "1"),
            new CompanyDTO("Company2", "2"), new CompanyDTO("Fromage", "543"),
            new CompanyDTO("Camembert", null), new CompanyDTO("pouet", "0") };

    @Test
    public void companyToDTOTest() {
        for (int i = 0; i < validCompanies.length; ++i) {
            assertEquals(validCompanyDTOs[i], CompanyMapper.companyToDTO(validCompanies[i]).get());
            assertTrue(validCompanies[i].getId() == 0
                    || Long.toString(validCompanies[i].getId()).equals(validCompanyDTOs[i].getId()));
        }
        assertEquals(Optional.empty(), CompanyMapper.companyToDTO(null));

    }

    @Test
    public void doubleCallTest() {
        for (int i = 0; i < validCompanyDTOs.length; ++i) {
            assertEquals(validCompanyDTOs[i], CompanyMapper
                    .companyToDTO(CompanyMapper.companyDTOToCompany(validCompanyDTOs[i]).get()).get());
            assertEquals(validCompanies[i], CompanyMapper
                    .companyDTOToCompany(CompanyMapper.companyToDTO(validCompanies[i]).get()).get());
            assertEquals(validCompanies[i].getId(), CompanyMapper
                    .companyDTOToCompany(CompanyMapper.companyToDTO(validCompanies[i]).get()).get().getId());
        }
    }

    @Test
    public void dtoToCompanyTest() {
        for (int i = 0; i < validCompanyDTOs.length; ++i) {
            assertEquals(CompanyMapper.companyDTOToCompany(validCompanyDTOs[i]).get(), validCompanies[i]);
            assertEquals(CompanyMapper.companyDTOToCompany(validCompanyDTOs[i]).get().getId(),
                    validCompanies[i].getId());
        }
    }
}
