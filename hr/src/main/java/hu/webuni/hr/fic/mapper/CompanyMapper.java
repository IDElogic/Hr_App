package hu.webuni.hr.fic.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.fic.dto.CompanyDto;
import hu.webuni.hr.fic.dto.EmployeeDto;
import hu.webuni.hr.fic.model.Company;
import hu.webuni.hr.fic.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
	
	List<CompanyDto> companiesToDtos(List<Company> company);

	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToSummaryDtos(List<Company> company);

	@Mapping(source = "legalForm.form", target = "legalForm")
	@Mapping(target = "employees", ignore = true)
	@Named("summary")
	CompanyDto companyToSummaryDto(Company company);

	@Mapping(source = "legalForm.form", target = "legalForm")
	CompanyDto companyToDto(Company company);

	@Mapping(target = "legalForm", ignore = true)
	Company dtoToCompany(CompanyDto companyDto);

	@Mapping(source = "company.name", target = "companyName")
	@Mapping(source = "position.name", target = "title")
	EmployeeDto employeeToDto(Employee employee);
	
	

}
