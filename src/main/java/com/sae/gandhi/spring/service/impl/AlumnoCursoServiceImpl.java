package com.sae.gandhi.spring.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoCursoDAO;
import com.sae.gandhi.spring.dao.AlumnoPagoBitacoraDAO;
import com.sae.gandhi.spring.dao.AlumnoPagoDAO;
import com.sae.gandhi.spring.dao.AlumnosDAO;
import com.sae.gandhi.spring.dao.CursoCostosDAO;
import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.AlumnoPagos;
import com.sae.gandhi.spring.entity.Alumnos;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.utils.SaeConstants;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.utils.builder.AlumnoCursoBuilder;
import com.sae.gandhi.spring.utils.builder.AlumnosBuilder;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;
import com.sae.gandhi.spring.vo.AlumnosVO;

@Service
@Transactional
public class AlumnoCursoServiceImpl implements AlumnoCursoService {
	
	private static final BigDecimal CIEN = new BigDecimal(100);
	private static final Integer DECIMALES = 2;
	
	@Autowired
	private AlumnoCursoDAO alumnoCursoDAO;
	
	@Autowired
	private CursoCostosDAO cursoCostosDAO;
	
	@Autowired
	private AlumnoPagoDAO alumnoPagoDAO;
	
	@Autowired
	private AlumnosDAO alumnosDAO;
	
	@Autowired
	private AlumnoPagoBitacoraDAO alumnoPagoBitacoraDAO;
	
	@Override
	public void save(AlumnoCursoVO alumnoCursoVO) {
		AlumnoCurso alumnoCurso = new AlumnoCurso();
		Calendar cal = Calendar.getInstance();
		
		alumnoCurso.setAlumnoCursoActivo(true);
		alumnoCurso.setAlumnoCursoBeca(alumnoCursoVO.getAlumnoCursoBeca());
		alumnoCurso.setAlumnoCursoDescuento(alumnoCursoVO.getAlumnoCursoDescuento());
		alumnoCurso.setAlumnoCursoIngreso(
				SaeDateUtils.localDateToDate(alumnoCursoVO.getAlumnoCursoFechaIngreso()));
		alumnoCurso.setAlumnoId(alumnoCursoVO.getAlumnoId());
		
		if(alumnoCursoVO.getCursoVO()!=null){
			alumnoCurso.setCursoId(alumnoCursoVO.getCursoVO().getCursoId());			
		}else{
			alumnoCurso.setCursoId(alumnoCursoVO.getCursoId());
		}
		
		//Si no hay fecha, se pone la fecha actual
		if(alumnoCurso.getAlumnoCursoIngreso()==null){
			alumnoCurso.setAlumnoCursoIngreso(cal.getTime());
		}
		
		alumnoCurso = alumnoCursoDAO.save(alumnoCurso);
		
		//////////////////////////////////////
		//Crear los costos asociados al curso
		List<CursoCostos> lstCursoCostos = 
				cursoCostosDAO.findByCursoIdAndCursoCostoActivo(alumnoCurso.getCursoId(), true);
		
		for (CursoCostos cursoCostos : lstCursoCostos) {
			Date initDate = SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaInicio());
			
			createStudentPayment(cursoCostos, alumnoCurso, initDate, 
					SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaFin()));
		}
	}

	/**
	 * Actualiza el % de beca o el descuento
	 */
	@Override
	public void update(AlumnoCursoVO alumnoCursoVO) {
		AlumnoCurso alumnoCurso;
		Alumnos alumno;
		Optional<AlumnoCurso> optional;
		Optional<Alumnos> alumnoOptional;
		BigDecimal saldo = BigDecimal.ZERO;
		Date fechaFin;
		int mesesDiff;
		int porcentaje;
		
		optional = alumnoCursoDAO.findById(alumnoCursoVO.getAlumnoCursoId());
		alumnoOptional = alumnosDAO.findById(alumnoCursoVO.getAlumnoId());
		
		if(!optional.isPresent() || !alumnoOptional.isPresent()){
			//TODO enviar mensaje de error indicando que no existe el curso en el alumno
			return;
		}

		alumnoCurso = optional.get();
		alumno = alumnoOptional.get();
		saldo = alumno.getAlumnoSaldo();
		
		//Si la beca y descuento son iguales, no realiza ninguna acción de evaluación
		BigDecimal becaBase = alumnoCurso.getAlumnoCursoBeca()==null?BigDecimal.ZERO:alumnoCurso.getAlumnoCursoBeca();
		BigDecimal becaEdicion = alumnoCursoVO.getAlumnoCursoBeca()==null?BigDecimal.ZERO:alumnoCursoVO.getAlumnoCursoBeca();
		BigDecimal descuentoBase = alumnoCurso.getAlumnoCursoDescuento()==null?BigDecimal.ZERO:alumnoCurso.getAlumnoCursoDescuento();
		BigDecimal descuentoEdicion = alumnoCursoVO.getAlumnoCursoDescuento()==null?BigDecimal.ZERO:alumnoCursoVO.getAlumnoCursoDescuento();
		if( becaBase.compareTo(becaEdicion)==0 && descuentoBase.compareTo(descuentoEdicion)==0){
			return;
		}

		//TODO guardar bitácora de datos previos
		alumnoCurso.setAlumnoCursoBeca(alumnoCursoVO.getAlumnoCursoBeca());
		alumnoCurso.setAlumnoCursoDescuento(alumnoCursoVO.getAlumnoCursoDescuento());
		
		//Obtener los costos en los que aplicará la beca/descuento
		List<CursoCostos> lstCursoCostos = cursoCostosDAO.findByAlumnoCursoIdAndBeca(alumnoCurso.getAlumnoCursoId());
		
		for(CursoCostos cursoCosto : lstCursoCostos){
			//Obtener el monto a descontar
			BigDecimal montoDescuento = BigDecimal.ZERO;
			BigDecimal montoTotal = BigDecimal.ZERO;
			
			//Obtener los alumnosPagos que correspondan al curso costo y descontar el monto al valor total
			List<AlumnoPagos> lstAlumnoPago = alumnoPagoDAO.findByAlumnoCursoIdAndCursoCostoId(alumnoCurso.getAlumnoCursoId(), cursoCosto.getCursoCostoId());
			for(AlumnoPagos alumnoPago : lstAlumnoPago){

				//Se ajusta el monto a pagar
				//1. Se calcula el porcentaje de adeudo con el monto total, este monto se calcula con la fecha limite y
				//	1.1. Fecha de 1er pago obtenida de la bitacora, si no existe
				//	1.2. Fecha actual
				if(alumnoPago.getAlumnoPagoFechaPago()!=null) {
					//No debe fallar ya que si existe un pago, existe una bitacora del momento en que se hizo
					fechaFin = alumnoPagoBitacoraDAO.findByAlumnopagoIdOrderByAlumnoPagosBitacoraId(alumnoPago.getAlumnoPagoId()).get(0).getAlumnoPagosBitacoraFechaPago();
				}else {
					fechaFin = Calendar.getInstance().getTime();
				}
				mesesDiff = SaeDateUtils.calcularMesesAFecha(alumnoPago.getAlumnoPagoFechaLimite(), fechaFin);
				mesesDiff +=1;
				
				//Ajuste de beca/descuento
				montoTotal = getBecaDiscountAmount(cursoCosto.getCostoMonto(), becaEdicion, descuentoEdicion); 
				
				//Porcentaje
				porcentaje = mesesDiff * 10;
				
				//Monto calculado
				montoDescuento = montoTotal.add(montoTotal.multiply(BigDecimal.valueOf(porcentaje))
						.divide(BigDecimal.valueOf(100),RoundingMode.HALF_UP));
				
				alumnoPago.setAlumnoPagoMonto(montoDescuento);
				
				//Solo se realizaran operaciones si se ha realizado un pago
				if(alumnoPago.getAlumnoPagoPago().compareTo(BigDecimal.ZERO)==0){
					continue;
				}
				
				//El saldo solamente se ajustará si el pago realizado es mayor al monto a cobrar
				//y es de una beca (Angelica menciona que el descuento no genera saldo a favor '20200210')
				if(alumnoPago.getAlumnoPagoPago().compareTo(alumnoPago.getAlumnoPagoMonto())>0 && alumnoCursoVO.getAlumnoCursoBeca()!=null)
					saldo = saldo.add(alumnoPago.getAlumnoPagoPago().subtract(alumnoPago.getAlumnoPagoMonto()));
				
				//Modificar etiqueta de pago
				if(alumnoPago.getAlumnoPagoPago().compareTo(BigDecimal.ZERO)==0){
					//Se mantiene el estatus 1 (PARCIAL)
					continue;
				}else if(alumnoPago.getAlumnoPagoPago().compareTo(alumnoPago.getAlumnoPagoMonto())>=0 && 
						alumnoPago.getAlumnoPagoEstatus().intValue()!=SaeEnums.Pago.COMPLETO.getStatusId()){
					//Se establece el pago 2 (COMPLETO)
					alumnoPago.setAlumnoPagoEstatus(SaeEnums.Pago.COMPLETO.getStatusId());
					continue;
				}else if(alumnoPago.getAlumnoPagoPago().compareTo(alumnoPago.getAlumnoPagoMonto())<0 &&
						alumnoPago.getAlumnoPagoEstatus().intValue()!=SaeEnums.Pago.PARCIAL.getStatusId()){
					//Se establece el pago 3 (PARCIAL)
					alumnoPago.setAlumnoPagoEstatus(SaeEnums.Pago.PARCIAL.getStatusId());
					continue;
					
				}
			}
		}
		
		alumno.setAlumnoSaldo(saldo);
	}

	/**
	 * Da de baja un curso del alumno. Poniendo el estatus de los pagos y curso en BAJA/CANCELADO  
	 */
	@Override
	public boolean delete(Integer alumnoCursoId) {
		//borrar de alumnoPagos todos los pagos excepto si
		alumnoPagoDAO.updateStatusPayments(alumnoCursoId, SaeEnums.Pago.CANCELADO.getStatusId());
		alumnoCursoDAO.updateStatusCurso(false, alumnoCursoId);
		
		return true;
	}

	@Override
	public AlumnoCursoVO findById(Integer alumnoCursoId) {
		// TODO Auto-generated method stub
		return null;
	}

	//Se usa para obtener los cursos a los que esta inscrito el alumno
	@Override
	public List<AlumnoCursoVO> findByStudent(Integer alumnoId) {
		List<AlumnoCurso> lstAlumno;
		List<AlumnoCursoVO> lstAlumnoVO = new ArrayList<>();
		
		lstAlumno = alumnoCursoDAO.findByAlumnoId(alumnoId);
		if(lstAlumno!=null && !lstAlumno.isEmpty()){
			lstAlumnoVO = AlumnoCursoBuilder.createListAlumnoCursoVO(lstAlumno);
		}
		
		return lstAlumnoVO;
	}

	
	public void createStudentPayment(CursoCostos cursoCostos, AlumnoCurso alumnoCurso, Date fechaInicio, Date fechaFin){
		AlumnoPagos alumnoPago;
		BigDecimal cursoMonto = BigDecimal.ZERO;
	
		cursoMonto = cursoCostos.getCostoMonto();
		
		//Si el curso es pago único, solo se crea un pago con fecha límite al final del curso
		if(cursoCostos.getCursoCostoPagoUnico()){
			alumnoPago = new AlumnoPagos();
			alumnoPago.setAlumnoPagoFechaLimite(fechaFin);
			
			alumnoPago.setAlumnoCursoId(alumnoCurso.getAlumnoCursoId());
			alumnoPago.setAlumnoPagoEstatus(1); //TODO estatus de pagos
			alumnoPago.setAlumnoPagoPago(BigDecimal.ZERO);
			alumnoPago.setAlumnoPagoMonto(cursoMonto);
			alumnoPago.setCursoCostoId(cursoCostos.getCursoCostoId());
			
			//Realizar cálculo para modifica el pago si es de beca
			if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
					alumnoCurso.getAlumnoCursoBeca()!=null && alumnoCurso.getAlumnoCursoBeca().compareTo(BigDecimal.ZERO) > 0 ){
				alumnoPago.setAlumnoPagoMonto(cursoMonto.subtract(cursoMonto
						.multiply(alumnoCurso.getAlumnoCursoBeca())
						.divide(CIEN,DECIMALES,RoundingMode.HALF_UP)));
			}
			
			//Realizar cálculo para modifica el pago si es de beca
			if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
					alumnoCurso.getAlumnoCursoDescuento()!=null && alumnoCurso.getAlumnoCursoDescuento().compareTo(BigDecimal.ZERO) > 0){
				alumnoPago.setAlumnoPagoMonto(
						cursoMonto
						.subtract(alumnoCurso.getAlumnoCursoDescuento()));
			}
			
			alumnoPagoDAO.save(alumnoPago);
		}
		
		//Si el curso no es único, se crea un pago por cada mes para la duracion del curso
		if(!cursoCostos.getCursoCostoPagoUnico()){
			//Crear el 1er pago con fecha límite el día X del 1er mes del curso.				
			Calendar calLimite = Calendar.getInstance();
			calLimite.setTime(fechaInicio);				
			calLimite.set(Calendar.DAY_OF_MONTH, cursoCostos.getCursoCostoDiaPago());
			
			//Si el alumno entró después de iniciado el curso, los pagos se crearan con el mes de ingreso del alumno al curso
			Calendar calIngresoAlumno = Calendar.getInstance();
			calIngresoAlumno.setTime(alumnoCurso.getAlumnoCursoIngreso());
			if(calLimite.before(calIngresoAlumno)){
				calLimite = calIngresoAlumno;
			}
			
			Calendar calEnd = Calendar.getInstance();
			calEnd.setTime(fechaFin);
			
			//Mientras la fecha límite de pago sea menor a la fecha de fin de curso, creará un 
			//registro de pago para cada mes
			while(calLimite.before(calEnd)){
				alumnoPago = new AlumnoPagos();
				
				if(calLimite.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
					calLimite.add(Calendar.DAY_OF_MONTH, -1);
				}else if(calLimite.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
					calLimite.add(Calendar.DAY_OF_MONTH, -2);
				}
				
				alumnoPago.setAlumnoPagoFechaLimite(calLimite.getTime());
				
				alumnoPago.setAlumnoCursoId(alumnoCurso.getAlumnoCursoId());
				alumnoPago.setAlumnoPagoEstatus(1); //TODO estatus de pagos
				alumnoPago.setAlumnoPagoPago(BigDecimal.ZERO);
				alumnoPago.setAlumnoPagoMonto(cursoMonto);
				alumnoPago.setCursoCostoId(cursoCostos.getCursoCostoId());
				
				//Realizar cálculo para modifica el pago si es de beca
				if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
						alumnoCurso.getAlumnoCursoBeca()!=null && alumnoCurso.getAlumnoCursoBeca().compareTo(BigDecimal.ZERO) > 0 ){
					alumnoPago.setAlumnoPagoMonto(cursoMonto.subtract(
							cursoMonto
							.multiply(alumnoCurso.getAlumnoCursoBeca())
							.divide(CIEN,DECIMALES,RoundingMode.HALF_UP)));
				}
				
				//Realizar cálculo para modifica el pago si es de beca
				if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
						alumnoCurso.getAlumnoCursoDescuento()!=null && alumnoCurso.getAlumnoCursoDescuento().compareTo(BigDecimal.ZERO) > 0){
					alumnoPago.setAlumnoPagoMonto(
							cursoMonto
							.subtract(alumnoCurso.getAlumnoCursoDescuento()));
				}
				
				alumnoPagoDAO.save(alumnoPago);
				
				//Sumar 1 mes a la fecha límite de pago y regresar el día de pago al original
				calLimite.add(Calendar.MONTH, 1);
				calLimite.set(Calendar.DAY_OF_MONTH, cursoCostos.getCursoCostoDiaPago());
			}
		}
	}

	//Obtener los cursos activos del alumno
	@Override
	public List<AlumnoCursoVO> findByStudentActive(Integer alumnoId) {
		List<AlumnoCurso> lstAlumno;
		List<AlumnoCursoVO> lstAlumnoVO = new ArrayList<>();
		
		lstAlumno = alumnoCursoDAO.findByAlumnoIdAndActive(alumnoId);
		if(lstAlumno!=null && !lstAlumno.isEmpty()){
			lstAlumnoVO = AlumnoCursoBuilder.createListAlumnoCursoVO(lstAlumno);
		}
		
		return lstAlumnoVO;
	}

	@Override
	public List<AlumnosVO> findByCurso(Integer cursoId) {
		List<AlumnoCurso> lstAlumnoCurso;
		List<Integer> lstAlumnoId;
		List<Alumnos> lstAlumnos;
		List<AlumnosVO> lstAlumnosVO = new ArrayList<>();
		
		lstAlumnoCurso = alumnoCursoDAO.findByCursoIdAndAlumnoCursoActivo(cursoId, true);
		if(lstAlumnoCurso!=null && !lstAlumnoCurso.isEmpty()){
			lstAlumnoId = new ArrayList<>();
			for(AlumnoCurso ac : lstAlumnoCurso){
				lstAlumnoId.add(ac.getAlumnoId());
			}
			
			lstAlumnos = alumnosDAO.findByAlumnoIdIn(lstAlumnoId);
			lstAlumnosVO = AlumnosBuilder.createListAlumnosVO(lstAlumnos);
		}
		
		return lstAlumnosVO;
	}

	@Override
	public AlumnoCursoVO findByCursoIdAndAlumnoId(Integer cursoId, Integer alumnoId) {
		AlumnoCurso alumnoCurso = alumnoCursoDAO.findByCursoIdAndAlumnoId(cursoId, alumnoId);
		return AlumnoCursoBuilder.createAlumnoCursoVO(alumnoCurso);
	}

	@Override
	public List<AlumnosVO> findStudentNotInCurso(Integer cursoId) {
		List<Alumnos> lstAlumnos = alumnoCursoDAO.findStudentNotInCurso(cursoId);
		return AlumnosBuilder.createListAlumnosVO(lstAlumnos);
	}
	
	private BigDecimal getBecaDiscountAmount(BigDecimal originalAmount, BigDecimal beca, BigDecimal discount) {
		BigDecimal montoDescuento = BigDecimal.ZERO;
		if(beca!=null && beca.compareTo(BigDecimal.ZERO)>0){
			montoDescuento = originalAmount.multiply(beca)
					.divide(SaeConstants.CIEN,SaeConstants.DECIMALES,RoundingMode.HALF_UP);
		}			
		if(discount!=null && discount.compareTo(BigDecimal.ZERO)>0){
			montoDescuento = discount;
		}
		return originalAmount.subtract(montoDescuento); 
	}
	
}
