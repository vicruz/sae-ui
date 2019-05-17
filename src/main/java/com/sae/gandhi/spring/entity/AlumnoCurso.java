package com.sae.gandhi.spring.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ALUMNO_CURSO")
public class AlumnoCurso {
	
	@Id
	@Column(name = "ALUMNOCURSO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer alumnoCursoId;
	
	@Column(name = "ALUMNO_ID")
	private Integer alumnoId;
	
	@Column(name = "CURSO_ID")
	private Integer cursoId;
	
	@Column(name = "ALUMNOCURSO_INGRESO")
	@Temporal(TemporalType.DATE)
	private Date alumnoCursoIngreso;
	
	@Column(name = "ALUMNOCURSO_ACTIVO")
	private Boolean alumnoCursoActivo;
	
	@Column(name = "ALUMNOCURSO_BECA")
	private BigDecimal alumnoCursoBeca;
	
	@Column(name = "ALUMNOCURSO_DESCUENTO")
	private BigDecimal alumnoCursoDescuento;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ALUMNO_ID", insertable=false, updatable=false)
	private Alumnos alumno;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CURSO_ID", insertable=false, updatable=false)
	private Cursos curso;

	public Integer getAlumnoCursoId() {
		return alumnoCursoId;
	}

	public void setAlumnoCursoId(Integer alumnoCursoId) {
		this.alumnoCursoId = alumnoCursoId;
	}

	public Integer getAlumnoId() {
		return alumnoId;
	}

	public void setAlumnoId(Integer alumnoId) {
		this.alumnoId = alumnoId;
	}

	public Integer getCursoId() {
		return cursoId;
	}

	public void setCursoId(Integer cursoId) {
		this.cursoId = cursoId;
	}

	public Date getAlumnoCursoIngreso() {
		return alumnoCursoIngreso;
	}

	public void setAlumnoCursoIngreso(Date alumnoCursoIngreso) {
		this.alumnoCursoIngreso = alumnoCursoIngreso;
	}

	public Boolean getAlumnoCursoActivo() {
		return alumnoCursoActivo;
	}

	public void setAlumnoCursoActivo(Boolean alumnoCursoActivo) {
		this.alumnoCursoActivo = alumnoCursoActivo;
	}

	public BigDecimal getAlumnoCursoBeca() {
		return alumnoCursoBeca;
	}

	public void setAlumnoCursoBeca(BigDecimal alumnoCursoBeca) {
		this.alumnoCursoBeca = alumnoCursoBeca;
	}

	public BigDecimal getAlumnoCursoDescuento() {
		return alumnoCursoDescuento;
	}

	public void setAlumnoCursoDescuento(BigDecimal alumnoCursoDescuento) {
		this.alumnoCursoDescuento = alumnoCursoDescuento;
	}

	public Alumnos getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumnos alumno) {
		this.alumno = alumno;
	}

	public Cursos getCurso() {
		return curso;
	}

	public void setCurso(Cursos curso) {
		this.curso = curso;
	}

}
