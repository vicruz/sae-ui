package com.sae.gandhi.spring.dao;

import java.util.Date;
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
	
	@Query("Select ap from AlumnoPagos ap join ap.cursoCostos cc where cc.cursoCostoAplicaBeca = true and ap.alumnoCursoId = ?1")
	public List<AlumnoPagos> findByAlumnoCursoIdAndBeca(Integer alumnocursoId);
	
	public List<AlumnoPagos> findByAlumnoCursoIdAndCursoCostoId(Integer alumnoCursoId, Integer cursoCostoId);
	
	@Query("Select ap from AlumnoPagos ap "
			+ "join ap.cursoCostos cc "
			+ "where cc.cursoCostoGeneraAdeudo = 1 "
			+ "and (ap.alumnoPagoEstatus = ?1 or ap.alumnoPagoEstatus = ?2) "
			+ "and ap.alumnoPagoFechaLimite < ?3")
	List<AlumnoPagos> findPagoLimitExceed(Integer semaforoPendiente, Integer semaforoAdeudo, Date today);
	
	@Query("Select ap from AlumnoPagos ap where ap.alumnoPagoPago is not null and ap.alumnoPagoPago > 0 and ap.cursoCostoId = ?1")
	public List<AlumnoPagos> findByCursoCostoIdPayed(Integer cursoCostoId);
	
	public void deleteByCursoCostoId(Integer cursoCostoId);
}
