package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sae.gandhi.spring.entity.AlumnoCurso;

public interface AlumnoCursoDAO extends JpaRepository<AlumnoCurso, Integer> {

	public List<AlumnoCurso> findByAlumnoId(Integer alumnoId);
	
	public List<AlumnoCurso> findByCursoId(Integer cursoId);
}
