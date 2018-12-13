package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.AlumnoPagoVO;

public interface AlumnoPagoService {
	
	public List<AlumnoPagoVO> findByAlumnoId(Integer alumnoId);
	public List<AlumnoPagoVO> findByAlumnoCursoId(Integer alumnoCursoId);
	public List<AlumnoPagoVO> findByAlumnoIdAndCursoId(Integer alumnoId, Integer cursoId);
	public AlumnoPagoVO update(AlumnoPagoVO vo);
	public AlumnoPagoVO updateFecha(AlumnoPagoVO vo);

}
