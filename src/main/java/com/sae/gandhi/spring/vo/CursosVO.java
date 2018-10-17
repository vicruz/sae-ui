package com.sae.gandhi.spring.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class CursosVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer cursoId;
	private String cursoNombre;
	private LocalDate cursoFechaInicio;
	private LocalDate cursoFechaFin;
	private Date fechaCreacion;
	private Integer cursoStatus;
	private Integer pagoStatus;
	private Integer inscritos;
	
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
	public Integer getPagoStatus() {
		return pagoStatus;
	}
	public void setPagoStatus(Integer pagoStatus) {
		this.pagoStatus = pagoStatus;
	}
	public Integer getInscritos() {
		return inscritos;
	}
	public void setInscritos(Integer inscritos) {
		this.inscritos = inscritos;
	}
	
	@Override
	public String toString() {
		return "CursosVO [cursoId=" + cursoId + ", cursoNombre=" + cursoNombre + ", cursoFechaInicio="
				+ cursoFechaInicio + ", cursoFechaFin=" + cursoFechaFin + ", fechaCreacion=" + fechaCreacion
				+ ", cursoStatus=" + cursoStatus + ", pagoStatus=" + pagoStatus + ", inscritos=" + inscritos + "]";
	}
	
	

}
