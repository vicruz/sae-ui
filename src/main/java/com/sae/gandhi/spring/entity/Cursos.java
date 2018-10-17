package com.sae.gandhi.spring.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="CURSOS")
public class Cursos {

	@Id
	@Column(name="CURSO_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@NotNull
	private Integer cursoId;
	
	@Column(name="CURSO_NOMBRE")
	@Size(max=255)
	private String cursoNombre;
	
	@Column(name="CURSO_FECHA_INICIO")
	@Temporal(TemporalType.DATE)
	private Date cursoFechaInicio;
	
	@Column(name="CURSO_FECHA_FIN")
	@Temporal(TemporalType.DATE)
	private Date cursoFechaFin;
	
	@Column(name="FECHA_CREACION")
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
	@Column(name="CURSO_ESTATUS")
	private Integer cursoStatus;
	
	@OneToMany(mappedBy="cursos")
	private List<CursoCostos> lstCursoCostos;

	public Integer getCursoId() {
		return cursoId;
	}

	public void setCursoId(Integer cursoId) {
		this.cursoId = cursoId;
	}

	public String getCursoNombre() {
		return cursoNombre;
	}

	public void setCursoNombre(String cursoNombre) {
		this.cursoNombre = cursoNombre;
	}

	public Date getCursoFechaInicio() {
		return cursoFechaInicio;
	}

	public void setCursoFechaInicio(Date cursoFechaInicio) {
		this.cursoFechaInicio = cursoFechaInicio;
	}

	public Date getCursoFechaFin() {
		return cursoFechaFin;
	}

	public void setCursoFechaFin(Date cursoFechaFin) {
		this.cursoFechaFin = cursoFechaFin;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getCursoStatus() {
		return cursoStatus;
	}

	public void setCursoStatus(Integer cursoStatus) {
		this.cursoStatus = cursoStatus;
	}

	public List<CursoCostos> getLstCursoCostos() {
		return lstCursoCostos;
	}

	public void setLstCursoCostos(List<CursoCostos> lstCursoCostos) {
		this.lstCursoCostos = lstCursoCostos;
	}
	
}
