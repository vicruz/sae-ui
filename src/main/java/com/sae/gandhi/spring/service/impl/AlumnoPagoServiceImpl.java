package com.sae.gandhi.spring.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
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
	public AlumnoPagoVO updateFechaMonto(AlumnoPagoVO vo) {
		AlumnoPagos alumnoPago = alumnoPagoDAO.findById(vo.getAlumnoPagoId()).get();
		alumnoPago.setAlumnoPagoFechaLimite(SaeDateUtils.localDateToDate(vo.getAlumnoPagoFechaLimite()));
		alumnoPago.setAlumnoPagoMonto(vo.getAlumnoPagoMonto());
		alumnoPago.setAlumnoPagoEstatus(SaeEnums.Pago.PREPARADO.getStatusId());
		
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

	@Override
	public void delete(AlumnoPagoVO vo) {
		if(vo.getAlumnoPagoPago()!=null && vo.getAlumnoPagoPago().compareTo(BigDecimal.ZERO)>0){
			return;
		}
		alumnoPagoDAO.deleteById(vo.getAlumnoPagoId());
		
	}

	@Override
	public void updateMontoFechaExceed() {
		Calendar fechaActual = Calendar.getInstance();
		//Solo busca los pagos que hayan excedido la fecha límite y se encuentren en "pendiente" o "Adeudo
		//Pagos que tenga activo el campo "Genera Adeudo"
		List<AlumnoPagos> lstAlumno = alumnoPagoDAO.findPagoLimitExceed(SaeEnums.Pago.PREPARADO.getStatusId(),
				SaeEnums.Pago.ADEUDO.getStatusId(),fechaActual.getTime());
		
		int mesesDiff;
		BigDecimal montoOriginal;
		BigDecimal montoCalculado;
		int porcentaje;
		Alumnos alumno;
		
		for(AlumnoPagos alumnoPagos : lstAlumno){
			mesesDiff = SaeDateUtils.calcularMesesAFecha(alumnoPagos.getAlumnoPagoFechaLimite(), fechaActual.getTime());
			
			//Se agrega un mes a la diferencia de meses ya que si solo ha pasado 1 día de retardo no
			//se hará modificación alguna
			mesesDiff +=1;
			
			//////////////////////////////////
			//Calculo del % a sumar al pago
			/////////////////////////////////
			//Monto original
			montoOriginal = alumnoPagos.getCursoCostos().getCostos().getCostoMonto();
			
			//Porcentaje
			porcentaje = mesesDiff * 10;
			
			//Monto calculado
			montoCalculado = montoOriginal.add(montoOriginal.multiply(BigDecimal.valueOf(porcentaje))
					.divide(BigDecimal.valueOf(100),RoundingMode.HALF_UP));
			
			//Nuevo monto
			alumnoPagos.setAlumnoPagoMonto(montoCalculado);
			alumnoPagos.setAlumnoPagoEstatus(SaeEnums.Pago.ADEUDO.getStatusId());
			
			//Se agrega el Id del alumno modificado a la lista para posteriormente modificar su estatus
			alumno = alumnoPagos.getAlumnoCurso().getAlumno();
			alumno.setAlumnoEstatus(SaeEnums.Pago.ADEUDO.getStatusId());
			
			alumnosDAO.save(alumno);
			alumnoPagoDAO.save(alumnoPagos);
		}
		
	}


}
