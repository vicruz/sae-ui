package com.sae.gandhi.spring.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class SaeDateUtils {

	public static Calendar getCalendarFromDate(Date date){
		if(date==null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
		
	}
	
	public static Date localDateToDate(LocalDate localDate){
		if(localDate==null) return null;
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Calendar localDateToCalendar(LocalDate localDate){
		if(localDate==null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(localDateToDate(localDate));
		return cal;
	}
	
	public static LocalDate calendarToLocalDate(Calendar calendar){
		if(calendar==null) return null;
		
		return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static LocalDate dateToLocalDate(Date date){
		if(date==null) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	/**
     * Método que calcula los meses que han pasado dese fecha inicio hasta
     * fecha fin.
     * @param fechaInicio: fecha de inicio de comparación.
     * @param fechaFin: fecha de finalización de comparación.
     * @return 0 si no ha pasado un mes o si se presenta alguna exepción.
     * > 0 si han pasado almenos un mes.
     */
	public static int calcularMesesAFecha(Date fechaInicio, Date fechaFin) {
        try {
            //Fecha inicio en objeto Calendar
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            //Fecha finalización en objeto Calendar
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            //Cálculo de meses para las fechas de inicio y finalización
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            //Diferencia en meses entre las dos fechas
            int diffMonth = endMes - startMes;
            
            //Si la el día de la fecha de finalización es menor que el día de la fecha inicio
            //se resta un mes, puesto que no estaria cumpliendo otro periodo.
            //Para esto ocupo el metoddo ponerAnioMesActual
            Date aFecha = ponerAnioMesActual(fechaInicio,fechaFin).getTime();
            if(formatearDate(fechaFin, "dd/MM/yyyy").compareTo(
                    formatearDate(aFecha,"dd/MM/yyyy")) < 0){
                diffMonth = diffMonth - 1;
            }
            //Si la fecha de finalización es menor que la fecha de inicio, retorno que los meses
            // transcurridos entre las dos fechas es 0
            if(diffMonth < 0){
                diffMonth = 0;
            }
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
    }
	
	/**
     * Método que remplaza el año y el mes de fecha y pone
     * el año y mes de fechaActual
     * @param fecha: fecha a remplazar el mes y el año
     * @param fechaActual: fecha de la cual se tomara el mes y el año
     * y se colocara en fecha
     * @return Calendar con la nueva fecha calculada.
     */
    private static Calendar ponerAnioMesActual(Date fecha, Date fechaActual) {
        try {
            Calendar cActual = Calendar.getInstance();
            cActual.setTime(fechaActual);
            Calendar cfecha = Calendar.getInstance();
            cfecha.setTime(fecha);
            //la fecha nueva
            Calendar c1 = Calendar.getInstance();
            c1.set(cActual.get(Calendar.YEAR), cActual.get(Calendar.MONTH), cfecha.get(Calendar.DATE));
            return c1;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Método que formatea un afecha en base al formato pasado como
     * parámetro.
     * @param fecha: fecha a formatear
     * @param pattern: formato que se dará a la fecha.
     * @return Fecha formateada en base al formato de pattern.
     * null si se presenta alguna excepción
     */
    private static Date formatearDate(Date fecha, String pattern) {
        SimpleDateFormat formato = new SimpleDateFormat(pattern);
        Date fechaFormateada = null;
        try {
            fechaFormateada = formato.parse(formato.format(fecha));
            return fechaFormateada;
        } catch (Exception ex) {
            return fechaFormateada;
        }
    } 
}
