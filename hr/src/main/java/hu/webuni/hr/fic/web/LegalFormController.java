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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.fic.dto.LegalFormDto;
import hu.webuni.hr.fic.mapper.LegalFormMapper;
import hu.webuni.hr.fic.service.LegalFormService;

@RestController
@RequestMapping("/api/legalforms")
public class LegalFormController {

	@Autowired
	LegalFormService legalFormService;

	@Autowired
	LegalFormMapper legalFormMapper;

	@GetMapping
	List<LegalFormDto> getLegalForms() {
		return legalFormMapper.legalFormsToLegalFormDtos(legalFormService.findAll());
	}

	@PostMapping
	LegalFormDto addLegalForm(@RequestBody LegalFormDto legalFormDto) {
		return legalFormMapper
				.legalFormToLegalFormDto(legalFormService.save(legalFormMapper.legalFormDTOToLegalForm(legalFormDto)));
	}

	@PutMapping("/{id}")
	LegalFormDto changeLegalForm(@RequestBody LegalFormDto legalFormDto, @PathVariable long id) {
		legalFormDto.setId(id);
		try {
			return legalFormMapper.legalFormToLegalFormDto(
					legalFormService.updateLegalForm(legalFormMapper.legalFormDTOToLegalForm(legalFormDto)));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("{/id}")
	void deleteLegalForm(@PathVariable long id) {
		try {
			legalFormService.delete(id);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
