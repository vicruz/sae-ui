package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.CursoCostosVO;

public interface CursoCostosService {

	public List<CursoCostosVO> findAllActive();
	public List<CursoCostosVO> findAll();
	public CursoCostosVO findById(Integer cursoCostoId);
	public void save(CursoCostosVO cursoCostosVo);
	public void update(CursoCostosVO cursoCostosVo);
	public void delete(Integer cursoCostoId);
	public List<CursoCostosVO> findByCurso(Integer cursoId, Boolean estatus);
	
}
