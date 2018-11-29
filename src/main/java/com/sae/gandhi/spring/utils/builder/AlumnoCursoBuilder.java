package com.sae.gandhi.spring.utils.builder;

import java.lang.reflect.InvocationTargetException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;

import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;

public class AlumnoCursoBuilder {

	public static AlumnoCursoVO createAlumnoCursoVO(AlumnoCurso alumnoCurso){
		AlumnoCursoVO vo = new AlumnoCursoVO();
		AlumnoCurso tmp = new AlumnoCurso();
		
		try{
			Calendar calInit = null;
			Calendar calEnd = null;
			Calendar calStart = null;
			
			BeanUtils.copyProperties(tmp, alumnoCurso);
			
			if(Objects.nonNull(alumnoCurso.getCurso().getCursoFechaInicio())){
				calInit = SaeDateUtils.getCalendarFromDate(tmp.getCurso().getCursoFechaInicio());
			}
			if(Objects.nonNull(alumnoCurso.getCurso().getCursoFechaFin())){
				calEnd = SaeDateUtils.getCalendarFromDate(tmp.getCurso().getCursoFechaFin());
			}
			if(Objects.nonNull(alumnoCurso.getAlumnoCursoIngreso())){
				calStart = SaeDateUtils.getCalendarFromDate(tmp.getAlumnoCursoIngreso());
				tmp.setAlumnoCursoIngreso(null);
			}
			
			BeanUtils.copyProperties(vo, tmp);
			
			vo.setAlumnoCursoFechaIngreso(calStart.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			vo.setCursoFechaInicio(calInit.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			vo.setCursoFechaFin(calEnd.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			vo.setCursoNombre(alumnoCurso.getCurso().getCursoNombre());
			vo.setAlumnoCursoEstatus(1);//TODO
			
		}catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}	
		
		return vo;
	}
	
	public static AlumnoCurso createAlumnoCurso(AlumnoCursoVO vo){
	
		AlumnoCurso alumnoCurso = new AlumnoCurso();
		
		
		try {
			Date dateStart = null;
			
			if(vo.getAlumnoCursoFechaIngreso()!=null){
				dateStart = Date.from(vo.getAlumnoCursoFechaIngreso().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				vo.setAlumnoCursoFechaIngreso(null);
			}

			BeanUtils.copyProperties(alumnoCurso, vo);
			alumnoCurso.setAlumnoCursoIngreso(dateStart);
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return alumnoCurso;
	}

	public static List<AlumnoCursoVO> createListAlumnoCursoVO(List<AlumnoCurso> lst){
		
		List<AlumnoCursoVO> lstReturn = new ArrayList<>();
		
		for (AlumnoCurso alumnoCurso : lst) {
			lstReturn.add(createAlumnoCursoVO(alumnoCurso));
		}
		
		return lstReturn;
	}
	
	public static List<AlumnoCurso> createListAlumnocurso(List<AlumnoCursoVO> lst){
		List<AlumnoCurso> lstReturn = new ArrayList<>();
		
		for (AlumnoCursoVO alumnoCurso : lst) {
			lstReturn.add(createAlumnoCurso(alumnoCurso));
		}
		
		return lstReturn;
	}
}
