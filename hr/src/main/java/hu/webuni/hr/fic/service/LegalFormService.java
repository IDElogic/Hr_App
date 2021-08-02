package hu.webuni.hr.fic.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.fic.model.LegalForm;
import hu.webuni.hr.fic.repository.LegalFormRepository;

@Service
public class LegalFormService {
	
	@Autowired
	LegalFormRepository legalFormRepository;
	
	public List<LegalForm> findAll() {
		return legalFormRepository.findAll();
	}
	
	public Optional<LegalForm> findById(long id) {
		return legalFormRepository.findById(id);
	}
	
	public Optional<LegalForm> findByForm(String form) {
		return legalFormRepository.findByForm(form);
	}
	
	@Transactional
	public LegalForm updateLegalForm(LegalForm legalForm) {
		findById(legalForm.getId()).orElseThrow(() -> new NoSuchElementException());
		return save(legalForm);
	}
	
	@Transactional
	public LegalForm save(LegalForm legalForm) {
		return legalFormRepository.save(legalForm);
	}
	
	@Transactional
	public void delete(long id) {
		findById(id).orElseThrow(() -> new NoSuchElementException());
		legalFormRepository.deleteById(id);
	}


}
