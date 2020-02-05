package com.sae.gandhi.spring.utils;

public class SaeEnums {

	public enum Curso{
		PREPARADO(1,"PREPARADO"),
		ACTIVO(2,"ACTIVO"),
		FINALIZADO(3,"FINALIZADO"),
		CANCELADO(4,"CANCELADO");
		
		
		private Integer statusId;
		private String status;
		
		Curso(Integer statusId, String status){
			this.statusId = statusId;
			this.status = status;
		}
		
		public Integer getStatusId(){
			return statusId;
		}

		public String getStatus(){
			return status;
		}
		
		public static Curso getCurso(Integer statusId){
			switch(statusId){
			case 1:
				return PREPARADO;
			case 2: 
				return ACTIVO;
			case 3: 
				return FINALIZADO;
			case 4: 
				return CANCELADO;
			default:
				return null;
			}
		}
		
	}
	
	public enum Pago{
		PREPARADO(1,"PREPARADO"),
		COMPLETO(2,"COMPLETO"),
		PARCIAL(3,"PARCIAL"),
		ADEUDO(4,"ADEUDO"),
		CANCELADO(5,"CANCELAR");
		
		
		private Integer statusId;
		private String status;
		
		Pago(Integer statusId, String status){
			this.statusId = statusId;
			this.status = status;
		}
		
		public Integer getStatusId(){
			return statusId;
		}

		public String getStatus(){
			return status;
		}
		
		public static Pago getPago(Integer statusId){
			switch(statusId){
			case 1:
				return PREPARADO;
			case 2: 
				return COMPLETO;
			case 3: 
				return PARCIAL;
			case 4: 
				return ADEUDO;
			case 5: 
				return CANCELADO;
			default:
				return null;
			}
		}
		
	}
	
	public enum Mes{
		ENERO(1,"ENERO"),
		FEBRERO(2,"FEBRERO"),
		MARZO(3,"MARZO"),
		ABRIL(4,"ABRIL"),
		MAYO(5,"MAYO"),
		JUNIO(6,"JUNIO"),
		JULIO(7,"JULIO"),
		AGOSTO(8,"AGOSTO"),
		SEPTIEMBRE(9,"SEPTIEMBRE"),
		OCTUBRE(10,"OCTUBRE"),
		NOVIEMBRE(11,"NOVIEMBRE"),
		DICIEMBRE(12,"DICIEMBRE");
		
		
		private Integer statusId;
		private String status;
		
		Mes(Integer statusId, String status){
			this.statusId = statusId;
			this.status = status;
		}
		
		public Integer getStatusId(){
			return statusId;
		}

		public String getStatus(){
			return status;
		}
		
		public static Mes getMes(Integer statusId){
			switch(statusId){
			case 1:
				return ENERO;
			case 2: 
				return FEBRERO;
			case 3: 
				return MARZO;
			case 4: 
				return ABRIL;
			case 5: 
				return MAYO;
			case 6: 
				return JUNIO;
			case 7: 
				return JULIO;
			case 8: 
				return AGOSTO;
			case 9: 
				return SEPTIEMBRE;
			case 10: 
				return OCTUBRE;
			case 11: 
				return NOVIEMBRE;
			default:
				return DICIEMBRE;
			}
		}
	}
	
	public enum MesCalendar{
		ENERO(0,"ENERO"),
		FEBRERO(1,"FEBRERO"),
		MARZO(2,"MARZO"),
		ABRIL(3,"ABRIL"),
		MAYO(4,"MAYO"),
		JUNIO(5,"JUNIO"),
		JULIO(6,"JULIO"),
		AGOSTO(7,"AGOSTO"),
		SEPTIEMBRE(8,"SEPTIEMBRE"),
		OCTUBRE(9,"OCTUBRE"),
		NOVIEMBRE(10,"NOVIEMBRE"),
		DICIEMBRE(11,"DICIEMBRE");
		
		
		private Integer statusId;
		private String status;
		
		MesCalendar(Integer statusId, String status){
			this.statusId = statusId;
			this.status = status;
		}
		
		public Integer getStatusId(){
			return statusId;
		}

		public String getStatus(){
			return status;
		}
		
		public static MesCalendar getMes(Integer statusId){
			switch(statusId){
			case 0:
				return ENERO;
			case 1: 
				return FEBRERO;
			case 2: 
				return MARZO;
			case 3: 
				return ABRIL;
			case 4: 
				return MAYO;
			case 5: 
				return JUNIO;
			case 6: 
				return JULIO;
			case 7: 
				return AGOSTO;
			case 8: 
				return SEPTIEMBRE;
			case 9: 
				return OCTUBRE;
			case 10: 
				return NOVIEMBRE;
			default:
				return DICIEMBRE;
			}
		}
	}
	
	public enum Rol{
		ADMIN(1,"admin"),
		USER(2,"user");
		
		private Integer rolId;
		private String rolName;
		
		Rol(Integer rolId, String rolName){
			this.rolId = rolId;
			this.rolName = rolName;
		}
		
		public Integer getRolId(){
			return rolId;
		}

		public String getRolName(){
			return rolName;
		}
		
		public static Rol getRol(Integer rolId){
			switch(rolId){
			case 1:
				return ADMIN;
			default:
				return USER;
			}
		}
		
		public static String[] getAllRoles(){
			return new String[] {Rol.ADMIN.getRolName(), Rol.USER.getRolName()};
		}
	}

}
