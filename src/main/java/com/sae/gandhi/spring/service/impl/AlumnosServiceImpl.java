package com.sae.gandhi.spring.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnosDAO;
import com.sae.gandhi.spring.entity.Alumnos;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.utils.builder.AlumnosBuilder;
import com.sae.gandhi.spring.vo.AlumnosVO;

@Transactional
@Service
public class AlumnosServiceImpl implements AlumnosService {

	@Autowired
	private AlumnosDAO alumnosDAO;
	
	@Override
	public List<AlumnosVO> findAll() {
		return AlumnosBuilder.createListAlumnosVO(alumnosDAO.findAll());
	}

	@Override
	public AlumnosVO findById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlumnosVO findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AlumnosVO findByAlumnoNameAndAlumnoApPaternoAndAlumnoApMaterno(String name, String apPaterno,
			String apMaterno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(AlumnosVO alumnoVO) {
		Alumnos alumno = AlumnosBuilder.createAlumnos(alumnoVO);
		alumno.setAlumnoActivo(true);
		alumno.setFechaCreacion(Calendar.getInstance().getTime());
		alumno.setAlumnoEstatus(1);
		
		alumnosDAO.save(alumno);
	}

}
