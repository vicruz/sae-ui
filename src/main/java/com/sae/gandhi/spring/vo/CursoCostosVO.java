package com.sae.gandhi.spring.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class CursoCostosVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer cursoCostoId;
	private Integer cursoId;
	private Integer costoId;
	private Integer cursoCostoDiaPago;
	private Boolean cursoCostoPagoUnico;
	private Boolean cursoCostoAplicaBeca;
	private Boolean cursoCostoGeneraAdeudo;
	private Boolean cursoCostoActivo;
	private Date fechaCreacion;
	private CostosVO costosVO; //para bindeo desde el combobox
	private String costoNombre;
	private BigDecimal costoMonto;
	
	public Integer getCursoCostoId() {
		return cursoCostoId;
	}
	public void setCursoCostoId(Integer cursoCostoId) {
		this.cursoCostoId = cursoCostoId;
	}
	public Integer getCursoId() {
		return cursoId;
	}
	public void setCursoId(Integer cursoId) {
		this.cursoId = cursoId;
	}
	public Integer getCostoId() {
		return costoId;
	}
	public void setCostoId(Integer costoId) {
		this.costoId = costoId;
	}
	public Integer getCursoCostoDiaPago() {
		return cursoCostoDiaPago;
	}
	public void setCursoCostoDiaPago(Integer cursoCostoDiaPago) {
		this.cursoCostoDiaPago = cursoCostoDiaPago;
	}
	
	public Boolean getCursoCostoPagoUnico() {
		return cursoCostoPagoUnico;
	}
	public void setCursoCostoPagoUnico(Boolean cursoCostoPagoUnico) {
		this.cursoCostoPagoUnico = cursoCostoPagoUnico;
	}
	public Boolean getCursoCostoAplicaBeca() {
		return cursoCostoAplicaBeca;
	}
	public void setCursoCostoAplicaBeca(Boolean cursoCostoAplicaBeca) {
		this.cursoCostoAplicaBeca = cursoCostoAplicaBeca;
	}
	public Boolean getCursoCostoGeneraAdeudo() {
		return cursoCostoGeneraAdeudo;
	}
	public void setCursoCostoGeneraAdeudo(Boolean cursoCostoGeneraAdeudo) {
		this.cursoCostoGeneraAdeudo = cursoCostoGeneraAdeudo;
	}
	public Boolean getCursoCostoActivo() {
		return cursoCostoActivo;
	}
	public void setCursoCostoActivo(Boolean cursoCostoActivo) {
		this.cursoCostoActivo = cursoCostoActivo;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public CostosVO getCostosVO() {
		return costosVO;
	}
	public void setCostosVO(CostosVO costosVO) {
		this.costosVO = costosVO;
	}
	public String getCostoNombre() {
		return costoNombre;
	}
	public void setCostoNombre(String costoNombre) {
		this.costoNombre = costoNombre;
	}
	public BigDecimal getCostoMonto() {
		return costoMonto;
	}
	public void setCostoMonto(BigDecimal costoMonto) {
		this.costoMonto = costoMonto;
	}
	
}
