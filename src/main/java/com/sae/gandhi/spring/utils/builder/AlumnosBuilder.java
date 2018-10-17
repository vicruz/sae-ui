package com.sae.gandhi.spring.utils.builder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.sae.gandhi.spring.entity.Alumnos;
import com.sae.gandhi.spring.vo.AlumnosVO;

public class AlumnosBuilder {

	public static Alumnos createAlumnos(AlumnosVO alumnoVO){
		Alumnos alumno = new Alumnos();
		try {
			BeanUtils.copyProperties(alumno, alumnoVO);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alumno;
	}
	
	public static List<Alumnos> createListAlumnos(List<AlumnosVO> lstAlumnosVO){
		List<Alumnos> lstAlumnos = new ArrayList<>();
		for(AlumnosVO vo: lstAlumnosVO){
			lstAlumnos.add(createAlumnos(vo));
		}
		return lstAlumnos;
	}
	
	public static AlumnosVO createAlumnosVO(Alumnos alumnos){
		AlumnosVO vo = new AlumnosVO();
		try {
			BeanUtils.copyProperties(vo, alumnos);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vo;
	}
	
	public static List<AlumnosVO> createListAlumnosVO(List<Alumnos> lstAlumnos){
		List<AlumnosVO> lstVO = new ArrayList<>();
		for(Alumnos alumno : lstAlumnos){
			lstVO.add(createAlumnosVO(alumno));
		}
		return lstVO;
	}
	
}
