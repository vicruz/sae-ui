package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.AlumnoPagos;

public interface AlumnoPagoDAO extends JpaRepository<AlumnoPagos, Integer> {

	public List<AlumnoPagos> findByAlumnoCursoId(Integer alumnoCursoId);

	@Query("Select ap from AlumnoPagos ap join ap.alumnoCurso ac where ac.alumnoId = ?1")
	public List<AlumnoPagos> findByAlumnoId(Integer alumnoId);
	
	@Query("Select ap from AlumnoPagos ap join ap.alumnoCurso ac where ac.alumnoId = ?1 and ac.cursoId = ?2")
	public List<AlumnoPagos> findByAlumnoIdAndCursoId(Integer alumnoId, Integer cursoId);
	
}
