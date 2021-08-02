package hu.webuni.hr.fic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.fic.model.LegalForm;

public interface LegalFormRepository extends JpaRepository<LegalForm, Long> {

	Optional<LegalForm> findByForm(String form);
	
}
