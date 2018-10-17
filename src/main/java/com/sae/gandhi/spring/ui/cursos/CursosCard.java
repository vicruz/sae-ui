package com.sae.gandhi.spring.ui.cursos;

import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Tarjeta de cursos, esta tarjeta contiene # de personas inscritas, Nombre del curso, fecha de inicio, 
 * fecha final y si se encuentra activo o no 
 * @author User
 *
 */
public class CursosCard extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private enum Status{
		ACTIVE,INACTIVE;
	}
	
	
	public CursosCard(CursosVO cursosVo){
		setAlignItems(Alignment.CENTER);
		addClassName("card-container");
		
//		getStyle().set("minWith", "250px")
//			.set("padding", "5px")
//			.set("display", "block");
		
		//Numero de personas inscritas
		Icon icon = new Icon(VaadinIcon.ACADEMY_CAP);
//		icon.setWidth("40px");
//		icon.setHeight("40px");
		icon.getStyle().set("borderRadius", "50%"); //Se hace redondo
		icon.getStyle().set("backgroundColor", "lightgray");
		
		//Contenedor que mantendrá el icono a la izquierda
		Div divQuantity = new Div(icon);
		divQuantity.getStyle().set("marginLeft", "30px");
		divQuantity.getStyle().set("marginRight", "20px");
		divQuantity.setWidth("40px");
		divQuantity.setHeight("40px");
        
        //Nombre del curso, fecha de inicio y fecha final
        Label courseTitle = new Label();
        Label courseInit = new Label();
        Label courseEnd = new Label();
        Label courseStatus = new Label();
        
        //Tamaño 13 y en negritas
        courseTitle.getStyle().set("fontSize", "13px"); 
        courseTitle.getStyle().set("fontWeight", "bold");
        courseTitle.setText(cursosVo.getCursoNombre());
        
        //Fecha de inicio y fin de tamaño 11
        courseInit.getStyle().set("fontSize", "11px");
        courseInit.setText(cursosVo.getCursoFechaInicio().toString());
        courseEnd.getStyle().set("fontSize", "11px");
        courseEnd.setText(cursosVo.getCursoFechaFin().toString());
        
        //Si esta activo, se pondra en verde
        courseEnd.getStyle().set("fontSize", "10px");
        /*if(cursosVo.getCursoActivo()){
        	courseStatus.getStyle().set("color", "green");
        	courseStatus.setText(Status.ACTIVE.toString());
        }else{ 
        	courseStatus.getStyle().set("color", "red");
        	courseStatus.setText(Status.INACTIVE.toString());
        }*/
        
        HorizontalLayout hlDates = new HorizontalLayout(courseInit, courseEnd);
        VerticalLayout vlData = new VerticalLayout(courseTitle, hlDates);
        Div divStatus = new Div(courseStatus);
        divStatus.getStyle().set("marginRight", "30px")
        	.set("marginRight", "10px");
        divStatus.setWidth("40px");
        divStatus.setHeight("40px");
        
        add(divQuantity, vlData, divStatus);
        
	}

}
