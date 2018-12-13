package com.sae.gandhi.spring.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AlumnoCursoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer alumnoCursoId;
	private Integer alumnoId;
	private Integer cursoId;
	private String cursoNombre;
	private LocalDate cursoFechaInicio;
	private LocalDate cursoFechaFin;
	private LocalDate alumnoCursoFechaIngreso;
//	private LocalDate alumnoCursoFechaLimite;
	private BigDecimal costoMonto;
	private Integer alumnoCursoEstatus;
	private BigDecimal alumnoCursoBeca;
	private BigDecimal alumnoCursoDescuento;
	private Boolean alumnoCursoActivo;
	private Boolean alumnoCursoAplicaBeca;
	private Boolean alumnoCursoAplicaDescuento;
	
	private CursosVO cursoVO;
	
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
	public String getCursoNombre() {
		return cursoNombre;
	}
	public void setCursoNombre(String cursoNombre) {
		this.cursoNombre = cursoNombre;
	}
	public LocalDate getCursoFechaInicio() {
		return cursoFechaInicio;
	}
	public void setCursoFechaInicio(LocalDate cursoFechaInicio) {
		this.cursoFechaInicio = cursoFechaInicio;
	}
	public LocalDate getCursoFechaFin() {
		return cursoFechaFin;
	}
	public void setCursoFechaFin(LocalDate cursoFechaFin) {
		this.cursoFechaFin = cursoFechaFin;
	}
	public LocalDate getAlumnoCursoFechaIngreso() {
		return alumnoCursoFechaIngreso;
	}
	public void setAlumnoCursoFechaIngreso(LocalDate alumnoCursoFechaIngreso) {
		this.alumnoCursoFechaIngreso = alumnoCursoFechaIngreso;
	}
	/*public LocalDate getAlumnoCursoFechaLimite() {
		return alumnoCursoFechaLimite;
	}
	public void setAlumnoCursoFechaLimite(LocalDate alumnoCursoFechaLimite) {
		this.alumnoCursoFechaLimite = alumnoCursoFechaLimite;
	}*/
	public BigDecimal getCostoMonto() {
		return costoMonto;
	}
	public void setCostoMonto(BigDecimal costoMonto) {
		this.costoMonto = costoMonto;
	}
	public Integer getAlumnoCursoEstatus() {
		return alumnoCursoEstatus;
	}
	public void setAlumnoCursoEstatus(Integer alumnoCursoEstatus) {
		this.alumnoCursoEstatus = alumnoCursoEstatus;
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
	public Boolean getAlumnoCursoActivo() {
		return alumnoCursoActivo;
	}
	public void setAlumnoCursoActivo(Boolean alumnoCursoActivo) {
		this.alumnoCursoActivo = alumnoCursoActivo;
	}
	public CursosVO getCursoVO() {
		return cursoVO;
	}
	public void setCursoVO(CursosVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	public Boolean getAlumnoCursoAplicaBeca() {
		return alumnoCursoAplicaBeca;
	}
	public void setAlumnoCursoAplicaBeca(Boolean alumnoCursoAplicaBeca) {
		this.alumnoCursoAplicaBeca = alumnoCursoAplicaBeca;
	}
	public Boolean getAlumnoCursoAplicaDescuento() {
		return alumnoCursoAplicaDescuento;
	}
	public void setAlumnoCursoAplicaDescuento(Boolean alumnoCursoAplicaDescuento) {
		this.alumnoCursoAplicaDescuento = alumnoCursoAplicaDescuento;
	}

}
