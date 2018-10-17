package com.sae.gandhi.spring.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="COSTOS")
public class Costos {

	@Id
	@Column(name="COSTO_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@NotNull
	private Integer costoId;
	
	@Column(name="COSTO_NOMBRE")
	@Size(max=255)
	private String costoNombre;
	
	@Column(name="COSTO_MONTO")
	private BigDecimal costoMonto;
	
	@Column(name="FECHA_CREACION")
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
	@Column(name="COSTO_ACTIVO")
	private Boolean costoActivo;
	
	public Integer getCostoId() {
		return costoId;
	}
	public void setCostoId(Integer costoId) {
		this.costoId = costoId;
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
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Boolean getCostoActivo() {
		return costoActivo;
	}
	public void setCostoActivo(Boolean costoActivo) {
		this.costoActivo = costoActivo;
	}
	
}
