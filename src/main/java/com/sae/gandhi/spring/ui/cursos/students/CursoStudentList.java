package com.sae.gandhi.spring.ui.cursos.students;

import java.util.List;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

@Controller
public class CursoStudentList extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6134396839514764206L;
	
    private AlumnoCursoService alumnoCursoService;
    private Integer cursoId;
    private Div viewTab = new Div();
    private final Grid<AlumnosVO> grid = new Grid<>();
    
	
    //El autowired en atributos privados no esta funcionando
    @Autowired
    public CursoStudentList(AlumnoCursoService alumnoCursoService){
    	this.alumnoCursoService = alumnoCursoService;
    	
    	initView();
        addContent();
        add(viewTab);
    }
    
    private void initView() {
        addClassName("cursosAlumnos-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        viewTab.addClassName("tab-container");
    }
    
    private void addContent() {

        grid.addColumn(AlumnosVO::getAlumnoNombreCompleto).setHeader("Alumno").setResizable(true);
        
        //Se envian metodos que cumplen con la funcion requerida
        grid.addColumn(new ComponentRenderer<>(this::createDeleteButton)).setHeader("Eliminar").setFlexGrow(0);
        grid.setSelectionMode(SelectionMode.SINGLE);
        
        grid.setWidth("80vw");
        //grid.setWidth("100%");
        
        viewTab.add(grid);
    }

   
    //Boton de Eliminar
    private Button createDeleteButton(AlumnosVO curso) {
        Button edit = new Button();
        edit.addClickListener(event -> {
        	deleteStudent(curso);
        });
        edit.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Eliminar");
        return edit;
    }
  
    public void updateView(Integer cursoId) {
        List<AlumnosVO> lstAlumnos = alumnoCursoService.findByCurso(cursoId);
        grid.setItems(lstAlumnos);
        this.cursoId = cursoId;
    }
    
 
 // Eliminar
 	private void deleteStudent(AlumnosVO alumnosVO) {
 		ConfirmDialog
         .createQuestion()
         .withCaption("Eliminar Alumno")
         .withMessage("Deseas eliminar el Alumno del Curso?")
         .withOkButton(() -> {
        	 //Buscar el alumnoCursoId y posteriormente, eliminarlo
        	 AlumnoCursoVO vo = alumnoCursoService.findByCursoIdAndAlumnoId(cursoId, alumnosVO.getAlumnoId());
        	 
        	 if(alumnoCursoService.delete(vo.getAlumnoCursoId())){
         		Notification.show("El alumno "+alumnosVO.getAlumnoNombreCompleto() +" se ha dado de baja del curso", 3000, Position.BOTTOM_END);
         		updateView(cursoId);
         	}else{
         		Notification.show("Ocurrió un error al dar de baja el alumno "+alumnosVO.getAlumnoNombreCompleto(), 
         				3000, Position.BOTTOM_END);
         	}
         }, ButtonOption.focus(), ButtonOption.caption("SI"))
         .withCancelButton(ButtonOption.caption("NO"))
         .open();
 	}
    
}
