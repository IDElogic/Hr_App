package hu.webuni.hr.fic.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.fic.dto.EmployeeDto;
import hu.webuni.hr.fic.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(source="position.name", target="title")
	@Mapping(source="company.name", target="companyName")
	@Mapping(source="superior.id", target="superiorId")
	@Mapping(target="password", ignore = true)
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	

	@Mapping(source="position.name", target="title")
	@Mapping(source="company.name", target="companyName")
	@Mapping(source="superior.id", target="superiorId")
	@Mapping(target="password", ignore = true)
	EmployeeDto employeeToDto(Employee employee);

	@Mapping(source = "title", target = "position.name")
	@Mapping(source = "companyName", target = "company.name")
	@Mapping(source = "superiorId", target= "superior.id")
	Employee dtoToEmployee(EmployeeDto employeeDto);

	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	

}
