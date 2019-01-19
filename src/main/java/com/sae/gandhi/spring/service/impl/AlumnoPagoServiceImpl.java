package com.sae.gandhi.spring.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoPagoBitacoraDAO;
import com.sae.gandhi.spring.dao.AlumnoPagoDAO;
import com.sae.gandhi.spring.entity.AlumnoPagos;
import com.sae.gandhi.spring.entity.AlumnoPagosBitacora;
import com.sae.gandhi.spring.service.AlumnoPagoService;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.utils.builder.AlumnoPagoBuilder;
import com.sae.gandhi.spring.vo.AlumnoPagoVO;

@Transactional
@Service
public class AlumnoPagoServiceImpl implements AlumnoPagoService {
	
	@Autowired
	AlumnoPagoDAO alumnoPagoDAO;
	
	@Autowired
	AlumnoPagoBitacoraDAO alumnoPagoBitacoraDAO;

	@Override
	public List<AlumnoPagoVO> findByAlumnoId(Integer alumnoId) {
		List<AlumnoPagos> lst = alumnoPagoDAO.findByAlumnoId(alumnoId);
		return AlumnoPagoBuilder.createListAlumnoPagoVO(lst);
	}

	@Override
	public List<AlumnoPagoVO> findByAlumnoCursoId(Integer alumnoCursoId) {
		List<AlumnoPagos> lst = alumnoPagoDAO.findByAlumnoCursoId(alumnoCursoId);
		return AlumnoPagoBuilder.createListAlumnoPagoVO(lst);
	}

	@Override
	public List<AlumnoPagoVO> findByAlumnoIdAndCursoId(Integer alumnoId, Integer cursoId) {
		List<AlumnoPagos> lst = alumnoPagoDAO.findByAlumnoIdAndCursoId(alumnoId, cursoId);
		return AlumnoPagoBuilder.createListAlumnoPagoVO(lst);
	}

	@Override
	public AlumnoPagoVO update(AlumnoPagoVO vo) {
		return null;
	}

	@Override
	public AlumnoPagoVO updateFecha(AlumnoPagoVO vo) {
		AlumnoPagos alumnoPago = alumnoPagoDAO.findById(vo.getAlumnoPagoId()).get();
		alumnoPago.setAlumnoPagoFechaLimite(SaeDateUtils.localDateToDate(vo.getAlumnoPagoFechaLimite()));
		
		//TODO Cambiar el estatus del pago a preparado y modificar el monto del pago dependiendo de la fecha límite
		return vo;
	}

	@Override
	public AlumnoPagoVO save(AlumnoPagoVO vo) {
		
		AlumnoPagosBitacora alumnoPagoBitacora = new AlumnoPagosBitacora();
		alumnoPagoBitacora.setAlumnopagoId(vo.getAlumnoPagoId());
		alumnoPagoBitacora.setAlumnoPagosBitacoraFechaPago(
				SaeDateUtils.localDateToDate(vo.getAlumnoPagoFechaPago()));
		alumnoPagoBitacora.setAlumnoPagosBitacoraPago(vo.getAlumnoPagoPago());
		//TODO Falta almacenar el saldo y saber si el pago se realizó con saldo y el monto del saldo utilizado
		
		//Almacenar el pago
		alumnoPagoBitacoraDAO.save(alumnoPagoBitacora);
		
		//Actualizar el pago
		Optional<AlumnoPagos> alumnoPago = alumnoPagoDAO.findById(vo.getAlumnoPagoId());
		if(alumnoPago.isPresent()){
			alumnoPago.get().setAlumnoPagoPago(alumnoPago.get().getAlumnoPagoPago().add(vo.getAlumnoPagoPago()));
			alumnoPago.get().setAlumnoPagoFechaPago(alumnoPagoBitacora.getAlumnoPagosBitacoraFechaPago());
			
			if(alumnoPago.get().getAlumnoPagoPago().compareTo(alumnoPago.get().getAlumnoPagoMonto())==0)
				alumnoPago.get().setAlumnoPagoEstatus(SaeEnums.Pago.COMPLETO.getStatusId());
			else
				alumnoPago.get().setAlumnoPagoEstatus(SaeEnums.Pago.PARCIAL.getStatusId());
		}
		
		return AlumnoPagoBuilder.createAlumnoPagoVO(alumnoPago.get());		
	}


}
