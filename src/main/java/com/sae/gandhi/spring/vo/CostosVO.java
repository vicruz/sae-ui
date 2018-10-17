package com.sae.gandhi.spring.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class CostosVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer costoId;
	private String costoNombre;
	private BigDecimal costoMonto;
	private Date fechaCreacion;
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
	@Override
	public String toString() {
		return "CostosDTO [costoId=" + costoId + ", costoNombre=" + costoNombre + ", costoMonto=" + costoMonto
				+ ", fechaCreacion=" + fechaCreacion + ", costoActivo=" + costoActivo + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((costoId == null) ? 0 : costoId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CostosVO other = (CostosVO) obj;
		if (costoId == null) {
			if (other.costoId != null)
				return false;
		} else if (!costoId.equals(other.costoId))
			return false;
		return true;
	}
	
	

}
