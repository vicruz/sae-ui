package com.sae.gandhi.spring.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.AlumnosDAO;
import com.sae.gandhi.spring.dao.CursosDAO;
import com.sae.gandhi.spring.entity.Alumnos;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.utils.builder.AlumnosBuilder;
import com.sae.gandhi.spring.vo.AlumnosListVO;
import com.sae.gandhi.spring.vo.AlumnosVO;

@Transactional
@Service
public class AlumnosServiceImpl implements AlumnosService {

	@Autowired
	private AlumnosDAO alumnosDAO;
	
	@Autowired
	private CursosDAO cursosDAO;
	
	@Override
	public List<AlumnosVO> findAll() {
		return AlumnosBuilder.createListAlumnosVO(alumnosDAO.findAll());
	}

	@Override
	public AlumnosVO findById(Integer alumnoId) {
		return AlumnosBuilder.createAlumnosVO(alumnosDAO.getOne(alumnoId));
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
		alumno.setAlumnoSaldo(BigDecimal.ZERO);
		
		alumnosDAO.save(alumno);
	}

	@Override
	public void update(AlumnosVO alumnoVO) {
		Alumnos alumno = AlumnosBuilder.createAlumnos(alumnoVO);
		alumnosDAO.save(alumno);
	}

	@Override
	public void changeActivo(boolean status, Integer alumnoId) {
		Optional<Alumnos> optionalAlumno = alumnosDAO.findById(alumnoId);
		Alumnos alumno;
		if(optionalAlumno.isPresent()){
			alumno = optionalAlumno.get();
			alumno.setAlumnoActivo(status);
		}
		//alumnosDAO.changeActivo(status, alumnoId);
	}

	@Override
	public List<AlumnosListVO> getAlumnosList() {
		List<AlumnosListVO> lst = alumnosDAO.getAlumnosList(); 

		//Obtener el curso del alumno
		for(AlumnosListVO vo : lst){
			vo.setCursoNombre(cursosDAO.getGradeCourseByStudent(vo.getAlumnoId()));
		}
		
		return lst;
	}

	@Override
	public List<AlumnosListVO> getAlumnosListActive() {
		List<AlumnosListVO> lst = alumnosDAO.getAlumnosListActive(); 

		//Obtener el curso del alumno
		for(AlumnosListVO vo : lst){
			vo.setCursoNombre(cursosDAO.getGradeCourseByStudent(vo.getAlumnoId()));
		}
		
		return lst;
	}

}
