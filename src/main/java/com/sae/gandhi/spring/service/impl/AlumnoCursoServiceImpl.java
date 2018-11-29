package com.sae.gandhi.spring.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnoCursoDAO;
import com.sae.gandhi.spring.entity.AlumnoCurso;
import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.utils.builder.AlumnoCursoBuilder;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;

@Service
@Transactional
public class AlumnoCursoServiceImpl implements AlumnoCursoService {
	
	@Autowired
	private AlumnoCursoDAO alumnoCursoDAO;
	

	@Override
	public void save(AlumnoCursoVO alumnoCursoVO) {
		AlumnoCurso alumnoCurso = new AlumnoCurso();
		
		alumnoCurso.setAlumnoCursoActivo(true);
		alumnoCurso.setAlumnoCursoBeca(alumnoCursoVO.getAlumnoCursoBeca());
		alumnoCurso.setAlumnoCursoDescuento(alumnoCursoVO.getAlumnoCursoDescuento());
		alumnoCurso.setAlumnoCursoIngreso(Calendar.getInstance().getTime());
		alumnoCurso.setAlumnoId(alumnoCursoVO.getAlumnoId());
		alumnoCurso.setCursoId(alumnoCursoVO.getCursoVO().getCursoId());
		alumnoCursoDAO.save(alumnoCurso);
	}

	@Override
	public void update(AlumnoCursoVO alumnoCursoVO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer alumnoCursoId) {
		// TODO Auto-generated method stub

	}

	@Override
	public AlumnoCursoVO findById(Integer alumnoCursoId) {
		// TODO Auto-generated method stub
		return null;
	}

	//Se usa para obtener los cursos a los que esta inscrito el alumno
	@Override
	public List<AlumnoCursoVO> findByStudent(Integer alumnoId) {
		List<AlumnoCurso> lstAlumno;
		List<AlumnoCursoVO> lstAlumnoVO = new ArrayList<>();
		
		lstAlumno = alumnoCursoDAO.findByAlumnoId(alumnoId);
		if(lstAlumno!=null && !lstAlumno.isEmpty()){
			lstAlumnoVO = AlumnoCursoBuilder.createListAlumnoCursoVO(lstAlumno);
		}
		
		return lstAlumnoVO;
	}

}
