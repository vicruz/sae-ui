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
@Table(name="ALUMNO_PAGOS_BITACORA")
public class AlumnoPagosBitacora {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ALUMNOPAGOSBITACORA_ID")
	private Integer alumnoPagosBitacoraId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ALUMNOPAGO_ID", insertable=false, updatable=false)
	private AlumnoPagos alumnoPago;
	
	@Column(name="ALUMNOPAGOSBITACORA_PAGO")
    private BigDecimal alumnoPagosBitacoraPago;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ALUMNOPAGOSBITACORA_FECHA_PAGO")
    private Date alumnoPagosBitacoraFechaPago;
    
    @Column(name="ALUMNOPAGOSBITACORA_SALDO")
    private Boolean alumnoPagosBitacoraSaldo;
    
    @Column(name="ALUMNOPAGOSBITACORA_MONTO_SALDO")
    private BigDecimal alumnoPagosBitacoraMontoSaldo;

	public Integer getAlumnoPagosBitacoraId() {
		return alumnoPagosBitacoraId;
	}

	public void setAlumnoPagosBitacoraId(Integer alumnoPagosBitacoraId) {
		this.alumnoPagosBitacoraId = alumnoPagosBitacoraId;
	}

	public AlumnoPagos getAlumnoPago() {
		return alumnoPago;
	}

	public void setAlumnoPago(AlumnoPagos alumnoPago) {
		this.alumnoPago = alumnoPago;
	}

	public BigDecimal getAlumnoPagosBitacoraPago() {
		return alumnoPagosBitacoraPago;
	}

	public void setAlumnoPagosBitacoraPago(BigDecimal alumnoPagosBitacoraPago) {
		this.alumnoPagosBitacoraPago = alumnoPagosBitacoraPago;
	}

	public Date getAlumnoPagosBitacoraFechaPago() {
		return alumnoPagosBitacoraFechaPago;
	}

	public void setAlumnoPagosBitacoraFechaPago(Date alumnoPagosBitacoraFechaPago) {
		this.alumnoPagosBitacoraFechaPago = alumnoPagosBitacoraFechaPago;
	}

	public Boolean getAlumnoPagosBitacoraSaldo() {
		return alumnoPagosBitacoraSaldo;
	}

	public void setAlumnoPagosBitacoraSaldo(Boolean alumnoPagosBitacoraSaldo) {
		this.alumnoPagosBitacoraSaldo = alumnoPagosBitacoraSaldo;
	}

	public BigDecimal getAlumnoPagosBitacoraMontoSaldo() {
		return alumnoPagosBitacoraMontoSaldo;
	}

	public void setAlumnoPagosBitacoraMontoSaldo(BigDecimal alumnoPagosBitacoraMontoSaldo) {
		this.alumnoPagosBitacoraMontoSaldo = alumnoPagosBitacoraMontoSaldo;
	}
    
}
