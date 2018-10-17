package com.sae.gandhi.spring;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CostosVO;
import com.sae.gandhi.spring.vo.CursosVO;

@Component
public class ScriptStart implements ApplicationRunner{
	
	private CostosService costosService;
	private CursosService cursosService;
	private AlumnosService alumnosService;
	
	@Autowired
	public ScriptStart(CostosService costosService, CursosService cursosService, AlumnosService alumnosService){
		this.costosService = costosService;
		this.cursosService = cursosService;
		this.alumnosService = alumnosService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CostosVO costoVO = new CostosVO();
		costoVO.setCostoActivo(true);
		costoVO.setCostoMonto(BigDecimal.valueOf(1700));
		costoVO.setCostoNombre("Inscripcion");
		costoVO.setFechaCreacion(Calendar.getInstance().getTime());
		costosService.save(costoVO);
		
		CursosVO cursoVO = new CursosVO();
		cursoVO.setCursoStatus(1); //Preparado
		cursoVO.setCursoFechaFin(Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		cursoVO.setCursoFechaInicio(Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		cursoVO.setCursoNombre("Kinder");
		cursoVO.setFechaCreacion(Calendar.getInstance().getTime());
		cursoVO.setInscritos(10);
		cursosService.save(cursoVO);
		
		AlumnosVO alumnoVO = new AlumnosVO();
		alumnoVO.setAlumnoNombre("Victor Ivan");
		alumnoVO.setAlumnoApPaterno("Cruz");
		alumnoVO.setAlumnoApMaterno("Gonzalez");
		alumnoVO.setAlumnoFechaNac(Calendar.getInstance().getTime());
		alumnoVO.setAlumnoImagen(null);
		alumnoVO.setAlumnoTutor("Maria Elena Gonzalez");
		alumnoVO.setAlumnoTutorEmail("ivan@gmail.com");
		alumnoVO.setAlumnoTutorTelefono1("5552520196");
		alumnoVO.setFechaCreacion(Calendar.getInstance().getTime());
		alumnoVO.setAlumnoEstatus(1);
		alumnoVO.setAlumnoActivo(true);
		alumnosService.save(alumnoVO);
		
		AlumnosVO alumnoVO2 = new AlumnosVO();
		alumnoVO2.setAlumnoNombre("Miranda");
		alumnoVO2.setAlumnoApPaterno("Rivas");
		alumnoVO2.setAlumnoApMaterno("X");
		alumnoVO2.setAlumnoFechaNac(Calendar.getInstance().getTime());
		alumnoVO2.setAlumnoImagen(null);
		alumnoVO2.setAlumnoTutor("Maria Elena Gonzalez");
		alumnoVO2.setAlumnoTutorEmail("ivan@gmail.com");
		alumnoVO2.setAlumnoTutorTelefono1("5552520196");
		alumnoVO2.setFechaCreacion(Calendar.getInstance().getTime());
		alumnoVO2.setAlumnoEstatus(1);
		alumnoVO2.setAlumnoActivo(false);
		alumnosService.save(alumnoVO2);
		
	}

}
