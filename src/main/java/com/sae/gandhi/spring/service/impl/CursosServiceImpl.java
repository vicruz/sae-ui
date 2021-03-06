package com.sae.gandhi.spring.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoCursoDAO;
import com.sae.gandhi.spring.dao.CursoCostosDAO;
import com.sae.gandhi.spring.dao.CursosDAO;
import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.entity.Cursos;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.utils.builder.CursosBuilder;
import com.sae.gandhi.spring.vo.CursosVO;

@Transactional
@Service
public class CursosServiceImpl implements CursosService {
	
	@Autowired
	private CursosDAO cursosDAO;
	
	@Autowired
	private CursoCostosDAO cursoCostosDAO;
	
	@Autowired
	private AlumnoCursoDAO alumnoCursoDAO;

	@Override
	public List<CursosVO> findAllActive() {
		List<CursosVO> lstVO;
		List<Cursos> lst;
		
		lst = cursosDAO.findAll();
		lstVO = CursosBuilder.createListCursoVO(lst);
		
		for(CursosVO vo : lstVO){
			vo.setInscritos(0);
		}
			
		return lstVO;
	}
	
	@Override
	public List<CursosVO> findAll() {
		List<CursosVO> lstVO;
		List<Cursos> lst;
		
		lst = cursosDAO.findAllOrderByFechaCreacionAscAndCursoActivo();
		lstVO = CursosBuilder.createListCursoVO(lst);
		
		for(CursosVO vo : lstVO){
			vo.setInscritos(0);
		}
			
		return lstVO;
	}

	@Override
	public CursosVO findById(Integer cursoId) {
		Optional<Cursos> curso = cursosDAO.findById(cursoId);
		return CursosBuilder.createCursoVO(curso.get());
	}

	@Override
	public void save(CursosVO cursoVo) {
		Cursos curso = CursosBuilder.createCursos(cursoVo);
		curso.setCursoStatus(1);
		curso.setFechaCreacion(Calendar.getInstance().getTime());
		cursosDAO.save(curso);
	}

	@Override
	public void update(CursosVO cursoVo) {
		Optional<Cursos> optionalCurso = cursosDAO.findById(cursoVo.getCursoId());
		Cursos curso = optionalCurso.get();
		curso.setCursoNombre(cursoVo.getCursoNombre());
		curso.setCursoFechaFin(Date.from(cursoVo.getCursoFechaFin().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		curso.setCursoFechaInicio(Date.from(cursoVo.getCursoFechaInicio().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		cursosDAO.save(curso);
	}

	@Override
	public boolean delete(Integer cursoId) {
		Optional<Cursos> optionalCurso = cursosDAO.findById(cursoId);
		Cursos curso = optionalCurso.get();
		
		List<AlumnoCurso> listAlumnos = alumnoCursoDAO.findByCursoIdAndAlumnoCursoActivo(cursoId, true);
		
		if(!listAlumnos.isEmpty()){
			return false;
		}
		
		curso.setCursoStatus(SaeEnums.Curso.CANCELADO.getStatusId());
		cursosDAO.save(curso);
		
		return true;
	}

	@Override
	public List<CursosVO> findByName(String cursNombre) {
		// TODO Auto-generated method stub
		return null;
	}

	//Identifica los cursos a los que no se encuentra inscrito el alumno
	@Override
	public List<CursosVO> findCoursesNotInStudent(Integer alumnoId) {
		//List<Cursos> lst = cursosDAO.findCoursesNotInStudent(alumnoId);
		List<Integer> lstInteger = new ArrayList<>();
		lstInteger.add(SaeEnums.Curso.ACTIVO.getStatusId());
		lstInteger.add(SaeEnums.Curso.PREPARADO.getStatusId());
		List<Cursos> lst = cursosDAO.findCoursesNotInStudentAndInStatus(lstInteger, alumnoId);
		return CursosBuilder.createListCursoVO(lst);
	}

	@Override
	public void updateStartedCourses() {
		Date today = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DATE);
		cursosDAO.updateStartedCourses(today);
	}

	@Override
	public void updateFinishedCourses() {
		Date today = DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DATE);
		cursosDAO.updateFinishedCourses(today);
	}

	@Override
	public void updateCourse(Integer cursoStatus, Integer cursoId) {
		cursosDAO.updateCourse(cursoStatus, cursoId);
	}

	@Override
	public boolean copyCourse(CursosVO cursoVo) {
		
		Date actualDate = Calendar.getInstance().getTime();
		
		//Crear curso
		Optional<Cursos> optionalCurso = cursosDAO.findById(cursoVo.getCursoId());
		if(!optionalCurso.isPresent())
			return false;

		Cursos oldCurso = optionalCurso.get();
		Cursos newCurso = new Cursos();
		
		newCurso.setCursoFechaFin(oldCurso.getCursoFechaFin());
		newCurso.setCursoFechaInicio(oldCurso.getCursoFechaInicio());
		newCurso.setCursoNombre(cursoVo.getCursoNombre());
		newCurso.setCursoStatus(oldCurso.getCursoStatus());
		newCurso.setFechaCreacion(actualDate);
		
		newCurso = cursosDAO.save(newCurso);
		
		List<CursoCostos> lst = new ArrayList<>();
		
		for(CursoCostos oldCursoCostos : oldCurso.getLstCursoCostos()) {
			CursoCostos newCursoCostos = new CursoCostos();
			
			newCursoCostos.setCostoMonto(oldCursoCostos.getCostoMonto());
			newCursoCostos.setCostoNombre(oldCursoCostos.getCostoNombre());
			newCursoCostos.setCursoCostoActivo(oldCursoCostos.getCursoCostoActivo());
			newCursoCostos.setCursoCostoAplicaBeca(oldCursoCostos.getCursoCostoAplicaBeca());
			newCursoCostos.setCursoCostoDiaPago(oldCursoCostos.getCursoCostoDiaPago());
			newCursoCostos.setCursoCostoGeneraAdeudo(oldCursoCostos.getCursoCostoGeneraAdeudo());
			newCursoCostos.setCursoCostoPagoUnico(oldCursoCostos.getCursoCostoPagoUnico());
			newCursoCostos.setFechaCreacion(actualDate);
			newCursoCostos.setCursoId(newCurso.getCursoId());
			lst.add(newCursoCostos);
		}
		
		cursoCostosDAO.saveAll(lst);
		
		return true;
	}

}
