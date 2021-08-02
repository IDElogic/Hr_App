package hu.webuni.hr.fic.web;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.fic.dto.AvgSalaryDto;
import hu.webuni.hr.fic.dto.CompanyDto;
import hu.webuni.hr.fic.dto.EmployeeDto;
import hu.webuni.hr.fic.mapper.CompanyMapper;
import hu.webuni.hr.fic.mapper.EmployeeMapper;
import hu.webuni.hr.fic.model.Company;
import hu.webuni.hr.fic.model.Employee;
import hu.webuni.hr.fic.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyContorller {

	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	// Az összes cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping
	public List<CompanyDto> getAllCompanies(@RequestParam(required = false) String full) {
		List<Company> allCompanies = companyService.getAllCompanies(full);
		if (full == null || full.equals("false")) {
			return companyMapper.companiesToSummaryDtos(allCompanies);
		} else {
			return companyMapper.companiesToDtos(allCompanies);
		}
	}

	// Azon cégek kilistázása, melyek egy bizonyos fizetés feletti alkalmazottat
	// alkalmaznak.
	// Full paraméter megléte esetén az alkalmazottak adataival együtt.
	@GetMapping(params = "aboveSalary")
	public List<CompanyDto> getCompaniesAboveASalary(@RequestParam int aboveSalary,
			@RequestParam(required = false) String full) {
		List<Company> allCompanies = companyService.findWhereEmployeeSalaryIsGreaterThan(aboveSalary);
		if (full == null || full.equals("false")) {
			return companyMapper.companiesToSummaryDtos(allCompanies);
		} else
			return companyMapper.companiesToDtos(allCompanies);
	}

	// Azon cégek kilistázása, melyek egy bizonyos látszám feletti alkalmazottat
	// alkalmaznak.
	// Full paraméter megléte esetén az alkalmazottak adataival együtt.
	@GetMapping(params = "aboveEmployeeNumber")
	public List<CompanyDto> getCompaniesAboveEmployeeNumber(@RequestParam int aboveEmployeeNumber,
			@RequestParam(required = false) String full) {
		List<Company> filteredCompanies = companyService.findWhereEmployeeNumberIsAbove(aboveEmployeeNumber);
		if (full == null || full.equals("false")) {
			return companyMapper.companiesToSummaryDtos(filteredCompanies);
		} else
			return companyMapper.companiesToDtos(filteredCompanies);
	}

	// Egy bizonyos cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping("/{id}")
	public CompanyDto getCompanyById(@PathVariable long id, @RequestParam(required = false) String full) {
		Company company = null;
		CompanyDto companyDto = null;
		if (full == null || full.equals("false")) {
			company = companyService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			companyDto = companyMapper.companyToSummaryDto(company);
		} else {
			company = companyService.findByIdFull(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			companyDto = companyMapper.companyToDto(company);
		}
		return companyDto;
	}

	// Egy adott vállalat alkalmazottainak átlagfizetése, titulusuk szerint
	// csoportosítva.
	@GetMapping(value = "/{id}", params = "avgSalaryByTitle")
	public List<AvgSalaryDto> getAverageSalariesByTitleAtACompany(@PathVariable long id,
			@RequestParam boolean avgSalaryByTitle) {
		if (avgSalaryByTitle) {
			try {
				return companyService.listAverageSalaryiesGroupedByTitlesAtACompany(id);
			} catch (NoSuchElementException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
		}
		return null;
	}

	// Egy új cég hozzáadása
	@PostMapping
	public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
		Company company = companyMapper.dtoToCompany(companyDto);
		try {
			return companyMapper.companyToDto(companyService.addNewCompany(company, companyDto.getLegalForm()));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
		}
	}

	// Egy adott cég módosítása
	@PutMapping("/{registrationNumber}")
	public CompanyDto modifyCompany(@PathVariable long registrationNumber, @RequestBody CompanyDto companyDto) {
		companyDto.setRegistrationNumber(registrationNumber);
		Company company = companyMapper.dtoToCompany(companyDto);
		try {
			return companyMapper.companyToDto(companyService.updateCompany(company, companyDto.getLegalForm()));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY);
		}
	}

	// Adott cég törlése
	@DeleteMapping("/{registrationNumber}")
	public void deleteCompany(@PathVariable long registrationNumber) {
		companyService.delete(registrationNumber);
	}

	// Egy cég egy bizonyos alkalmazottjának törlése
	@DeleteMapping("/{registrationNumber}/employee/{id}")
	public CompanyDto deleteEmployeeFromACompany(@PathVariable long registrationNumber, @PathVariable long id) {
		try {
			return companyMapper.companyToDto(companyService.deleteEmployee(registrationNumber, id));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	// Alkalmazott hozzáadása egy céghez
	@PostMapping("/{registrationNumber}/employee")
	public CompanyDto addEmployeeToACompany(@PathVariable long registrationNumber,
			@RequestBody EmployeeDto employeeDto) {
		try {
			Employee employee = employeeMapper.dtoToEmployee(employeeDto);
			return companyMapper
					.companyToDto(companyService.addEmployee(registrationNumber, employeeDto.getTitle(), employeeDto.getSuperiorId(), employee));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	// Egy cég alkalmazottainak módosítása
	@PutMapping("/{registrationNumber}/employee")
	public CompanyDto modifyEmployeesOfACompany(@PathVariable long registrationNumber,
			@RequestBody List<EmployeeDto> employeeDtos) {
		try {
			return companyMapper.companyToDto(companyService.replaceEmployees(employeeMapper.dtosToEmployees(employeeDtos),registrationNumber));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
