package hu.cubix.hr.patrik.mapper;

import hu.cubix.hr.patrik.dto.CompanyDto;
import hu.cubix.hr.patrik.model.Company;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    public CompanyDto companyToDto(Company company);

    public List<CompanyDto> companiesToDtos(List<Company> companies);

    public Company dtoToCompany(CompanyDto companyDto);

    public List<Company> dtosToCompanies(List<CompanyDto> companyDtos);

    @Mapping(target = "employees", ignore = true)
    @Named("summary")
    public CompanyDto companyToSummaryDto(Company company);

    @IterableMapping(qualifiedByName = "summary")
    public List<CompanyDto> companiesToSummaryDtos(List<Company> companies);
}
