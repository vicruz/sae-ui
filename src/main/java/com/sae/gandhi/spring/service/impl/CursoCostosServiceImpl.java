package com.sae.gandhi.spring.service.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.CursoCostosDAO;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.utils.builder.CursoCostoBuilder;
import com.sae.gandhi.spring.vo.CursoCostosVO;

@Transactional
@Service
public class CursoCostosServiceImpl implements CursoCostosService {
	
	@Autowired
	private CursoCostosDAO cursoCostosDAO;

	@Override
	public List<CursoCostosVO> findAllActive() {
		return CursoCostoBuilder.createListCursoCostoVO(cursoCostosDAO.findByCursoCostoActivo(true));
	}

	@Override
	public List<CursoCostosVO> findAll() {
		return CursoCostoBuilder.createListCursoCostoVO(cursoCostosDAO.findAll());
	}

	@Override
	public CursoCostosVO findById(Integer cursoCostoId) {
		Optional<CursoCostos> optional = cursoCostosDAO.findById(cursoCostoId);
		return CursoCostoBuilder.createCursoCostoVO(optional.get());
	}

	@Override
	public void save(CursoCostosVO cursoCostosVo) {
		CursoCostos cursoCostos = CursoCostoBuilder.createCursoCosto(cursoCostosVo);
		cursoCostos.setCostoId(cursoCostosVo.getCostosVO().getCostoId());
		cursoCostos.setCursoCostoActivo(true);
		cursoCostos.setFechaCreacion(Calendar.getInstance().getTime());
		cursoCostosDAO.save(cursoCostos);
	}

	@Override
	public void update(CursoCostosVO cursoCostosVo) {
		Optional<CursoCostos> optional = cursoCostosDAO.findById(cursoCostosVo.getCostoId());
		CursoCostos cursoCostos = optional.get();
		
		cursoCostos.setCursoCostoActivo(cursoCostosVo.getCursoCostoActivo());
		//cursoCostos.setCursoCostoAnio(cursoCostosVo.getCursoCostoAnio());
		cursoCostos.setCursoCostoAplicaBeca(cursoCostosVo.getCursoCostoAplicaBeca());
		//cursoCostos.setCursoCostoFechaLimite(cursoCostosVo.getCursoCostoFechaLimite());
		cursoCostos.setCursoCostoGeneraAdeudo(cursoCostosVo.getCursoCostoGeneraAdeudo());
		//cursoCostos.setCursoCostoMes(cursoCostosVo.getCursoCostoMes());
		cursoCostos.setCursoCostoPagoUnico(cursoCostosVo.getCursoCostoPagoUnico());
		cursoCostos.setCursoCostoDiaPago(cursoCostosVo.getCursoCostoDiaPago());
		cursoCostosDAO.save(cursoCostos);
	}

	@Override
	public void delete(Integer cursoCostoId) {
		cursoCostosDAO.deleteById(cursoCostoId);
	}

	@Override
	public List<CursoCostosVO> findByCurso(Integer cursoId, Boolean estatus) {
		return CursoCostoBuilder.createListCursoCostoVO(
				cursoCostosDAO.findByCursoIdAndCursoCostoActivo(cursoId, estatus));
	}

}
