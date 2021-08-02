package hu.webuni.hr.fic.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LegalForm {
	
	@Id
	@GeneratedValue
	private long id;
	private String form;
	
	public LegalForm() {
		
	}
	
	public LegalForm(long id, String form) {
		this.id = id;
		this.form = form;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	
	
}
