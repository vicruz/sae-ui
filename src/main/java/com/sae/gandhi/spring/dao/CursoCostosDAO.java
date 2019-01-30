package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.CursoCostos;

public interface CursoCostosDAO extends JpaRepository<CursoCostos, Integer> {

	
	List<CursoCostos> findByCursoCostoActivo(Boolean status);
	List<CursoCostos> findByCursoIdAndCursoCostoActivo(Integer cursoId, Boolean status);
	
	@Query("Select distinct cc from AlumnoPagos ap join ap.cursoCostos cc where cc.cursoCostoAplicaBeca = true and ap.alumnoCursoId = ?1")
	public List<CursoCostos> findByAlumnoCursoIdAndBeca(Integer alumnocursoId);
	
	
}
