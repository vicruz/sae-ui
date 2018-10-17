package com.sae.gandhi.spring.service;

import java.util.List;

import com.sae.gandhi.spring.vo.CostosVO;

public interface CostosService {

/*	private static CostosService instance;
	private static List<CostosDTO> lstCostos;
	
	public static CostosService getInstance() {
		if (instance == null) {
			instance = new CostosService();
			lstCostos = new ArrayList<>();
			instance.ensureTestData();
		}
		return instance;
	}
	
	//Datos dummies para pruebas
	public void ensureTestData() {
		
		CostosDTO costo = new CostosDTO();
		costo.setCostoId(1);
		costo.setCostoActivo(new Short("1"));
		costo.setCostoFecha(Calendar.getInstance().getTime());
		costo.setCostoNombre("Inscripcion Primaria");
		costo.setCostoMonto(BigDecimal.valueOf(1700));
		lstCostos.add(costo);
		
		costo = new CostosDTO();
		costo.setCostoId(2);
		costo.setCostoActivo(new Short("1"));
		costo.setCostoFecha(Calendar.getInstance().getTime());
		costo.setCostoNombre("Mensualidad Primaria");
		costo.setCostoMonto(BigDecimal.valueOf(1300));
		lstCostos.add(costo);
		
		costo = new CostosDTO();
		costo.setCostoId(3);
		costo.setCostoActivo(new Short("1"));
		costo.setCostoFecha(Calendar.getInstance().getTime());
		costo.setCostoNombre("Club de tareas Primaria");
		costo.setCostoMonto(BigDecimal.valueOf(600));
		lstCostos.add(costo);
	
		costo = new CostosDTO();
		costo.setCostoId(1);
		costo.setCostoActivo(new Short("1"));
		costo.setCostoFecha(Calendar.getInstance().getTime());
		costo.setCostoNombre("Utiles Primaria");
		costo.setCostoMonto(BigDecimal.valueOf(1700));
		lstCostos.add(costo);
	}
*/	
	
	public List<CostosVO> findAll();
	
	public void save(CostosVO costoDto);
	
	public void update(CostosVO costoDto);
	
	public void deactivate(CostosVO costoDto);
	
	public List<CostosVO> findByName(String name);
	
	public CostosVO findById(Integer costoId);
	
	public List<CostosVO> findAllActive();
	
	public List<CostosVO> findNotInCurso(Integer cursoId);
	
}
