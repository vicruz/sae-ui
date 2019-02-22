package com.sae.gandhi.spring.scheule;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sae.gandhi.spring.service.AlumnoPagoService;


@Component
public class Schedulers {
	
	private static final Logger log = LoggerFactory.getLogger(Schedulers.class);
	
	@Autowired
	private AlumnoPagoService alumnoPagoService;
	
	//Se ejecuta todos los dias a las 00:10 a.m.
	//@Scheduled(cron="0 10 0 * * *")
	@Scheduled(cron="0 0/2 * * * *") //Para debug, cada 5 minutos
	public void updatePayments() {
		log.info("Iniciando la busqueda de pagos vencidos: " + Calendar.getInstance().getTime());
		alumnoPagoService.updateMontoFechaExceed();
		log.info("Terminando la busqueda de pagos vencidos: " + Calendar.getInstance().getTime());
	}
}
