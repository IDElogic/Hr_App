package hu.webuni.hr.fic.dto;

public class LegalFormDto {

	private long id;
	private String form;
	
	public LegalFormDto(long id, String form) {
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
