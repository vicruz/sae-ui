package com.sae.gandhi.spring.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.entity.Cursos;
import com.sae.gandhi.spring.vo.GraphStudentVO;

public interface CursosDAO extends JpaRepository<Cursos, Integer> {

	@Query("Select c from Cursos c order by c.cursoStatus asc, c.fechaCreacion desc")
	public List<Cursos> findAll();
	
	@Query("Select c from Cursos c order by c.fechaCreacion desc, c.cursoStatus desc")
	public List<Cursos> findAllOrderByFechaCreacionAscAndCursoActivo();
	
	public List<Cursos> findAllByCursoStatusOrderByFechaCreacionDesc(Integer statusId);
	
	@Query("Select c from Cursos c where c.cursoId not in "
			+ "(select ac.cursoId from AlumnoCurso ac where ac.alumnoId = ?1) order by c.fechaCreacion desc")
	public List<Cursos> findCoursesNotInStudent(Integer alumnoId);
	
	@Query("Select c from Cursos c where c.cursoStatus in ?1 and c.cursoId not in "
			+ "(select ac.cursoId from AlumnoCurso ac where ac.alumnoId = ?2) order by c.fechaCreacion desc")
	public List<Cursos> findCoursesNotInStudentAndInStatus(List<Integer> lstCursoStatus, Integer alumnoId);
	
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
	
	@Query("Select new com.sae.gandhi.spring.vo.GraphStudentVO(curso.cursoNombre, count(1)) "
			+ "from AlumnoCurso ac "
			+ "join ac.curso curso "
			+ "where curso.cursoFechaFin > ?1 "
			+ "and ac.alumnoCursoActivo= true "
			+ "group by curso.cursoNombre "
			+ "order by curso.cursoId")
	public List<GraphStudentVO> getCourseGraphData(Date fechaActual);

	/*	
	select curso.CURSO_NOMBRE 
	from alumno_curso ac
	join cursos curso on ac.CURSO_ID = curso.CURSO_ID
	where ac.ALUMNO_ID = 4
	and ac.ALUMNOCURSO_ID = (select min(ac2.alumnocurso_id) from alumno_curso ac2 
			join cursos curso2 on ac2.curso_id = curso2.curso_id 
	        where ac2.alumno_id = ac.alumno_id 
	        and curso2.CURSO_ESTATUS= 2);
*/	
	@Query("Select curso.cursoNombre from AlumnoCurso ac "
			+ "join ac.curso curso "
			+ "where ac.alumnoId = ?1 "
			+ "and ac.alumnoCursoId = "
			+ "		(select min(ac2.alumnoCursoId) from AlumnoCurso ac2 "
			+ "		join ac2.curso curso2 "
			+ "		where ac2.alumnoId = ac.alumnoId "
			+ "		and curso2.cursoStatus = 2)")
	public String getGradeCourseByStudent(Integer alumnoId);
	
}
