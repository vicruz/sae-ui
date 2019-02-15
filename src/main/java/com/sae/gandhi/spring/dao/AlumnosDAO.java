package com.sae.gandhi.spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sae.gandhi.spring.entity.Alumnos;

public interface AlumnosDAO extends JpaRepository<Alumnos, Integer> {

	@Query("update Alumnos a set a.alumnoActivo = ?1 where a.alumnoId = ?2 ")
	public void changeActivo(boolean estatusId, Integer alumnoId);
}
