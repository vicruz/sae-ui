package com.sae.gandhi.spring.service;

import java.util.Date;
import java.util.List;

import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;

public interface AlumnoCursoService {

	public void save(AlumnoCursoVO alumnoCursoVO);
	public void update(AlumnoCursoVO alumnoCursoVO);
	public void delete(Integer alumnoCursoId);
	public AlumnoCursoVO findById(Integer alumnoCursoId);
	public List<AlumnoCursoVO> findByStudent(Integer alumnoId);
	public void createStudentPayment(CursoCostos cursoCostos, AlumnoCurso alumnoCurso, Date fechaInicio, Date fechaFin);
	
}
