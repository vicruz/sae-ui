package com.sae.gandhi.spring.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="ALUMNO_PAGOS")
public class AlumnoPagos implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8186354188743614885L;

	@Id
	@Column(name = "ALUMNOPAGO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer alumnoPagoId;
	
	@Column(name="ALUMNOCURSO_ID")
	private Integer alumnoCursoId;
	
	@Column(name="CURSOCOSTO_ID")
	private Integer cursoCostoId;
	
	@Column(name="ALUMNOPAGO_MONTO")
	private BigDecimal alumnoPagoMonto;
	
	@Column(name="ALUMNOPAGO_PAGO")
	private BigDecimal alumnoPagoPago;
	
	@Column(name="ALUMNOPAGO_FECHA_LIMITE")
	private Date alumnoPagoFechaLimite;
	
	@Column(name="ALUMNOPAGO_FECHAPAGO")
	private Date alumnoPagoFechaPago;
	
	@Column(name="ALUMNOPAGO_ESTATUS")
	private Integer alumnoPagoEstatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ALUMNOCURSO_ID", insertable=false, updatable=false)
	private AlumnoCurso alumnoCurso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CURSOCOSTO_ID", insertable=false, updatable=false)
	private CursoCostos cursoCostos;
	
	@OneToMany(mappedBy="alumnoPago", fetch = FetchType.LAZY)
	private List<AlumnoPagosBitacora> lstAlumnoPagosBitacora;
	
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

	public Date getAlumnoPagoFechaLimite() {
		return alumnoPagoFechaLimite;
	}

	public void setAlumnoPagoFechaLimite(Date alumnoPagoFechaLimite) {
		this.alumnoPagoFechaLimite = alumnoPagoFechaLimite;
	}

	public Date getAlumnoPagoFechaPago() {
		return alumnoPagoFechaPago;
	}

	public void setAlumnoPagoFechaPago(Date alumnoPagoFechaPago) {
		this.alumnoPagoFechaPago = alumnoPagoFechaPago;
	}

	public Integer getAlumnoPagoEstatus() {
		return alumnoPagoEstatus;
	}

	public void setAlumnoPagoEstatus(Integer alumnoPagoEstatus) {
		this.alumnoPagoEstatus = alumnoPagoEstatus;
	}

	public AlumnoCurso getAlumnoCurso() {
		return alumnoCurso;
	}

	public void setAlumnoCurso(AlumnoCurso alumnoCurso) {
		this.alumnoCurso = alumnoCurso;
	}

	public Integer getCursoCostoId() {
		return cursoCostoId;
	}

	public void setCursoCostoId(Integer cursoCostoId) {
		this.cursoCostoId = cursoCostoId;
	}

	public CursoCostos getCursoCostos() {
		return cursoCostos;
	}

	public void setCursoCostos(CursoCostos cursoCostos) {
		this.cursoCostos = cursoCostos;
	}

	public List<AlumnoPagosBitacora> getLstAlumnoPagosBitacora() {
		return lstAlumnoPagosBitacora;
	}

	public void setLstAlumnoPagosBitacora(List<AlumnoPagosBitacora> lstAlumnoPagosBitacora) {
		this.lstAlumnoPagosBitacora = lstAlumnoPagosBitacora;
	}
	
}
