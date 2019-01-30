package com.sae.gandhi.spring;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CostosVO;
import com.sae.gandhi.spring.vo.CursoCostosVO;
import com.sae.gandhi.spring.vo.CursosVO;

@Component
public class ScriptStart implements ApplicationRunner{
	
	private CostosService costosService;
	private CursosService cursosService;
	private AlumnosService alumnosService;
	private CursoCostosService cursoCostosService;
	private AlumnoCursoService alumnoCursoService;
	
	@Autowired
	public ScriptStart(CostosService costosService, CursosService cursosService, AlumnosService alumnosService,
			CursoCostosService cursoCostosService, AlumnoCursoService alumnoCursoService){
		this.costosService = costosService;
		this.cursosService = cursosService;
		this.alumnosService = alumnosService;
		this.cursoCostosService = cursoCostosService;
		this.alumnoCursoService = alumnoCursoService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		CostosVO costoVO = new CostosVO();
		costoVO.setCostoActivo(true);
		costoVO.setCostoMonto(BigDecimal.valueOf(1700));
		costoVO.setCostoNombre("Inscripción");
		costoVO.setFechaCreacion(Calendar.getInstance().getTime());
		costosService.save(costoVO);
		
		CursosVO cursoVO = new CursosVO();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -5);
		cursoVO.setCursoStatus(1); //Preparado
		cursoVO.setCursoFechaInicio(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		calendar.add(Calendar.MONTH, 10);
		cursoVO.setCursoFechaFin(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		cursoVO.setCursoNombre("Kinder");
		cursoVO.setFechaCreacion(Calendar.getInstance().getTime());
		cursoVO.setInscritos(10);
		cursosService.save(cursoVO);
		/*
		CursoCostosVO cursoCostoVO = new CursoCostosVO();
		cursoCostoVO.setCostoId(2);
		cursoCostoVO.setCursoCostoActivo(true);
		cursoCostoVO.setCursoCostoAplicaBeca(true);
		cursoCostoVO.setCostoId(1);
		cursoCostoVO.setCursoCostoGeneraAdeudo(true);
		cursoCostoVO.setCursoCostoPagoUnico(false);
		cursoCostoVO.setCursoCostoDiaPago(5);
		cursoCostoVO.setFechaCreacion(Calendar.getInstance().getTime());
		cursoCostosService.save(cursoCostoVO);*/
		
		AlumnosVO alumnoVO = new AlumnosVO();
		alumnoVO.setAlumnoNombre("Victor Ivan");
		alumnoVO.setAlumnoApPaterno("Cruz");
		alumnoVO.setAlumnoApMaterno("Gonzalez");
		alumnoVO.setAlumnoFechaNac(Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		alumnoVO.setAlumnoImagen(null);
		alumnoVO.setAlumnoTutor("Maria Elena Gonzalez");
		alumnoVO.setAlumnoTutorEmail("ivan@gmail.com");
		alumnoVO.setAlumnoTutorTelefono1("5552520196");
		alumnoVO.setFechaCreacion(Calendar.getInstance().getTime());
		alumnoVO.setAlumnoEstatus(1);
		alumnoVO.setAlumnoActivo(true);
		alumnoVO.setAlumnoSaldo(BigDecimal.ZERO);
		alumnosService.save(alumnoVO);
		
		AlumnosVO alumnoVO2 = new AlumnosVO();
		alumnoVO2.setAlumnoNombre("Miranda");
		alumnoVO2.setAlumnoApPaterno("Rivas");
		alumnoVO2.setAlumnoApMaterno("X");
		alumnoVO2.setAlumnoFechaNac(Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		alumnoVO2.setAlumnoImagen(null);
		alumnoVO2.setAlumnoTutor("Maria Elena Gonzalez");
		alumnoVO2.setAlumnoTutorEmail("ivan@gmail.com");
		alumnoVO2.setAlumnoTutorTelefono1("5552520196");
		alumnoVO2.setFechaCreacion(Calendar.getInstance().getTime());
		alumnoVO2.setAlumnoEstatus(1);
		alumnoVO2.setAlumnoActivo(false);
		alumnoVO2.setAlumnoSaldo(BigDecimal.ZERO);
		alumnosService.save(alumnoVO2);
		
		//////////////////////CURSO COSTO
		CursoCostosVO cursoCostoVO = new CursoCostosVO();
		cursoCostoVO.setCostoId(1);
		cursoCostoVO.setCursoCostoActivo(true);
		cursoCostoVO.setCursoCostoAplicaBeca(true);
		cursoCostoVO.setCursoCostoDiaPago(5);
		cursoCostoVO.setCursoCostoGeneraAdeudo(true);
		cursoCostoVO.setCursoCostoPagoUnico(false);
		cursoCostoVO.setCursoId(2);
		cursoCostoVO.setFechaCreacion(Calendar.getInstance().getTime());
		cursoCostosService.save(cursoCostoVO);
		
		///////////////////ALUMNO CURSO
		AlumnoCursoVO alumnoCursoVO = new AlumnoCursoVO();
		alumnoCursoVO.setAlumnoCursoActivo(true);
		alumnoCursoVO.setAlumnoCursoBeca(BigDecimal.valueOf(10));
		alumnoCursoVO.setAlumnoCursoFechaIngreso(Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		alumnoCursoVO.setAlumnoId(3);
		alumnoCursoVO.setCursoId(2);
		cursoVO.setCursoId(2);
		alumnoCursoVO.setCursoVO(cursoVO);
		alumnoCursoService.save(alumnoCursoVO);
		
		///////////////////ALUMNO PAGOS
		/*Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 5);
		AlumnoPagoVO alumnoPagosVO1 = new AlumnoPagoVO();
		AlumnoPagoVO alumnoPagosVO2 = new AlumnoPagoVO();
		AlumnoPagoVO alumnoPagosVO3 = new AlumnoPagoVO();
		AlumnoPagoVO alumnoPagosVO4 = new AlumnoPagoVO();
		AlumnoPagoVO alumnoPagosVO5 = new AlumnoPagoVO();
		
		alumnoPagosVO1.setAlumnoCursoId(6);
		alumnoPagosVO1.setEstatusId(1);
		alumnoPagosVO1.setAlumnoPagoFechaLimite(cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		alumnoPagosVO1.setAlumnoPagoFechaPago(null);
		alumnoPagosVO1.setAlumnoPagoMonto(BigDecimal.valueOf(1530));
		alumnoPagosVO1.setAlumnoPagoPago(BigDecimal.ZERO);
		alumnoPagosVO1.setCursoCostosId(5);
		alumnoPagoService.*/
		
		CostosVO costo2VO = new CostosVO();
		costo2VO.setCostoActivo(true);
		costo2VO.setCostoMonto(BigDecimal.valueOf(1800));
		costo2VO.setCostoNombre("Colegiatura");
		costo2VO.setFechaCreacion(Calendar.getInstance().getTime());
		costosService.save(costo2VO);
		
		CostosVO costo3VO = new CostosVO();
		costo3VO.setCostoActivo(true);
		costo3VO.setCostoMonto(BigDecimal.valueOf(200));
		costo3VO.setCostoNombre("Extra");
		costo3VO.setFechaCreacion(Calendar.getInstance().getTime());
		costosService.save(costo3VO);
		
		
	}

}
