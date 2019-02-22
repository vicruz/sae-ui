package com.sae.gandhi.spring.service;

import java.math.BigDecimal;
import java.util.List;

import com.sae.gandhi.spring.vo.AlumnoPagoVO;

public interface AlumnoPagoService {
	
	public List<AlumnoPagoVO> findByAlumnoId(Integer alumnoId);
	public List<AlumnoPagoVO> findByAlumnoCursoId(Integer alumnoCursoId);
	public List<AlumnoPagoVO> findByAlumnoIdAndCursoId(Integer alumnoId, Integer cursoId);
	public AlumnoPagoVO update(AlumnoPagoVO vo);
	public AlumnoPagoVO updateFechaMonto(AlumnoPagoVO vo);
	public AlumnoPagoVO save(AlumnoPagoVO vo, Integer alumnoId, BigDecimal alumnoSaldo);
	public void delete(AlumnoPagoVO vo);
	public void updateMontoFechaExceed();

}
