package com.sae.gandhi.spring.service;

import java.io.InputStream;

import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CursosVO;

public interface ReportService {
	
	public InputStream generateReport(AlumnosVO alumnoVO, CursosVO cursosVO, Integer alumnoId, Integer cursoId);

}
