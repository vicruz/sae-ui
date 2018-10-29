package com.sae.gandhi.spring.utils.builder;

import java.lang.reflect.InvocationTargetException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;

import com.sae.gandhi.spring.entity.Alumnos;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.vo.AlumnosVO;

public class AlumnosBuilder {

	public static Alumnos createAlumnos(AlumnosVO alumnoVO){
		Alumnos alumno = new Alumnos();
		try {
			Date dateBirth = null;
			
			if(Objects.nonNull(alumnoVO.getAlumnoFechaNac())){
				dateBirth = Date.from(alumnoVO.getAlumnoFechaNac().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				alumnoVO.setAlumnoFechaNac(null);
			}
			
			BeanUtils.copyProperties(alumno, alumnoVO);
			alumno.setAlumnoFechaNac(dateBirth);
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
			Calendar calendar = null;
			
			if(Objects.nonNull(alumnos.getAlumnoFechaNac())){
				calendar = SaeDateUtils.getCalendarFromDate(alumnos.getAlumnoFechaNac());
				alumnos.setAlumnoFechaNac(null);
			}
			
			BeanUtils.copyProperties(vo, alumnos);
			if(Objects.nonNull(calendar)){
				vo.setAlumnoFechaNac(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());				
			}
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
