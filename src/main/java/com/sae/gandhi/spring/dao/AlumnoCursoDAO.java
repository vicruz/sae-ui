package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.Alumnos;

public interface AlumnoCursoDAO extends JpaRepository<AlumnoCurso, Integer> {

	public List<AlumnoCurso> findByAlumnoId(Integer alumnoId);
	
	@Query("from AlumnoCurso ac where ac.alumnoId = ?1 and ac.alumnoCursoActivo = true")
	public List<AlumnoCurso> findByAlumnoIdAndActive(Integer alumnoId);
	
	public List<AlumnoCurso> findByCursoIdAndAlumnoCursoActivo(Integer cursoId, boolean activo);
	
	@Modifying
	@Query("update AlumnoCurso ac set ac.alumnoCursoActivo = ?1 where ac.alumnoCursoId = ?2")
	public void updateStatusCurso(boolean status, Integer alumnoCursoId);
	
	public AlumnoCurso findByCursoIdAndAlumnoId(Integer cursoId, Integer alumnoId);
	
	@Query("select al from Alumnos al where al.alumnoActivo = true and al.alumnoId not in (select alc.alumnoId from AlumnoCurso alc where alc.cursoId = ?1 ) ")
	public List<Alumnos> findStudentNotInCurso(Integer cursoId);
}
