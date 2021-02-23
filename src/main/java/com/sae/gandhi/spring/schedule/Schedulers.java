package com.sae.gandhi.spring.schedule;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sae.gandhi.spring.service.AlumnoPagoService;
import com.sae.gandhi.spring.service.CursosService;


@Component
public class Schedulers {
	
	private static final Logger log = LoggerFactory.getLogger(Schedulers.class);
	
	@Autowired
	private AlumnoPagoService alumnoPagoService;
	
	@Autowired
	private CursosService cursosService;
	
	//Se ejecuta todos los dias a las 12:00 p.m.
	//@Scheduled(cron="0 0 12 * * *")
	@Scheduled(cron="0 0/2 * * * *") //Para debug, cada 5 minutos
	public void updatePayments() {
		log.info("Iniciando la busqueda de pagos vencidos: " + Calendar.getInstance().getTime());
		alumnoPagoService.updateMontoFechaExceed();
		log.info("Terminando la busqueda de pagos vencidos: " + Calendar.getInstance().getTime());
	}
	
	//Se ejecuta todos los dias a las 00:20 a.m.
	//@Scheduled(cron="0 20 0 * * *")
	@Scheduled(cron="0 0/10 * * * *") //Para debug cada 2 minutos
	public void updateStatedAndFinishedCourses(){
		log.info("Iniciando la actualización de estatus de cursos: " + Calendar.getInstance().getTime());
		cursosService.updateStartedCourses();
		cursosService.updateFinishedCourses();
		log.info("Termianndo la actualización de estatus de cursos: " + Calendar.getInstance().getTime());
	}
}
