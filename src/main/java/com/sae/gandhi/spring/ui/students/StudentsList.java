package com.sae.gandhi.spring.ui.students;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.utils.StreamImage;
import com.sae.gandhi.spring.vo.AlumnosListVO;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

@Route(value = "alumnos", layout = MainView.class)
@PageTitle("Alumnos")
public class StudentsList extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1953074122084336367L;

	private final TextField searchField = new TextField("", "Buscar Alumnos");
	private final Checkbox cbAlumnos = new Checkbox("Ver Todos", false);
    
    private AlumnosService alumnosService;
    
    private StudentDialog form;
    
    //Gris que contendrá los costos
    private final Grid<AlumnosListVO> grid = new Grid<>();
    List<AlumnosListVO> lstAlumnos;
    
    @Autowired
    public StudentsList(AlumnosService alumnosService){
    	this.alumnosService = alumnosService;
    	initView();
    	addSearchBar();
    	addContent();
    	
    	loadData();
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
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        Button newButton = new Button("Nuevo Alumno");
        newButton.setIcon(new Icon(VaadinIcon.PLUS));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> openStudentDialog());
        	
        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }
    
    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);


        //Se envian metodos que cumplen con la funcion requerida
        grid.addColumn(new ComponentRenderer<>(this::createDivStudents));
        grid.setSelectionMode(SelectionMode.SINGLE);
        
        //Check para ver a todos los alumnos
        Label lbTitulo = new Label();
        lbTitulo.getStyle().set("fontSize", "18px"); 
        lbTitulo.getStyle().set("fontWeight", "bold");
        lbTitulo.setWidth("80%");
        lbTitulo.setText("Alumnos");
        
        HorizontalLayout hlContent = new HorizontalLayout();
        hlContent.setPadding(true);
        hlContent.getStyle().set("padding", "0px");
        hlContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        
        hlContent.add(lbTitulo, cbAlumnos);
        hlContent.setVerticalComponentAlignment(Alignment.CENTER, cbAlumnos);
        
        //Evento de check
        cbAlumnos.addValueChangeListener(evt -> {
        	loadData();
        });
        
        
        container.add(hlContent, grid);
        add(container);
    }
    
    
    private Div createDivStudents(AlumnosListVO alumno) {
        Div div = new Div();
        Div divImage = new Div();
        Div divData = new Div();
        Image image;
        AbstractStreamResource asr;
    	
        div.getStyle().set("padding", "10px")
        	.set("display", "flex")
        	.set("min-width", "250px");
        
        //Color rojo cuando el alumno es inactivo, azul cuando esta activo
        /*div.getStyle().set("border-top-style", "outset");
        if(alumno.getAlumnoActivo()){
        	div.getStyle().set("border-top-color", "blue");
        }else{
        	div.getStyle().set("border-top-color", "gray");
        }*/
        
        
        divImage.getStyle().set("margin-right", "10px")
        	.set("width", "40px")
        	.set("height", "40px");
        
        //Para cargar la imagen dependiendo si es war o jar
        //https://vaadin.com/blog/vaadin-10-and-static-resources
        //Image image = new Image("frontend/images/usuario.jpg","alt");
        
        if(Objects.isNull(alumno.getAlumnoImagen())){
        	image = new Image("frontend/images/usuario.jpg","");
        }else{
        	//ByteArrayInputStream bis = new ByteArrayInputStream(alumno.getAlumnoImagen());
        	StreamImage stream = new StreamImage(alumno.getAlumnoImagen());
        	asr = new StreamResource("image", stream);
        	image = new Image(asr,"");
        }
        
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
        lbCoursePhone.setText((Objects.nonNull(alumno.getAlumnoTutor())?alumno.getAlumnoTutor():"") + " - " + 
        	(Objects.nonNull(alumno.getAlumnoTutorTelefono1())?alumno.getAlumnoTutorTelefono1():""));
        
        vlNameTutor.add(lbCourseTitle);
        vlNameTutor.add(lbCoursePhone);
        
        divData.add(vlNameTutor);
        
        ///////////////////////////////////////
        //Datos del curso y estatus del alumno
        HorizontalLayout hlDataCourse = new HorizontalLayout();
        hlDataCourse.setPadding(true);
        hlDataCourse.setBoxSizing(BoxSizing.BORDER_BOX);
        hlDataCourse.setWidth("100%");
        hlDataCourse.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        
        //Nombre del curso
        Label lbCourse = new Label();
        lbCourse.getStyle().set("fontSize", "14px")
        	.set("fontWeight", "bold");
        lbCourse.setText(alumno.getCursoNombre());
        lbCourse.setWidth("70%");
        hlDataCourse.add(lbCourse);
        hlDataCourse.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, lbCourse);
        
        
        //Estatus de pago del alumno
        //TODO
        /*
        Label lbStatus = new Label();
        lbStatus.getStyle().set("fontSize", "12px");
        lbStatus.setText("pagado");
        lbStatus.setWidth("15%");
        lbStatus.getStyle().set("background", "ffffc0");
        hlDataCourse.add(lbStatus);
        hlDataCourse.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, lbStatus);
        */
        ///Boton para editar al alumno
        Button edit = new Button("");
        edit.addClickListener(event -> 
        		{edit.getUI().ifPresent(ui -> ui.navigate("alumnos/edit/"+alumno.getAlumnoId()));});
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        edit.setWidth("15%");
        hlDataCourse.add(edit);
        
        ///Boton para ir a pagos del alumno
        Button btnPayments = new Button("");
        btnPayments.addClickListener(event -> 
        		{edit.getUI().ifPresent(ui -> ui.navigate("alumnos/pagos/"+alumno.getAlumnoId()));});
        btnPayments.setIcon(new Icon(VaadinIcon.BOOK_DOLLAR));
        btnPayments.addClassName("review__edit");
        btnPayments.getElement().setAttribute("theme", "tertiary");
        btnPayments.getElement().setAttribute("title", "Pagos");
        btnPayments.setWidth("15%");
        if(!alumno.getAlumnoActivo()){
        	btnPayments.setEnabled(false);
        }
        hlDataCourse.add(btnPayments);
                
        
        div.add(divImage);
        div.add(divData);
        div.add(hlDataCourse);
        return div;
    }
    
    //Carga los datos del grid
    private void loadData() {
    	if(!cbAlumnos.getValue()){
    		lstAlumnos = alumnosService.getAlumnosListActive();
    	}else{
    		lstAlumnos = alumnosService.getAlumnosList();
    	}
    	grid.setItems(lstAlumnos);
        
    }
    
    private void updateView() {
    	List<AlumnosListVO> lstAlumnosGrid = new ArrayList<>();
        if (searchField.getValue().length() > 0 && !searchField.getValue().trim().equals("")) {
        	for(AlumnosListVO vo: lstAlumnos){
        		String nombre = vo.getAlumnoNombre()+" "+vo.getAlumnoApPaterno()+" "+vo.getAlumnoApMaterno();
        		if(nombre.toUpperCase().contains(searchField.getValue().toUpperCase()) || 
        				vo.getAlumnoTutor().toUpperCase().contains(searchField.getValue().toUpperCase())){
        			lstAlumnosGrid.add(vo);
        		}
        	}
        } else {
        	lstAlumnosGrid = lstAlumnos;
        }
        grid.setItems(lstAlumnosGrid);
    }
    
  //Metodo de salvar
    private void saveStudent(AlumnosVO alumnosVo,
            AbstractEditorDialog.Operation operation) {

    	if(Objects.nonNull(form.getBuffer().getInputStream())){
    		try {
				alumnosVo.setAlumnoImagen(IOUtils.toByteArray(form.getBuffer().getInputStream()));
			} catch (IOException e) {}
    	}
    	
    	alumnosService.save(alumnosVo);
    	form.setBuffer(new MemoryBuffer());
    	form.setUpload(new Upload());
    	
    	
        Notification.show(
                "Alumno " + alumnosVo.getAlumnoNombre() + " agregado", 3000, Position.BOTTOM_END);
        loadData();
    }
    
    //Eliminar
    private void deleteStudent(AlumnosVO alumnosVo) {
    	/*costosService.deactivate(costos);
    	
        Notification.show("Costo "+costos.getCostoNombre() +" eliminado", 3000, Position.BOTTOM_END);
        updateView();*/
    }

	private void openStudentDialog(){
		
		form = new StudentDialog(this::saveStudent, this::deleteStudent);
		form.open(new AlumnosVO(),
    			AbstractEditorDialog.Operation.ADD);
    	form.getTxtName().getElement().callFunction("focus");
    
	}
}
