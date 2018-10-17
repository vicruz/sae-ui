package com.sae.gandhi.spring.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.CostosDAO;
import com.sae.gandhi.spring.entity.Costos;
import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.utils.builder.CostosBuilder;
import com.sae.gandhi.spring.vo.CostosVO;

@Transactional
@Service
public class CostosServiceImpl implements CostosService {

	
	@Autowired
	private CostosDAO costosDAO;

	@Override
	public List<CostosVO> findAll() {		
		return CostosBuilder.createListCostoVO(costosDAO.findAll());
	}

	@Override
	public void save(CostosVO costoDto) {
		Costos costo = CostosBuilder.createCosto(costoDto);
		costo.setCostoActivo(true);		
		costo.setFechaCreacion(Calendar.getInstance().getTime());
		
		costosDAO.save(costo);
	}

	@Override
	public void update(CostosVO costoDto) {
		Optional<Costos> optional = costosDAO.findById(costoDto.getCostoId());
		Costos costo = optional.get();
		
		costo.setCostoMonto(costoDto.getCostoMonto());
		costo.setCostoNombre(costoDto.getCostoNombre());
		
	}

	@Override
	public void deactivate(CostosVO costoDto) {
		Optional<Costos> optional = costosDAO.findById(costoDto.getCostoId());
		Costos costo = optional.get();
		
		costo.setCostoActivo(false);
	}

	@Override
	public List<CostosVO> findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CostosVO findById(Integer costoId) {
		Optional<Costos> optional = costosDAO.findById(costoId);
		Costos costo = optional.get();
		
		return CostosBuilder.createCostoVO(costo);
	}

	@Override
	public List<CostosVO> findAllActive() {
		return CostosBuilder.createListCostoVO(costosDAO.findAllActive());
	}

	@Override
	public List<CostosVO> findNotInCurso(Integer cursoId) {
		return CostosBuilder.createListCostoVO(costosDAO.findAllNotInCurso(cursoId));
	}
	
	


}
