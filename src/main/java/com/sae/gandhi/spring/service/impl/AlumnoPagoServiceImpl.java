package com.sae.gandhi.spring.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoPagoBitacoraDAO;
import com.sae.gandhi.spring.dao.AlumnoPagoDAO;
import com.sae.gandhi.spring.dao.AlumnosDAO;
import com.sae.gandhi.spring.entity.AlumnoPagos;
import com.sae.gandhi.spring.entity.AlumnoPagosBitacora;
import com.sae.gandhi.spring.entity.Alumnos;
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
	
	@Autowired
	AlumnosDAO alumnosDAO;

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
	public AlumnoPagoVO save(AlumnoPagoVO vo, Integer alumnoId, BigDecimal alumnoSaldo) {
		
		AlumnoPagosBitacora alumnoPagoBitacora = new AlumnoPagosBitacora();
		Optional<AlumnoPagos> alumnoPago = alumnoPagoDAO.findById(vo.getAlumnoPagoId());
		AlumnoPagos alumnoPagos;
		BigDecimal montoSaldo = BigDecimal.ZERO;
		
		if(alumnoPago.isPresent()){
			alumnoPagos = alumnoPago.get();
			alumnoPagoBitacora.setAlumnopagoId(vo.getAlumnoPagoId());
			alumnoPagoBitacora.setAlumnoPagosBitacoraFechaPago(
					SaeDateUtils.localDateToDate(vo.getAlumnoPagoFechaPago()));
			alumnoPagoBitacora.setAlumnoPagosBitacoraPago(vo.getAlumnoPagoPago());
			
			//Calculos del saldo
			//saber si el pago se realizó con saldo y el monto del saldo utilizado
			if(vo.getUsaSaldo() && alumnoSaldo.compareTo(BigDecimal.ZERO)>0){
				Optional<Alumnos> optional = alumnosDAO.findById(alumnoId);
				Alumnos alumno = optional.get();
				BigDecimal tmp = alumnoPagos.getAlumnoPagoMonto().subtract(vo.getAlumnoPagoPago());
				
				//Si el valor tmp es mayor al saldo, el monto utilizado será el saldo completo
				if(tmp.compareTo(alumnoSaldo)>=0){
					montoSaldo = alumnoSaldo;
				}
				//En caso contrario, el saldo utilizado será el del tmp
				else{
					montoSaldo = tmp;
				}
				
				//Actualizacion del saldo en el alumno
				alumno.setAlumnoSaldo(alumno.getAlumnoSaldo().subtract(montoSaldo));
			}
			alumnoPagoBitacora.setAlumnoPagosBitacoraSaldo(montoSaldo);

			//Almacenar el pago
			alumnoPagoBitacoraDAO.save(alumnoPagoBitacora);

			//Actualizar el pago
			alumnoPagos.setAlumnoPagoPago(alumnoPago.get().getAlumnoPagoPago().add(vo.getAlumnoPagoPago()).add(montoSaldo));
			alumnoPagos.setAlumnoPagoFechaPago(alumnoPagoBitacora.getAlumnoPagosBitacoraFechaPago());
			
			if(alumnoPagos.getAlumnoPagoPago().compareTo(alumnoPago.get().getAlumnoPagoMonto())==0)
				alumnoPagos.setAlumnoPagoEstatus(SaeEnums.Pago.COMPLETO.getStatusId());
			else
				alumnoPagos.setAlumnoPagoEstatus(SaeEnums.Pago.PARCIAL.getStatusId());
		}
		
		return AlumnoPagoBuilder.createAlumnoPagoVO(alumnoPago.get());		
	}


}
