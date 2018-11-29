package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.AlumnoCursoVO;

public interface AlumnoCursoService {

	public void save(AlumnoCursoVO alumnoCursoVO);
	public void update(AlumnoCursoVO alumnoCursoVO);
	public void delete(Integer alumnoCursoId);
	public AlumnoCursoVO findById(Integer alumnoCursoId);
	public List<AlumnoCursoVO> findByStudent(Integer alumnoId);
	
}
