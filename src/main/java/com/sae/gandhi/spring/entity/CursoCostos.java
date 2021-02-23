package com.sae.gandhi.spring.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;


@Entity
@Table(name="CURSO_COSTOS")
public class CursoCostos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7570690923137059221L;

	@Id
	@Column(name="CURSOCOSTO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer cursoCostoId;
	
	@Column(name="CURSO_ID")
	@NotNull
	private Integer cursoId;
	
	@Column(name="CURSOCOSTO_DIA_PAGO")
	private Integer cursoCostoDiaPago;
	
	@Column(name="CURSOCOSTO_PAGO")
	private Boolean cursoCostoPagoUnico;
	
	@Column(name="CURSOCOSTO_APLICA_BECA")
	private Boolean cursoCostoAplicaBeca;
	
	@Column(name="CURSOCOSTO_GENERA_ADEUDO")
	private Boolean cursoCostoGeneraAdeudo;
	
	@Column(name="CURSOCOSTO_ACTIVO")
	private Boolean cursoCostoActivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CURSO_ID", insertable=false, updatable=false)
	private Cursos cursos;
	
	@Column(name="FECHA_CREACION")
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
	@Column(name="COSTO_NOMBRE")
	private String costoNombre;
	
	@Column(name="COSTO_MONTO")
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
	public Cursos getCursos() {
		return cursos;
	}
	public void setCursos(Cursos cursos) {
		this.cursos = cursos;
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
