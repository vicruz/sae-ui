package com.sae.gandhi.spring.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoCursoDAO;
import com.sae.gandhi.spring.dao.AlumnoPagoDAO;
import com.sae.gandhi.spring.dao.CursoCostosDAO;
import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.AlumnoPagos;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.utils.builder.AlumnoCursoBuilder;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;

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
	

	@Override
	public void save(AlumnoCursoVO alumnoCursoVO) {
		AlumnoCurso alumnoCurso = new AlumnoCurso();
		
		alumnoCurso.setAlumnoCursoActivo(true);
		alumnoCurso.setAlumnoCursoBeca(alumnoCursoVO.getAlumnoCursoBeca());
		alumnoCurso.setAlumnoCursoDescuento(alumnoCursoVO.getAlumnoCursoDescuento());
		alumnoCurso.setAlumnoCursoIngreso(Calendar.getInstance().getTime());
		alumnoCurso.setAlumnoId(alumnoCursoVO.getAlumnoId());
		if(alumnoCursoVO.getCursoVO()!=null){
			alumnoCurso.setCursoId(alumnoCursoVO.getCursoVO().getCursoId());			
		}else{
			alumnoCurso.setCursoId(alumnoCursoVO.getCursoId());
		}
		alumnoCurso = alumnoCursoDAO.save(alumnoCurso);
		
		//////////////////////////////////////
		//Crear los costos asociados al curso
		List<CursoCostos> lstCursoCostos = 
				cursoCostosDAO.findByCursoIdAndCursoCostoActivo(alumnoCurso.getCursoId(), true);
		
		for (CursoCostos cursoCostos : lstCursoCostos) {
			AlumnoPagos alumnoPago;
			
			//Si el curso es pago único, solo se crea un pago con fecha límite al final del curso
			if(cursoCostos.getCursoCostoPagoUnico()){
				alumnoPago = new AlumnoPagos();
				alumnoPago.setAlumnoPagoFechaLimite(
						SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaFin()));
				
				alumnoPago.setAlumnoCursoId(alumnoCurso.getAlumnoCursoId());
				alumnoPago.setAlumnoPagoEstatus(1); //TODO estatus de pagos
				alumnoPago.setAlumnoPagoPago(BigDecimal.ZERO);
				alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto());
				alumnoPago.setCursoCostoId(cursoCostos.getCursoCostoId());
				
				//Realizar cálculo para modifica el pago si es de beca
				if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
						alumnoCurso.getAlumnoCursoBeca()!=null && alumnoCurso.getAlumnoCursoBeca().compareTo(BigDecimal.ZERO) > 0 ){
					alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto().subtract(
							cursoCostos.getCostos().getCostoMonto()
							.multiply(alumnoCurso.getAlumnoCursoBeca())
							.divide(CIEN,DECIMALES,RoundingMode.HALF_UP)));
				}
				
				//Realizar cálculo para modifica el pago si es de beca
				if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
						alumnoCurso.getAlumnoCursoDescuento()!=null && alumnoCurso.getAlumnoCursoDescuento().compareTo(BigDecimal.ZERO) > 0){
					alumnoPago.setAlumnoPagoMonto(
							cursoCostos.getCostos().getCostoMonto()
							.subtract(alumnoCurso.getAlumnoCursoDescuento()));
				}
				
				alumnoPagoDAO.save(alumnoPago);
				continue;
			}
			
			//Si el curso genera adeudo, se crea un pago por cada mes para la duracion del curso
			if(cursoCostos.getCursoCostoGeneraAdeudo()){
				//Crear el 1er pago con fecha límite el dí X del 1er mes
				Calendar calLimite = Calendar.getInstance();
				calLimite.setTime(SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaInicio()));				
				calLimite.set(Calendar.DAY_OF_MONTH, cursoCostos.getCursoCostoDiaPago());
				
				Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaFin()));
				
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
					alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto());
					alumnoPago.setCursoCostoId(cursoCostos.getCursoCostoId());
					
					//Realizar cálculo para modifica el pago si es de beca
					if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
							alumnoCurso.getAlumnoCursoBeca()!=null && alumnoCurso.getAlumnoCursoBeca().compareTo(BigDecimal.ZERO) > 0 ){
						alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto().subtract(
								cursoCostos.getCostos().getCostoMonto()
								.multiply(alumnoCurso.getAlumnoCursoBeca())
								.divide(CIEN,DECIMALES,RoundingMode.HALF_UP)));
					}
					
					//Realizar cálculo para modifica el pago si es de beca
					if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
							alumnoCurso.getAlumnoCursoDescuento()!=null && alumnoCurso.getAlumnoCursoDescuento().compareTo(BigDecimal.ZERO) > 0){
						alumnoPago.setAlumnoPagoMonto(
								cursoCostos.getCostos().getCostoMonto()
								.subtract(alumnoCurso.getAlumnoCursoDescuento()));
					}
					
					alumnoPagoDAO.save(alumnoPago);
					
					//Sumar 1 mes a la fecha límite de pago y regresar el día de pago al original
					calLimite.add(Calendar.MONTH, 1);
					calLimite.set(Calendar.DAY_OF_MONTH, cursoCostos.getCursoCostoDiaPago());
				}
				
				
			}
	
			
		}
	}

	/**
	 * Actualiza el % de beca o el descuento
	 */
	@Override
	public void update(AlumnoCursoVO alumnoCursoVO) {
		AlumnoCurso alumnoCurso;
		Optional<AlumnoCurso> optional;
		BigDecimal saldo = BigDecimal.ZERO;
		
		optional = alumnoCursoDAO.findById(alumnoCursoVO.getAlumnoCursoId());
		
		if(!optional.isPresent()){
			//TODO enviar mensaje de error indicando que no existe el curso en el alumno
			return;
		}

		alumnoCurso = optional.get();
		
		//Si la beca y descuento son iguales, no realiza ninguna acción de evaluación
		if( ((alumnoCurso.getAlumnoCursoBeca()==null && alumnoCursoVO.getAlumnoCursoBeca()==null) || 
				alumnoCurso.getAlumnoCursoBeca().compareTo(alumnoCursoVO.getAlumnoCursoBeca())==0) &&
			((alumnoCurso.getAlumnoCursoDescuento()==null && alumnoCursoVO.getAlumnoCursoDescuento()==null) || 
						alumnoCurso.getAlumnoCursoDescuento().compareTo(alumnoCursoVO.getAlumnoCursoDescuento())==0)
				){
			return;
		}

		//TODO guardar bitácora de datos previos
		alumnoCurso.setAlumnoCursoBeca(alumnoCursoVO.getAlumnoCursoBeca());
		alumnoCurso.setAlumnoCursoDescuento(alumnoCursoVO.getAlumnoCursoDescuento());
		
		///////////////////////////////////////////
		//Ajustar el monto a pagar del curso
		///////////////////////////////////////////
		
		//Obtener los costos en los que aplicará la beca/descuento
		List<CursoCostos> lstCursoCostos = cursoCostosDAO.findByAlumnoCursoIdAndBeca(alumnoCurso.getAlumnoCursoId());
		
		for(CursoCostos cursoCosto : lstCursoCostos){
			//Obtener el monto a descontar
			BigDecimal montoDescuento = BigDecimal.ZERO;
			//Se estableció la beca
			if(alumnoCursoVO.getAlumnoCursoBeca()!=null){
				montoDescuento = cursoCosto.getCostos().getCostoMonto()
						.multiply(alumnoCursoVO.getAlumnoCursoBeca())
						.divide(CIEN,DECIMALES,RoundingMode.HALF_UP);
			}
			
			if(alumnoCursoVO.getAlumnoCursoDescuento()!=null){
				montoDescuento = alumnoCursoVO.getAlumnoCursoDescuento();
			}
			
			
			//Obtener los alumnosPagos que correspondan al curso costo y descontar el monto al valor total
			List<AlumnoPagos> lstAlumnoPago = alumnoPagoDAO.findByAlumnoCursoIdAndCursoCostoId(alumnoCurso.getAlumnoCursoId(), cursoCosto.getCursoCostoId());
			for(AlumnoPagos alumnoPago : lstAlumnoPago){
				alumnoPago.setAlumnoPagoMonto(alumnoPago.getAlumnoPagoMonto().subtract(montoDescuento));
				saldo = saldo.add(alumnoPago.getAlumnoPagoMonto().subtract(alumnoPago.getAlumnoPagoPago()));
			}
		}
		
		System.out.println("Saldo: " + saldo);
		
/*		
		//////////////////////////////////////
		//Crear los costos asociados al curso
		List<CursoCostos> lstCursoCostos = 
				cursoCostosDAO.findByCursoIdAndCursoCostoActivo(alumnoCurso.getCursoId(), true);
		
		for (CursoCostos cursoCostos : lstCursoCostos) {
			AlumnoPagos alumnoPago;
			
			//Si el curso es pago único, solo se crea un pago con fecha límite al final del curso
			if(cursoCostos.getCursoCostoPagoUnico()){
				alumnoPago = new AlumnoPagos();
				alumnoPago.setAlumnoPagoFechaLimite(
						SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaFin()));
				
				alumnoPago.setAlumnoCursoId(alumnoCurso.getAlumnoCursoId());
				alumnoPago.setAlumnoPagoEstatus(1); //TODO estatus de pagos
				alumnoPago.setAlumnoPagoPago(BigDecimal.ZERO);
				alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto());
				alumnoPago.setCursoCostoId(cursoCostos.getCursoCostoId());
				
				//Realizar cálculo para modifica el pago si es de beca
				if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
						alumnoCurso.getAlumnoCursoBeca()!=null && alumnoCurso.getAlumnoCursoBeca().compareTo(BigDecimal.ZERO) > 0 ){
					alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto().subtract(
							cursoCostos.getCostos().getCostoMonto()
							.multiply(alumnoCurso.getAlumnoCursoBeca())
							.divide(CIEN,DECIMALES,RoundingMode.HALF_UP)));
				}
				
				//Realizar cálculo para modifica el pago si es de beca
				if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
						alumnoCurso.getAlumnoCursoDescuento()!=null && alumnoCurso.getAlumnoCursoDescuento().compareTo(BigDecimal.ZERO) > 0){
					alumnoPago.setAlumnoPagoMonto(
							cursoCostos.getCostos().getCostoMonto()
							.subtract(alumnoCurso.getAlumnoCursoDescuento()));
				}
				
				alumnoPagoDAO.save(alumnoPago);
				continue;
			}
			
			//Si el curso genera adeudo, se crea un pago por cada mes para la duracion del curso
			if(cursoCostos.getCursoCostoGeneraAdeudo()){
				//Crear el 1er pago con fecha límite el dí X del 1er mes
				Calendar calLimite = Calendar.getInstance();
				calLimite.setTime(SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaInicio()));				
				calLimite.set(Calendar.DAY_OF_MONTH, cursoCostos.getCursoCostoDiaPago());
				
				Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(SaeDateUtils.localDateToDate(alumnoCursoVO.getCursoVO().getCursoFechaFin()));
				
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
					alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto());
					alumnoPago.setCursoCostoId(cursoCostos.getCursoCostoId());
					
					//Realizar cálculo para modifica el pago si es de beca
					if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
							alumnoCurso.getAlumnoCursoBeca()!=null && alumnoCurso.getAlumnoCursoBeca().compareTo(BigDecimal.ZERO) > 0 ){
						alumnoPago.setAlumnoPagoMonto(cursoCostos.getCostos().getCostoMonto().subtract(
								cursoCostos.getCostos().getCostoMonto()
								.multiply(alumnoCurso.getAlumnoCursoBeca())
								.divide(CIEN,DECIMALES,RoundingMode.HALF_UP)));
					}
					
					//Realizar cálculo para modifica el pago si es de beca
					if(cursoCostos.getCursoCostoAplicaBeca()!=null && cursoCostos.getCursoCostoAplicaBeca() && 
							alumnoCurso.getAlumnoCursoDescuento()!=null && alumnoCurso.getAlumnoCursoDescuento().compareTo(BigDecimal.ZERO) > 0){
						alumnoPago.setAlumnoPagoMonto(
								cursoCostos.getCostos().getCostoMonto()
								.subtract(alumnoCurso.getAlumnoCursoDescuento()));
					}
					
					alumnoPagoDAO.save(alumnoPago);
					
					//Sumar 1 mes a la fecha límite de pago y regresar el día de pago al original
					calLimite.add(Calendar.MONTH, 1);
					calLimite.set(Calendar.DAY_OF_MONTH, cursoCostos.getCursoCostoDiaPago());
				}
				
				
			}
	
			
		}
*/
	}

	@Override
	public void delete(Integer alumnoCursoId) {
		// TODO Auto-generated method stub

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

}
