package com.sae.gandhi.spring.utils.builder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.vo.CursoCostosVO;

public class CursoCostoBuilder {
	
	public static CursoCostosVO createCursoCostoVO(CursoCostos cursoCostos){
		CursoCostosVO vo = new CursoCostosVO();
		
		try {
			BeanUtils.copyProperties(vo, cursoCostos);
			vo.setCostoMonto(cursoCostos.getCostos().getCostoMonto());
			vo.setCostoNombre(cursoCostos.getCostos().getCostoNombre());
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vo;
	}
	
	
	public static List<CursoCostosVO> createListCursoCostoVO(List<CursoCostos> lstCursoCostos){
		List<CursoCostosVO> lstVo = new ArrayList<>();
		
		for(CursoCostos curso: lstCursoCostos){
			lstVo.add(createCursoCostoVO(curso));
		}
		
		return lstVo;
	}
	
	public static CursoCostos createCursoCosto(CursoCostosVO cursoCostosVo){
		CursoCostos curso = new CursoCostos();
		
		try {
			BeanUtils.copyProperties(curso, cursoCostosVo);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return curso;
	}
	
	
	public static List<CursoCostos> createListCursoCosto(List<CursoCostosVO> lstCursoCostosVO){
		List<CursoCostos> lstCurso = new ArrayList<>();
		
		for(CursoCostosVO cursoVO: lstCursoCostosVO){
			lstCurso.add(createCursoCosto(cursoVO));
		}
		
		return lstCurso;
	}
	

}
