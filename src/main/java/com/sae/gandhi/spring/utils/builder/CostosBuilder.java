package com.sae.gandhi.spring.utils.builder;

import java.util.ArrayList;
import java.util.List;

import com.sae.gandhi.spring.entity.Costos;
import com.sae.gandhi.spring.vo.CostosVO;

public class CostosBuilder {

	public static CostosVO createCostoVO(Costos costo){
		CostosVO dto = new CostosVO();
		
		dto.setCostoActivo(costo.getCostoActivo());
		dto.setFechaCreacion(costo.getFechaCreacion());
		dto.setCostoId(costo.getCostoId());
		dto.setCostoMonto(costo.getCostoMonto());
		dto.setCostoNombre(costo.getCostoNombre());
		
		return dto;
	}
	
	public static List<CostosVO> createListCostoVO(List<Costos> lstCostos){
		List<CostosVO> lstCostosDto = new ArrayList<>();
		
		for(Costos costo : lstCostos){
			lstCostosDto.add(createCostoVO(costo));
		}
		
		return lstCostosDto;
	}
	
	public static Costos createCosto(CostosVO costoDto){
		Costos costo = new Costos();
		
		costo.setCostoActivo(costoDto.getCostoActivo());
		costo.setFechaCreacion(costoDto.getFechaCreacion());
		costo.setCostoId(costoDto.getCostoId());
		costo.setCostoMonto(costoDto.getCostoMonto());
		costo.setCostoNombre(costoDto.getCostoNombre());
		
		return costo;
	}
	
	public static List<Costos> createListCosto(List<CostosVO> lstCostosDto){
		List<Costos> lstCostos = new ArrayList<>();
		
		for(CostosVO costoDto : lstCostosDto){
			lstCostos.add(createCosto(costoDto));
		}
		
		return lstCostos;
	}
	
}
