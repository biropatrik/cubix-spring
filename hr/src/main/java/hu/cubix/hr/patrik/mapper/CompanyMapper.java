package hu.cubix.hr.patrik.mapper;

import hu.cubix.hr.patrik.dto.CompanyDto;
import hu.cubix.hr.patrik.model.Company;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    public CompanyDto companyToDto(Company company);

    public List<CompanyDto> companiesToDtos(List<Company> companies);

    public Company dtoToCompany(CompanyDto companyDto);

    public List<Company> dtosToCompanies(List<CompanyDto> companyDtos);
}
