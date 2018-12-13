package com.sae.gandhi.spring.utils.builder;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sae.gandhi.spring.entity.Cursos;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.vo.CursosVO;

public class CursosBuilder {

	public static CursosVO createCursoVO(Cursos curso){
		CursosVO cursoVO = new CursosVO();
		Calendar calInicio = SaeDateUtils.getCalendarFromDate(curso.getCursoFechaInicio());
		Calendar calFinal = SaeDateUtils.getCalendarFromDate(curso.getCursoFechaFin());
		
		cursoVO.setCursoStatus(curso.getCursoStatus());
		cursoVO.setCursoFechaFin(calFinal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		cursoVO.setCursoFechaInicio(calInicio.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		cursoVO.setCursoId(curso.getCursoId());
		cursoVO.setCursoNombre(curso.getCursoNombre());
		//cursoVO.setInscritos(0);
		return cursoVO;
	}
	
	public static List<CursosVO> createListCursoVO(List<Cursos> lstCursos){
		List<CursosVO> lstVO = new ArrayList<CursosVO>();
		for(Cursos curso : lstCursos){
			lstVO.add(createCursoVO(curso));
		}
		
		return lstVO;
	}
	
	public static Cursos createCursos(CursosVO cursoVO){
		Cursos curso = new Cursos();
		curso.setCursoStatus(cursoVO.getCursoStatus());
		curso.setCursoFechaFin(Date.from(cursoVO.getCursoFechaFin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		curso.setCursoFechaInicio(Date.from(cursoVO.getCursoFechaInicio().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		curso.setCursoId(cursoVO.getCursoId());
		curso.setCursoNombre(cursoVO.getCursoNombre());
		//cursoVO.setInscritos(0);
		return curso;
	}
	
	public static List<Cursos> createListCurso(List<CursosVO> lstCursosVO){
		List<Cursos> lst = new ArrayList<Cursos>();
		for(CursosVO cursoVO : lstCursosVO){
			lst.add(createCursos(cursoVO));
		}
		
		return lst;
	}
	
}
