package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.AlumnoPagosBitacora;
import com.sae.gandhi.spring.entity.vo.AlumnoPagoBitacoraVO;

public interface AlumnoPagoBitacoraDAO extends JpaRepository<AlumnoPagosBitacora, Integer> {

	/*
	select curso.CURSO_NOMBRE,costo.COSTO_NOMBRE,ap.ALUMNOPAGO_FECHA_LIMITE, ap.ALUMNOPAGO_ID, ap.ALUMNOPAGO_MONTO as monto, apb.ALUMNOPAGOSBITACORA_PAGO+apb.ALUMNOPAGOSBITACORA_SALDO as pago, 
	apb.ALUMNOPAGOSBITACORA_FECHA_PAGO as fecha_pago, ap.ALUMNOPAGO_ESTATUS 
	from alumno_pagos ap
	join alumno_curso ac on ap.ALUMNOCURSO_ID = ac.ALUMNOCURSO_ID
	join curso_costos cc on ap.CURSOCOSTO_ID = cc.CURSOCOSTO_ID
	join costos costo on cc.COSTO_ID = costo.COSTO_ID
	join cursos curso on ac.CURSO_ID = curso.CURSO_ID
	left join alumno_pagos_bitacora apb on ap.ALUMNOPAGO_ID = apb.ALUMNOPAGO_ID
	where ac.ALUMNO_ID = 1
	and ac.CURSO_ID = 1;
	 */
	
	//Encuentra los pagos por alumno pago
	@Query("Select new com.sae.gandhi.spring.entity.vo.AlumnoPagoBitacoraVO(curso.cursoNombre, cc.costoNombre, ap.alumnoPagoFechaLimite, "
			+ "ap.alumnoPagoMonto, apb.alumnoPagosBitacoraFechaPago, apb.alumnoPagosBitacoraPago, apb.alumnoPagosBitacoraSaldo, ap.alumnoPagoEstatus) "
			+ "from AlumnoPagos ap "
			+ "join ap.alumnoCurso ac "
			+ "join ap.cursoCostos cc "
			+ "join cc.cursos curso "
			+ "left join ap.lstAlumnoPagosBitacora apb "
			+ "where ac.alumnoId = ?1 order by ac.alumnoCursoId, ap.alumnoPagoId")
	public List<AlumnoPagoBitacoraVO> findByAlumnoId(Integer idAlumnoPago);
	
	//Encuentra los pagos por alumno pago
	@Query("Select new com.sae.gandhi.spring.entity.vo.AlumnoPagoBitacoraVO(curso.cursoNombre, cc.costoNombre, ap.alumnoPagoFechaLimite, "
			+ "ap.alumnoPagoMonto, apb.alumnoPagosBitacoraFechaPago, apb.alumnoPagosBitacoraPago, apb.alumnoPagosBitacoraSaldo, ap.alumnoPagoEstatus) "
			+ "from AlumnoPagos ap "
			+ "join ap.alumnoCurso ac "
			+ "join ap.cursoCostos cc "
			+ "join cc.cursos curso "
			+ "left join ap.lstAlumnoPagosBitacora apb "
			+ "where ac.alumnoId = ?1 and ac.cursoId = ?2 order by ac.alumnoCursoId, ap.alumnoPagoId")
	public List<AlumnoPagoBitacoraVO> findByAlumnoIdAndCursoId(Integer alumnoId, Integer cursoId);
	
	public List<AlumnoPagosBitacora> findByAlumnopagoIdOrderByAlumnoPagosBitacoraId(Integer alumnopagoId);
	
}
