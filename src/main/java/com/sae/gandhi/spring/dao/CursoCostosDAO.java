package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.CursoCostos;

public interface CursoCostosDAO extends JpaRepository<CursoCostos, Integer> {

	
	List<CursoCostos> findByCursoCostoActivo(Boolean status);
	List<CursoCostos> findByCursoIdAndCursoCostoActivo(Integer cursoId, Boolean status);
	
}
