package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.Cursos;

public interface CursosDAO extends JpaRepository<Cursos, Integer> {

	@Query("Select c from Cursos c order by c.fechaCreacion, c.cursoStatus desc")
	public List<Cursos> findAll();
	
	@Query("Select c from Cursos c order by c.fechaCreacion desc, c.cursoStatus desc")
	public List<Cursos> findAllOrderByFechaCreacionAscAndCursoActivo();
	
	public List<Cursos> findAllByCursoStatusOrderByFechaCreacionDesc(Integer statusId);
	
}
