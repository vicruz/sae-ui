package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.CostosVO;

public interface CostosService {
	
	public List<CostosVO> findAll();
	
	public CostosVO save(CostosVO costoDto);
	
	public void update(CostosVO costoDto);
	
	public void deactivate(CostosVO costoDto);
	
	public List<CostosVO> findByName(String name);
	
	public CostosVO findById(Integer costoId);
	
	public List<CostosVO> findAllActive();
	
	public List<CostosVO> findNotInCurso(Integer cursoId);
	
}
