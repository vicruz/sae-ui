package com.sae.gandhi.spring.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.Costos;

public interface CostosDAO extends JpaRepository<Costos, Integer> {
	
	@Query("Select c from Costos c where c.costoActivo = 1 order by c.fechaCreacion desc")
	public List<Costos> findAllActive();
	
	//@Query("Select c from Costos c where c.costoId not in (Select cc.costoId from CursoCostos cc where cc.cursoId = ?1)")
	//public List<Costos> findAllNotInCurso(Integer cursoId);
	
}
