package com.sae.gandhi.spring.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.entity.Cursos;

public interface CursosDAO extends JpaRepository<Cursos, Integer> {

	@Query("Select c from Cursos c order by c.cursoStatus asc, c.fechaCreacion desc")
	public List<Cursos> findAll();
	
	@Query("Select c from Cursos c order by c.fechaCreacion desc, c.cursoStatus desc")
	public List<Cursos> findAllOrderByFechaCreacionAscAndCursoActivo();
	
	public List<Cursos> findAllByCursoStatusOrderByFechaCreacionDesc(Integer statusId);
	
	@Query("Select c from Cursos c where c.cursoId not in "
			+ "(select ac.cursoId from AlumnoCurso ac where ac.alumnoId = ?1) order by c.fechaCreacion desc")
	public List<Cursos> findCoursesNotInStudent(Integer alumnoId);
	
	@Transactional
	@Modifying
	@Query("Update Cursos c set c.cursoStatus = 2 where c.cursoStatus = 1 and c.cursoFechaInicio <= ?1")
	public void updateStartedCourses(Date today);
	
	@Transactional
	@Modifying
	@Query("Update Cursos c set c.cursoStatus = 3 where c.cursoStatus = 2 and c.cursoFechaFin <= ?1")
	public void updateFinishedCourses(Date today);
	
	@Modifying
	@Query("Update Cursos c set c.cursoStatus = ?1 where c.cursoId = ?2")
	public void updateCourse(Integer cursoStatus, Integer cursoId);
	
}
