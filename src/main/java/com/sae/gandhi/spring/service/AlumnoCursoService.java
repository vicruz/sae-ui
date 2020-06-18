package com.sae.gandhi.spring.service;

import java.util.Date;
import java.util.List;

import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;
import com.sae.gandhi.spring.vo.AlumnosVO;

public interface AlumnoCursoService {

	public void save(AlumnoCursoVO alumnoCursoVO);
	public void update(AlumnoCursoVO alumnoCursoVO);
	public boolean delete(Integer alumnoCursoId);
	public AlumnoCursoVO findById(Integer alumnoCursoId);
	public List<AlumnoCursoVO> findByStudent(Integer alumnoId);
	public List<AlumnoCursoVO> findByStudentActive(Integer alumnoId);
	public void createStudentPayment(CursoCostos cursoCostos, AlumnoCurso alumnoCurso, Date fechaInicio, Date fechaFin);
	public List<AlumnosVO> findByCurso(Integer cursoId);
	public AlumnoCursoVO findByCursoIdAndAlumnoId(Integer cursoId, Integer alumnoId);
	public List<AlumnosVO> findStudentNotInCurso(Integer cursoId);
}
