package com.sae.gandhi.spring.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="ALUMNOS")
public class Alumnos {

	@Id
	@Column(name="ALUMNO_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@NotNull
	private Integer alumnoId;
	
	@Column(name="ALUMNO_NOMBRE")
	private String alumnoNombre;
	
	@Column(name="ALUMNO_AP_PATERNO")
	private String alumnoApPaterno;
	
	@Column(name="ALUMNO_AP_MATERNO")
	private String alumnoApMaterno;
	
	@Column(name="ALUMNO_TUTOR")
	private String alumnoTutor;
	
	@Column(name="ALUMNO_EMAIL")
	private String alumnoTutorEmail;
	
	@Column(name="ALUMNO_TELEFONO1")
	private String alumnoTutorTelefono1;
	
	@Column(name="ALUMNO_TELEFONO2")
	private String alumnoTutorTelefono2;
	
	@Column(name="ALUMNO_FECHA_NAC")
	@Temporal(TemporalType.DATE)
	private Date alumnoFechaNac;
	
	@Column(name="ALUMNO_IMAGEN")
	@Lob
	private byte[] alumnoImagen;
	
	@Column(name="ALUMNO_SALDO")
	private BigDecimal alumnoSaldo;
	
	@Column(name="ALUMNO_ACTIVO")
	private Boolean alumnoActivo;
	
	@Column(name="ALUMNO_ESTATUS")
	private Integer alumnoEstatus;
	
	@Column(name="FECHA_CREACION")
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
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
	public Date getAlumnoFechaNac() {
		return alumnoFechaNac;
	}
	public void setAlumnoFechaNac(Date alumnoFechaNac) {
		this.alumnoFechaNac = alumnoFechaNac;
	}
	public byte[] getAlumnoImagen() {
		return alumnoImagen;
	}
	public void setAlumnoImagen(byte[] alumnoImagen) {
		this.alumnoImagen = alumnoImagen;
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
	
}
