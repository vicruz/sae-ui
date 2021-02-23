package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.CursosVO;

public interface CursosService {

	public List<CursosVO> findAllActive();
	public List<CursosVO> findAll();
	public CursosVO findById(Integer cursoId);
	public void save(CursosVO cursoVo);
	public void update(CursosVO cursoVo);
	public boolean delete(Integer cursoId);
	public List<CursosVO> findByName(String cursNombre);
	public List<CursosVO> findCoursesNotInStudent(Integer alumnoId);
	public void updateStartedCourses();
	public void updateFinishedCourses();
	public void updateCourse(Integer cursoStatus, Integer cursoId);
	public boolean copyCourse(CursosVO cursoVo);
	
}
