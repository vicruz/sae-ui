package com.sae.gandhi.spring.vo;

import java.io.Serializable;

public class AlumnosListVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer alumnoId;
	private String alumnoNombre;
	private String alumnoApPaterno;
	private String alumnoApMaterno;
	private String alumnoTutor;
	private String alumnoTutorTelefono1;
	private byte[] alumnoImagen;
	private String cursoNombre;
	private Integer alumnoStatus;
	
	public AlumnosListVO(Integer alumnoId, String alumnoNombre, String alumnoApPaterno, String alumnoApMaterno,
			String alumnoTutor, String alumnoTutorTelefono1, byte[] alumnoImagen) {
		super();
		this.alumnoId = alumnoId;
		this.alumnoNombre = alumnoNombre;
		this.alumnoApPaterno = alumnoApPaterno;
		this.alumnoApMaterno = alumnoApMaterno;
		this.alumnoTutor = alumnoTutor;
		this.alumnoTutorTelefono1 = alumnoTutorTelefono1;
		this.alumnoImagen = alumnoImagen;
	}
	
	public AlumnosListVO(Integer alumnoId, String alumnoNombre, String alumnoApPaterno, String alumnoApMaterno,
			String alumnoTutor, String alumnoTutorTelefono1, byte[] alumnoImagen, String cursoNombre) {
		super();
		this.alumnoId = alumnoId;
		this.alumnoNombre = alumnoNombre;
		this.alumnoApPaterno = alumnoApPaterno;
		this.alumnoApMaterno = alumnoApMaterno;
		this.alumnoTutor = alumnoTutor;
		this.alumnoTutorTelefono1 = alumnoTutorTelefono1;
		this.alumnoImagen = alumnoImagen;
		this.cursoNombre = cursoNombre;
	}
	
	public AlumnosListVO(Integer alumnoId, String alumnoNombre, String alumnoApPaterno, String alumnoApMaterno,
			String alumnoTutor, String alumnoTutorTelefono1, byte[] alumnoImagen, String cursoNombre, Integer alumnoStatus) {
		super();
		this.alumnoId = alumnoId;
		this.alumnoNombre = alumnoNombre;
		this.alumnoApPaterno = alumnoApPaterno;
		this.alumnoApMaterno = alumnoApMaterno;
		this.alumnoTutor = alumnoTutor;
		this.alumnoTutorTelefono1 = alumnoTutorTelefono1;
		this.alumnoImagen = alumnoImagen;
		this.cursoNombre = cursoNombre;
		this.alumnoStatus = alumnoStatus;
	}
	
	public Integer getAlumnoId() {
		return alumnoId;
	}
	public void setAlumnoId(Integer alumnoId) {
		this.alumnoId = alumnoId;
	}
	public String getAlumnoNombre() {
		return alumnoNombre;
	}
	public void setAlumnoNombre(String alumnoNombre) {
		this.alumnoNombre = alumnoNombre;
	}
	public String getAlumnoApPaterno() {
		return alumnoApPaterno;
	}
	public void setAlumnoApPaterno(String alumnoApPaterno) {
		this.alumnoApPaterno = alumnoApPaterno;
	}
	public String getAlumnoApMaterno() {
		return alumnoApMaterno;
	}
	public void setAlumnoApMaterno(String alumnoApMaterno) {
		this.alumnoApMaterno = alumnoApMaterno;
	}
	public String getAlumnoTutor() {
		return alumnoTutor;
	}
	public void setAlumnoTutor(String alumnoTutor) {
		this.alumnoTutor = alumnoTutor;
	}
	public String getAlumnoTutorTelefono1() {
		return alumnoTutorTelefono1;
	}
	public void setAlumnoTutorTelefono1(String alumnoTutorTelefono1) {
		this.alumnoTutorTelefono1 = alumnoTutorTelefono1;
	}
	public byte[] getAlumnoImagen() {
		return alumnoImagen;
	}
	public void setAlumnoImagen(byte[] alumnoImagen) {
		this.alumnoImagen = alumnoImagen;
	}
	public String getCursoNombre() {
		return cursoNombre;
	}
	public void setCursoNombre(String cursoNombre) {
		this.cursoNombre = cursoNombre;
	}

	public Integer getAlumnoStatus() {
		return alumnoStatus;
	}

	public void setAlumnoStatus(Integer alumnoStatus) {
		this.alumnoStatus = alumnoStatus;
	}

}
