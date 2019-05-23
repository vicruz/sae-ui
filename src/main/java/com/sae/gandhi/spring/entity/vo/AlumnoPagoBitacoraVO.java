package com.sae.gandhi.spring.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

public class AlumnoPagoBitacoraVO {

	private String curso;
	private String concepto;
	private Date fechaLimite;
	private BigDecimal monto;
	private Date fechaPago;
	private BigDecimal pago;
	private BigDecimal saldo;
	private Integer estatus;

    public AlumnoPagoBitacoraVO(){    }

	public AlumnoPagoBitacoraVO(String curso, String concepto, Date fechaLimite, BigDecimal monto, Date fechaPago,
			BigDecimal pago, BigDecimal saldo, Integer estatus) {
		super();
		this.curso = curso;
		this.concepto = concepto;
		this.fechaLimite = fechaLimite;
		this.monto = monto;
		this.fechaPago = fechaPago;
		this.pago = pago;
		this.saldo = saldo;
		this.estatus = estatus;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Date getFechaLimite() {
		return fechaLimite;
	}

	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public BigDecimal getPago() {
		return pago;
	}

	public void setPago(BigDecimal pago) {
		this.pago = pago;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public Integer getEstatus() {
		return estatus;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}
    
}
