package com.sae.gandhi.spring.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoCursoDAO;
import com.sae.gandhi.spring.dao.AlumnoPagoDAO;
import com.sae.gandhi.spring.dao.CursoCostosDAO;
import com.sae.gandhi.spring.dao.CursosDAO;
import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.entity.AlumnoPagos;
import com.sae.gandhi.spring.entity.CursoCostos;
import com.sae.gandhi.spring.entity.Cursos;
import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.utils.builder.CursoCostoBuilder;
import com.sae.gandhi.spring.vo.CursoCostosVO;

@Transactional
@Service
public class CursoCostosServiceImpl implements CursoCostosService {
	
	@Autowired
	private CursoCostosDAO cursoCostosDAO;
	
	@Autowired
	private AlumnoPagoDAO alumnoPagoDAO;
	
	@Autowired
	private AlumnoCursoDAO alumnoCursoDAO;
	
	@Autowired
	private CursosDAO cursosDAO;
	
	@Autowired
	private AlumnoCursoService alumnoCursoService;

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
		Calendar cal = Calendar.getInstance();
		
		if(cursoCostosVo.getCostosVO()!=null){
			cursoCostos.setCostoId(cursoCostosVo.getCostosVO().getCostoId());			
		}else{
			cursoCostos.setCostoId(cursoCostosVo.getCostoId());
		}
		cursoCostos.setCursoCostoActivo(true);
		cursoCostos.setFechaCreacion(cal.getTime());
		cursoCostos = cursoCostosDAO.save(cursoCostos);
		
		//Guardar el curso costo en los alumnos
		Optional<Cursos> optional = cursosDAO.findById(cursoCostos.getCursoId());
		List<AlumnoCurso> lst = alumnoCursoDAO.findByCursoId(cursoCostos.getCursoId());
		Cursos curso = optional.orElse(null);
		Date initDate = curso.getCursoFechaInicio();
		
		if(cal.getTime().after(curso.getCursoFechaInicio())){
			initDate = cal.getTime(); 
		}
		
		for(AlumnoCurso alumnoCurso : lst){
			alumnoCursoService.createStudentPayment(cursoCostos, alumnoCurso, initDate, curso.getCursoFechaFin());
		}
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
	public boolean delete(Integer cursoCostoId) {
		
		List<AlumnoPagos> lst = alumnoPagoDAO.findByCursoCostoIdPayed(cursoCostoId);
		//Busca si algún alumno ha pagado el costo del curso en algún mes
		if(lst!=null && !lst.isEmpty()){
			return false;
		}
		
		//elimina todos los pagos de los alumnos que sean del cursoCostoId
		alumnoPagoDAO.deleteByCursoCostoId(cursoCostoId);
		cursoCostosDAO.deleteById(cursoCostoId);
		return true;
	}

	@Override
	public List<CursoCostosVO> findByCurso(Integer cursoId, Boolean estatus) {
		return CursoCostoBuilder.createListCursoCostoVO(
				cursoCostosDAO.findByCursoIdAndCursoCostoActivo(cursoId, estatus));
	}

}
