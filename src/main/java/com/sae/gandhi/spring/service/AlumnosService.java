package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.AlumnosVO;

public interface AlumnosService {

	public void save(AlumnosVO alumnoVO);
	public List<AlumnosVO> findAll();
	public AlumnosVO findById();
	public AlumnosVO findByName(String name);
	public AlumnosVO findByAlumnoNameAndAlumnoApPaternoAndAlumnoApMaterno(String name, String apPaterno, String apMaterno);	
	
}
