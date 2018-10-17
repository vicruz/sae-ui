package com.sae.gandhi.spring.ui.students;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "alumnos", layout = MainView.class)
@PageTitle("Alumnos")
public class StudentsList extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1953074122084336367L;

	private final TextField searchField = new TextField("", "Buscar Alumnos");
    private final H3 header = new H3("Alumnos");
    
    private AlumnosService alumnosService;
    
    //Gris que contendrá los costos
    private final Grid<AlumnosVO> grid = new Grid<>();
    
    @Autowired
    public StudentsList(AlumnosService alumnosService){
    	this.alumnosService = alumnosService;
    	initView();
    	addSearchBar();
    	addContent();
    	
    	updateView();
    }

    private void initView() {
        addClassName("students-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }
    
    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
//        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

//        Button newButton = new Button("Nuevo Costo", new Icon("lumo", "plus"));
        Button newButton = new Button("Nuevo Alumno");//,event -> form.open(new CostosVO(),
                //AbstractEditorDialog.Operation.ADD));
        newButton.setIcon(new Icon(VaadinIcon.PLUS));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        //newButton.addClickListener(e -> form.open(new CostosVO(),
        //        AbstractEditorDialog.Operation.ADD));

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }
    
    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);

//        grid.addColumn(AlumnosVO::getAlumnoNombre).setHeader("Nombre");

        //Se envian metodos que cumplen con la funcion requerida
        grid.addColumn(new ComponentRenderer<>(this::createDivStudents));
        //grid.addColumn(new ComponentRenderer<>(this::createInactiveButton)).setFlexGrow(0);
        grid.setSelectionMode(SelectionMode.SINGLE);
      
        container.add(header, grid);
        add(container);
    }
    
    
    private Div createDivStudents(AlumnosVO alumno) {
        Div div = new Div();
        Div divImage = new Div();
        Div divData = new Div();
    	
        div.getStyle().set("padding", "10px")
        	.set("display", "flex")
        	.set("min-width", "250px");
        
        //Color rojo cuando el alumno es inactivo, azul cuando esta activo
        /*if(alumno.getAlumnoActivo()){
        	div.getStyle().set("background-image", "linear-gradient(blue 5%, white 80%)");
        }else{
        	div.getStyle().set("background-image", "linear-gradient(red 5%, white 80%)");
        }*/
        
        
        divImage.getStyle().set("margin-right", "10px")
        	.set("width", "40px")
        	.set("height", "40px");
        
        //Para cargar la imagen dependiendo si es war o jar
        //https://vaadin.com/blog/vaadin-10-and-static-resources
        Image image = new Image("frontend/images/usuario.jpg","alt");
        image.getStyle().set("border-radius", "50%")
        	.set("width", "60px")
        	.set("height", "60px")
        	.set("background-color", "lightgray");
        
        divImage.add(image);
        
        ///////////////////////////////////////////////
        ////Horizontal para nombre de alumno y tutor
        VerticalLayout vlNameTutor = new VerticalLayout();
        
        //Nombre del alumno
        //Tamaño 14 y en negritas
        Label lbCourseTitle = new Label();
        lbCourseTitle.getStyle().set("fontSize", "14px"); 
        lbCourseTitle.getStyle().set("fontWeight", "bold");
        lbCourseTitle.setText(alumno.getAlumnoNombre()+" "+alumno.getAlumnoApPaterno()+" "+alumno.getAlumnoApMaterno());
        
        //Nombre del tutor y correo
        Label lbCoursePhone = new Label();
        lbCoursePhone.getStyle().set("fontSize", "12px");
        lbCoursePhone.setText(alumno.getAlumnoTutor() + " - " + alumno.getAlumnoTutorTelefono1());
        
        vlNameTutor.add(lbCourseTitle);
        vlNameTutor.add(lbCoursePhone);
        
        //Nombre del curso
        //TODO
        Label lbCourse = new Label();
        lbCourse.getStyle().set("fontSize", "14px")
        	.set("fontWeight", "bold")
        	.set("display", "block") //lo hace un bloque para que pueda tomar los paddings
        	.set("padding-top", "4%")
        	.set("padding-left", "10%")
        	.set("padding-right", "10%");
        lbCourse.setText("3° DE PRIMARIA 2018 - 2019");
        
        
        //Estatus de pago del alumno
        Label lbStatus = new Label();
        lbStatus.getStyle().set("fontSize", "12px")
        	.set("display", "block")
        	.set("padding-top", "4%")
        	.set("padding-left", "5%")
        	.set("padding-right", "5%");        
        lbStatus.setText("pagado");
        
        
        Div divStatus = new Div();
/*        divStatus.getStyle().set("display", "var(- -lumo-border-radius)")
        	.set("background", "var(- -lumo-shade-10pct)")
        	.set("color", "var(- -lumo-secondary-text-color)")
        	.set("padding", "2px 10px")
        	.set("font-size", "var(- -lumo-font-size-xs)")
        	.set("text-transform", "capitalize");
        
        Span span = new Span();
        span.getStyle().set("color", "var(- -lumo-success-color)")
        	.set("background", "var(- -lumo-success-color-10pct), value)");
        span.setText("Pagado");
        divStatus.add(span);
*/        
        divStatus.add(lbStatus);
        
        ///Boton para editar al alumno
        Button edit = new Button("");
//        edit.addClickListener(event -> 
//        		{edit.getUI().ifPresent(ui -> ui.navigate("cursos/edit/"+curso.getCursoId()));});
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        edit.getStyle().set("padding", "3%");
//        divStatus.add(edit);
        
        
    	/*Button edit = new Button("",event -> form.open(costo,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        */
        
        divData.add(vlNameTutor);
        
        div.add(divImage);
        div.add(divData);
        div.add(lbCourse);
//        div.add(divStatus);
        div.add(lbStatus);
        div.add(edit);
        return div;
    }
    
    //Carga los datos del grid
    private void updateView() {
        List<AlumnosVO> lstAlumnos = alumnosService.findAll();
        grid.setItems(lstAlumnos);

        /*if (searchField.getValue().length() > 0) {
            header.setText("Search for “"+ searchField.getValue() +"”");
        } else {
            header.setText("Costos");
        }*/
    }

	
}
