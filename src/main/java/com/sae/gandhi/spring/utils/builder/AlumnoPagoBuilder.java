package com.sae.gandhi.spring.utils.builder;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.sae.gandhi.spring.entity.AlumnoPagos;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.vo.AlumnoPagoVO;

public class AlumnoPagoBuilder {

	public static AlumnoPagos createAlumnoPago(AlumnoPagoVO vo){
		AlumnoPagos alumnoPagos = new AlumnoPagos();
		Date fechaLimite;
		Date fechaPago;
		try {
			
			fechaLimite = SaeDateUtils.localDateToDate(vo.getAlumnoPagoFechaLimite());
			fechaPago = SaeDateUtils.localDateToDate(vo.getAlumnoPagoFechaPago());
			
			vo.setAlumnoPagoFechaLimite(null);
			vo.setAlumnoPagoFechaPago(null);
			
			BeanUtils.copyProperties(alumnoPagos, vo);
			alumnoPagos.setAlumnoPagoFechaLimite(fechaLimite);
			alumnoPagos.setAlumnoPagoFechaPago(fechaPago);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return alumnoPagos;
	}
	
	public static List<AlumnoPagos> createListAlumnoPago(List<AlumnoPagoVO> lstVO){
		List<AlumnoPagos> lst = new ArrayList<>();
		
		for (AlumnoPagoVO alumnoPagos : lstVO) {
			lst.add(createAlumnoPago(alumnoPagos));
		}
		
		return lst;
	}
	
	public static AlumnoPagoVO createAlumnoPagoVO(AlumnoPagos alumnoPago){
		AlumnoPagoVO vo = new AlumnoPagoVO();
		AlumnoPagos tmp = new AlumnoPagos();
		LocalDate fechaLimite;
		LocalDate fechaPago;
		try {
			BeanUtils.copyProperties(tmp, alumnoPago);
			
			fechaLimite = SaeDateUtils.dateToLocalDate(tmp.getAlumnoPagoFechaLimite());
			fechaPago = SaeDateUtils.dateToLocalDate(tmp.getAlumnoPagoFechaPago());
			
			tmp.setAlumnoPagoFechaLimite(null);
			tmp.setAlumnoPagoFechaPago(null);
			
			BeanUtils.copyProperties(vo, tmp);
			vo.setCursoNombre(alumnoPago.getAlumnoCurso().getCurso().getCursoNombre());
			vo.setCostoNombre(alumnoPago.getCursoCostos().getCostoNombre());
			vo.setEstatusId(alumnoPago.getAlumnoPagoEstatus());
			vo.setAlumnoPagoFechaLimite(fechaLimite);
			vo.setAlumnoPagoFechaPago(fechaPago);
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	public static List<AlumnoPagoVO> createListAlumnoPagoVO(List<AlumnoPagos> lstAlumnoPagos){
		List<AlumnoPagoVO> lst = new ArrayList<>();
		
		for (AlumnoPagos alumnoPagos : lstAlumnoPagos) {
			lst.add(createAlumnoPagoVO(alumnoPagos));
		}
		
		return lst;
	}
}
