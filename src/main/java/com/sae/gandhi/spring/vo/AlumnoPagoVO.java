package com.sae.gandhi.spring.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AlumnoPagoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer alumnoPagoId;
	private Integer alumnoCursoId;
	private Integer cursoCostosId;
	private Integer cursoId;
	
	private String cursoNombre;
	private BigDecimal alumnoPagoMonto;
	private BigDecimal alumnoPagoPago;
	private LocalDate alumnoPagoFechaLimite;
	private LocalDate alumnoPagoFechaPago;
	private Integer estatusId;
	private String costoNombre;
	private Boolean usaSaldo;
	
	public Integer getAlumnoPagoId() {
		return alumnoPagoId;
	}
	public void setAlumnoPagoId(Integer alumnoPagoId) {
		this.alumnoPagoId = alumnoPagoId;
	}
	public Integer getAlumnoCursoId() {
		return alumnoCursoId;
	}
	public void setAlumnoCursoId(Integer alumnoCursoId) {
		this.alumnoCursoId = alumnoCursoId;
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
	public BigDecimal getAlumnoPagoMonto() {
		return alumnoPagoMonto;
	}
	public void setAlumnoPagoMonto(BigDecimal alumnoPagoMonto) {
		this.alumnoPagoMonto = alumnoPagoMonto;
	}
	public BigDecimal getAlumnoPagoPago() {
		return alumnoPagoPago;
	}
	public void setAlumnoPagoPago(BigDecimal alumnoPagoPago) {
		this.alumnoPagoPago = alumnoPagoPago;
	}
	public LocalDate getAlumnoPagoFechaLimite() {
		return alumnoPagoFechaLimite;
	}
	public void setAlumnoPagoFechaLimite(LocalDate alumnoPagoFechaLimite) {
		this.alumnoPagoFechaLimite = alumnoPagoFechaLimite;
	}
	public LocalDate getAlumnoPagoFechaPago() {
		return alumnoPagoFechaPago;
	}
	public void setAlumnoPagoFechaPago(LocalDate alumnoPagoFechaPago) {
		this.alumnoPagoFechaPago = alumnoPagoFechaPago;
	}
	public Integer getEstatusId() {
		return estatusId;
	}
	public void setEstatusId(Integer estatusId) {
		this.estatusId = estatusId;
	}
	public Integer getCursoCostosId() {
		return cursoCostosId;
	}
	public void setCursoCostosId(Integer cursoCostosId) {
		this.cursoCostosId = cursoCostosId;
	}
	public String getCostoNombre() {
		return costoNombre;
	}
	public void setCostoNombre(String costoNombre) {
		this.costoNombre = costoNombre;
	}
	public Boolean getUsaSaldo() {
		return usaSaldo;
	}
	public void setUsaSaldo(Boolean usaSaldo) {
		this.usaSaldo = usaSaldo;
	}
	
}
