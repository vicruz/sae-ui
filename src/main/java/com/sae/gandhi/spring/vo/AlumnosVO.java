package com.sae.gandhi.spring.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public class AlumnosVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8389534049494795194L;
	private Integer alumnoId;
	private String alumnoNombre;
	private String alumnoApPaterno;
	private String alumnoApMaterno;
	private String alumnoTutor;
	private String alumnoTutorEmail;
	private String alumnoTutorTelefono1;
	private String alumnoTutorTelefono2;
	private LocalDate alumnoFechaNac;
	private byte[] alumnoImagen;
	private MemoryBuffer alumnoImagenMemory;
	private Boolean alumnoActivo;
	private Integer alumnoEstatus;
	private Date fechaCreacion;
	private BigDecimal alumnoSaldo;

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
	public String getAlumnoTutorEmail() {
		return alumnoTutorEmail;
	}
	public void setAlumnoTutorEmail(String alumnoTutorEmail) {
		this.alumnoTutorEmail = alumnoTutorEmail;
	}
	public String getAlumnoTutorTelefono1() {
		return alumnoTutorTelefono1;
	}
	public void setAlumnoTutorTelefono1(String alumnoTutorTelefono1) {
		this.alumnoTutorTelefono1 = alumnoTutorTelefono1;
	}
	public String getAlumnoTutorTelefono2() {
		return alumnoTutorTelefono2;
	}
	public void setAlumnoTutorTelefono2(String alumnoTutorTelefono2) {
		this.alumnoTutorTelefono2 = alumnoTutorTelefono2;
	}
	public LocalDate getAlumnoFechaNac() {
		return alumnoFechaNac;
	}
	public void setAlumnoFechaNac(LocalDate alumnoFechaNac) {
		this.alumnoFechaNac = alumnoFechaNac;
	}
	public byte[] getAlumnoImagen() {
		return alumnoImagen;
	}
	public void setAlumnoImagen(byte[] alumnoImagen) {
		this.alumnoImagen = alumnoImagen;
	}
	public MemoryBuffer getAlumnoImagenMemory() {
		return alumnoImagenMemory;
	}
	public void setAlumnoImagenMemory(MemoryBuffer alumnoImagenMemory) {
		this.alumnoImagenMemory = alumnoImagenMemory;
	}
	public Boolean getAlumnoActivo() {
		return alumnoActivo;
	}
	public void setAlumnoActivo(Boolean alumnoActivo) {
		this.alumnoActivo = alumnoActivo;
	}
	public Integer getAlumnoEstatus() {
		return alumnoEstatus;
	}
	public void setAlumnoEstatus(Integer alumnoEstatus) {
		this.alumnoEstatus = alumnoEstatus;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public BigDecimal getAlumnoSaldo() {
		return alumnoSaldo;
	}
	public void setAlumnoSaldo(BigDecimal alumnoSaldo) {
		this.alumnoSaldo = alumnoSaldo;
	}
	public String getAlumnoNombreCompleto(){
		return this.alumnoNombre + " " + this.alumnoApPaterno + " " + this.alumnoApMaterno;
	}
	
}
