package hu.webuni.hr.fic.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.fic.dto.LegalFormDto;
import hu.webuni.hr.fic.model.LegalForm;

@Mapper(componentModel = "spring")
public interface LegalFormMapper {
	
	LegalFormDto legalFormToLegalFormDto(LegalForm legalForm);
	
	List<LegalFormDto> legalFormsToLegalFormDtos(List<LegalForm> legalForms);
	
	LegalForm legalFormDTOToLegalForm(LegalFormDto legalFormDto);

	
}
