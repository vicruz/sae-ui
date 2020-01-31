package com.sae.gandhi.spring.ui.students;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.ui.cursos.pagos.CursoCostosList;
import com.sae.gandhi.spring.ui.students.cursos.AlumnoCursoAddDialog;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "alumnos/edit", layout = MainView.class)
@PageTitle("Alumno")
public class StudentsEditorPage extends VerticalLayout implements HasUrlParameter<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CURSOS = "Cursos";
	// private static final String ALUMNOS = "Alumnos";
	// private CursoCostoEditorDialog formCostoAdd;
	private Binder<AlumnosVO> binder = new Binder<>();

	private Integer alumnoId;
	private AlumnosVO alumnoVO;

	private AlumnosService alumnosService;
	private CursosService cursosService;
//	private CursoCostosList cursoCostosList;
//	private CostosService costosService;
	private CursoCostosService cursoCostoService;
	private AlumnoCursoService alumnoCursoService;

	private final H4 header = new H4("Editar Alumno");
//	private final DatePicker startDatePicker = new DatePicker("Inicio Curso");
//	private final DatePicker endDatePicker = new DatePicker("Fin Curso");
	private HorizontalLayout hlContent = new HorizontalLayout();
	private Label lbStatus = new Label();
	private TextField txtName = new TextField("Nombre");
	private TextField txtApPaterno = new TextField("Ap. Paterno");
	private TextField txtApMaterno = new TextField("Ap. Materno");
	private TextField txtTutor = new TextField("Tutor");
	private TextField txtEmail = new TextField("Email");
	private DatePicker dtFechaNac = new DatePicker();
	private TextField txtPhone1 = new TextField("Telefono 1");
	private TextField txtPhone2 = new TextField("Telefono 2");

	// Grid que contendrá los cursos del alumno
	private final Grid<AlumnoCursoVO> grid = new Grid<>();

	private Button btnChangeStatus;
	private Button btnSave;
	private Button addButton = new Button();
	private Tabs tabs = new Tabs();

	// Image
	private Upload upload;
	private MemoryBuffer buffer;

	// Dialog
	private AlumnoCursoAddDialog formCursoAdd;

	@Autowired
	public StudentsEditorPage(AlumnosService alumnosService, CursosService cursosService,
			CursoCostosList cursoCostosList, CostosService costosService, CursoCostosService cursoCostoService,
			AlumnoCursoService alumnoCursoService) {
		this.alumnosService = alumnosService;
		this.cursosService = cursosService;
//		this.cursoCostosList = cursoCostosList;
//		this.costosService = costosService;
		this.cursoCostoService = cursoCostoService;
		this.alumnoCursoService = alumnoCursoService;
	}

	@Override
	public void setParameter(BeforeEvent event, Integer parameter) {
		alumnoId = parameter;
		// formCostoAdd = new CursoCostoEditorDialog(this::saveCursoCosto,
		// this::deleteCursoCosto, this.costosService, cursoId);
		//hlContent.setHeight("1200px");
		hlContent.getStyle().set("padding-bottom", "20px");

		loadData();
		addTitle();
		add(hlContent);
		addImage();
		addFields();
		addButtons();
		// addNameField();
		// addDatesField();
		addTabs();

		// Iniciar con el focus en el nombre
		// tfCursoName.focus();
	}

	private void loadData() {
		alumnoVO = alumnosService.findById(alumnoId);
		// Obtener cursos
	}

	private void addTitle() {
		HorizontalLayout hl = new HorizontalLayout();

		header.getStyle().set("width", "50%");
		lbStatus.getStyle().set("textAlign", "right").set("width", "50%").set("paddingTop", "30px");
		hl.add(header, lbStatus);
		//hl.setSizeFull();
		add(hl);
	}

	private void addFields() {

		VerticalLayout vlStudentData = new VerticalLayout();
		HorizontalLayout hlStudent = new HorizontalLayout();
		HorizontalLayout hlParent = new HorizontalLayout();
		HorizontalLayout hlParentPhone = new HorizontalLayout();

		txtName.setValue(alumnoVO.getAlumnoNombre());
		binder.forField(txtName).withConverter(String::trim, String::trim).withNullRepresentation("") // cuando no existe un texto, debe haber una validación para el bindeo
				.withValidator(new StringLengthValidator("El nombre debe ser por lo menos 3 caracteres", 3, null))
				.bind(AlumnosVO::getAlumnoNombre, AlumnosVO::setAlumnoNombre); // Establece setter y getter para su bindeo

		txtApPaterno.setValue(alumnoVO.getAlumnoApPaterno());
		binder.forField(txtApPaterno).withConverter(String::trim, String::trim).withNullRepresentation("") // cuando no existe un texto, debe haber una validación para el bindeo
				.withValidator(new StringLengthValidator("El apellido debe ser por lo menos 3 caracteres", 3, null))
				.bind(AlumnosVO::getAlumnoApPaterno, AlumnosVO::setAlumnoApPaterno); // Establece setter y getter para su bindeo

		txtApMaterno.setValue(alumnoVO.getAlumnoApMaterno());
		binder.forField(txtApMaterno).withConverter(String::trim, String::trim).withNullRepresentation("") // cuando no existe un texto, haber una validación para el bindeo
				.withValidator(new StringLengthValidator("El apellido debe ser por lo menos 3 caracteres", 3, null))
				.bind(AlumnosVO::getAlumnoApMaterno, AlumnosVO::setAlumnoApMaterno); // Establece setter y getter para su bindeo

		hlStudent.add(txtName);
		hlStudent.add(txtApPaterno);
		hlStudent.add(txtApMaterno);

		dtFechaNac.setValue(alumnoVO.getAlumnoFechaNac());
		binder.forField(dtFechaNac).bind(AlumnosVO::getAlumnoFechaNac, AlumnosVO::setAlumnoFechaNac); // Establece setter y getter para su bindeo

		txtTutor.setValue(alumnoVO.getAlumnoTutor());
		binder.forField(txtTutor).withConverter(String::trim, String::trim) // Quita espacios a la cadena introducida
				.withNullRepresentation("") // cuando no existe un texto, debe haber una validación para el bindeo 
				.bind(AlumnosVO::getAlumnoTutor, AlumnosVO::setAlumnoTutor); // // Establece setter y getter para su bindeo

		txtEmail.setValue(alumnoVO.getAlumnoTutorEmail());
		binder.forField(txtEmail).withConverter(String::trim, String::trim) // Quita espacios a la cadena introducida
				.withValidator(new EmailValidator("Email incorrecto")).withNullRepresentation("") // cuando no existe un texto, debe haber una validación para el bindeo 
				.bind(AlumnosVO::getAlumnoTutorEmail, AlumnosVO::setAlumnoTutorEmail); // Establece setter y getter para su bindeo

		dtFechaNac.setLabel("Fecha de Nacimiento");
		hlParent.add(dtFechaNac);
		hlParent.add(txtTutor);
		hlParent.add(txtEmail);

		if (Objects.nonNull(alumnoVO.getAlumnoTutorTelefono1())) {
			txtPhone1.setValue(alumnoVO.getAlumnoTutorTelefono1());
		}
		if (Objects.nonNull(alumnoVO.getAlumnoTutorTelefono2())) {
			txtPhone2.setValue(alumnoVO.getAlumnoTutorTelefono2());
		}

		binder.forField(txtPhone1).withConverter(String::trim, String::trim) // Quita espacios a la cadena introducida
				.withNullRepresentation("") // cuando no existe un texto, debe haber una validación para el bindeo  
				.bind(AlumnosVO::getAlumnoTutorTelefono1, AlumnosVO::setAlumnoTutorTelefono1); // Establece setter y getter para su bindeo

		binder.forField(txtPhone2).withConverter(String::trim, String::trim) // Quita espacios a la cadena introducida
				.withNullRepresentation("") // cuando no existe un texto, debe haber una validación para el bindeo
				.bind(AlumnosVO::getAlumnoTutorTelefono2, AlumnosVO::setAlumnoTutorTelefono2); // Establece setter y getter para su bindeo

		hlParentPhone.add(txtPhone1);
		hlParentPhone.add(txtPhone2);

		vlStudentData.getStyle().set("height", "100%");
		vlStudentData.add(hlStudent);
		vlStudentData.add(hlParent);
		vlStudentData.add(hlParentPhone);

		hlContent.add(vlStudentData);
	}

	private void addImage() {
		Div div = new Div();
		Div divImage = new Div();
		Component componentInit;

		VerticalLayout vl = new VerticalLayout();

		buffer = new MemoryBuffer();
		upload = new Upload(buffer);
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

		// Cargar la imagen por default
		if (Objects.isNull(alumnoVO.getAlumnoImagen())) {
			InputStream is = null;
			try {
				// File fileImage = new File("frontend/images/usuario.jpg");
				is = this.getClass().getResourceAsStream("/META-INF/resources/frontend/images/usuario.jpg");
				// is = new BufferedInputStream(new FileInputStream(fileImage));
			} catch (Exception e) {
			}
			componentInit = createComponent("image/jpeg", "usuario.jpg", is);
			showOutput("usuario.jpg", componentInit, divImage);
		} else {
			ByteArrayInputStream bis = new ByteArrayInputStream(alumnoVO.getAlumnoImagen());
			componentInit = createComponent("image/jpeg", "alumno.jpg", bis);
			showOutput("alumno.jpg", componentInit, divImage);
		}

		upload.addSucceededListener(event -> {
			Component component = createComponent(event.getMIMEType(), event.getFileName(), buffer.getInputStream());
			divImage.removeAll();
			showOutput(event.getFileName(), component, divImage);
		});

		vl.add(upload);
		vl.add(divImage);
		div.add(vl);
		hlContent.add(div);
	}

	private void addButtons() {
		HorizontalLayout hlButtons = new HorizontalLayout();
		hlButtons.getStyle().set("align-items", "start");
		btnSave = new Button("Guardar");
		btnSave.addClickListener(e -> saveButton());
		//btnSave.getStyle().set("padding-top", "10px");
		
		if(alumnoVO.getAlumnoActivo()){
			btnChangeStatus = new Button("Baja");			
		}else{
			btnChangeStatus = new Button("Activar");
		}
		btnChangeStatus.addClickListener(e -> changeStatusButton());
		//btnChangeStatus.getStyle().set("padding-top", "10px");
		
		hlButtons.add(btnSave,btnChangeStatus);
		add(hlButtons);
	}

	// private void addTabs() {
	// HorizontalLayout hlTab = new HorizontalLayout();
	// Tab tabCursos = new Tab(CURSOS);
	// Button addButton = new Button();
	// addButton.setIcon(new Icon(VaadinIcon.PLUS));
	// addButton.getStyle().set("borderRadius", "50%"); // Se hace redondo
	// addButton.getStyle().set("backgroundColor", "lightgray");
	// addButton.addClickListener(event -> addEventButton());
	//
	// // tabs.add(tabCostos, tabStudents);
	// tabs.add(tabCursos);
	// tabs.setFlexGrowForEnclosedTabs(1);
	//
	// hlTab.add(tabs, addButton);
	// hlTab.setVerticalComponentAlignment(Alignment.START, tabs);
	// hlTab.setVerticalComponentAlignment(Alignment.END, addButton);
	//
	// Div divCursos = new Div();
	// divCursos.add(cursoCostosList);
	// divCursos.setHeight("30vh");
	//
	// /*
	// * Div divStudents = new Div(); divStudents.setText(ALUMNOS);
	// * divStudents.setVisible(false);
	// */
	// Map<Tab, Div> tabsToPages = new HashMap<>();
	// tabsToPages.put(tabCursos, divCursos);
	// // tabsToPages.put(tabStudents, divStudents);
	// Div pages = new Div(divCursos);// , divStudents);
	// Set<Component> pagesShown =
	// Stream.of(divCursos).collect(Collectors.toSet());
	//
	// tabs.addSelectedChangeListener(event -> {
	// pagesShown.forEach(page -> page.setVisible(false));
	// pagesShown.clear();
	// Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
	// selectedPage.setVisible(true);
	// pagesShown.add(selectedPage);
	//
	// System.out.println(tabs.getSelectedTab().getLabel());
	// });
	//
	// add(hlTab, pages);
	// }

	private void addTabs() {
		HorizontalLayout hlTab = new HorizontalLayout();
		Tab tabCursos = new Tab(CURSOS);
//		Button addButton = new Button();
		addButton.setIcon(new Icon(VaadinIcon.PLUS));
		addButton.getStyle().set("borderRadius", "50%"); // Se hace redondo
		addButton.getStyle().set("backgroundColor", "lightgray");
		addButton.addClickListener(event -> addEventButton());
		addButton.setEnabled(alumnoVO.getAlumnoActivo());

		tabs.add(tabCursos);
		tabs.setFlexGrowForEnclosedTabs(1);

		hlTab.add(tabs, addButton);
		hlTab.getStyle().set("padding-bottom", "20px");

		// grid.setHeight("90px"); //
		grid.addColumn(AlumnoCursoVO::getCursoNombre).setHeader("Curso").setResizable(true);
		grid.addColumn(new LocalDateRenderer<>(AlumnoCursoVO::getCursoFechaInicio,DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
			.setHeader("Inicio").setResizable(true);
		grid.addColumn(new LocalDateRenderer<>(AlumnoCursoVO::getCursoFechaFin,DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
				.setHeader("Fin").setResizable(true);
		grid.addColumn(new LocalDateRenderer<>(AlumnoCursoVO::getAlumnoCursoFechaIngreso,DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
				.setHeader("Ingreso").setResizable(true);
		// grid.addColumn(AlumnoCursoVO::getAlumnoCursoFechaLimite).setHeader("Limite").setResizable(true);
		/*grid.addColumn(new NumberRenderer<>(AlumnoCursoVO::getCostoMonto, NumberFormat.getCurrencyInstance()))
				.setHeader("Costo").setWidth("4em").setResizable(true);*/
		grid.addColumn(AlumnoCursoVO::getAlumnoCursoEstatus).setHeader("Estatus").setResizable(true);
		// Se envian metodos que cumplen con la funcion requerida
		grid.addColumn(new ComponentRenderer<>(this::createCoursePayButton)).setHeader("Pagos").setFlexGrow(0);
		grid.addColumn(new ComponentRenderer<>(this::createCourseEditButton)).setHeader("Editar").setFlexGrow(0);
		grid.addColumn(new ComponentRenderer<>(this::createCourseCancelButton)).setHeader("Baja").setFlexGrow(0);
		grid.setSelectionMode(SelectionMode.SINGLE);
		updateView();
		add(hlTab, grid);
	}

	private void saveButton() {
		binder.writeBeanIfValid(alumnoVO);

		try {
			if (Objects.nonNull(buffer.getInputStream())) {
				alumnoVO.setAlumnoImagen(IOUtils.toByteArray(buffer.getInputStream()));
			}
		} catch (IOException e) {
		}

		alumnosService.update(alumnoVO);
		Notification.show("Alumno actualizado", 5000, Notification.Position.BOTTOM_END);
	}
	
	private void changeStatusButton(){
		String mensaje;
		String mensajeNotification;
		if(alumnoVO.getAlumnoActivo()){
			mensaje = "Deseas dar de Baja al Alumno?";
			mensajeNotification = "Alumno dado de baja";
		}else{
			mensaje = "Deseas dar de Alta al Alumno?";
			mensajeNotification = "Alumno dado de alta";
		}
		
		
		ConfirmDialog .createQuestion() .withCaption("Estatus de Alumno")
		  .withMessage(mensaje) .withOkButton(() -> {
			  alumnosService.changeActivo(!alumnoVO.getAlumnoActivo(), alumnoVO.getAlumnoId());			  
			  alumnoVO.setAlumnoActivo(!alumnoVO.getAlumnoActivo());
			  
			  if(alumnoVO.getAlumnoActivo()){
					btnChangeStatus.setText("Baja");
					addButton.setEnabled(true);
				}else{
					btnChangeStatus.setText("Activar");
					addButton.setEnabled(false);
					//Desactivar todos sus cursos
					List<AlumnoCursoVO> lstAlumnos = alumnoCursoService.findByStudentActive(alumnoId);
					for(AlumnoCursoVO vo: lstAlumnos){
						if(alumnoCursoService.delete(vo.getAlumnoCursoId())){
			        		Notification.show("El curso "+vo.getCursoNombre() +" se ha dado de baja", 3000, Position.BOTTOM_END);
			        		updateView();
			        	}else{
			        		Notification.show("Ocurrió un error al dar de baja el curso "+ vo.getCursoNombre(), 
			        				3000, Position.BOTTOM_END);
			        	}
					}
					
				}
			  
			  Notification.show(mensajeNotification,
					  3000, Position.BOTTOM_END); }, ButtonOption.focus(),
				  ButtonOption.caption("SI"))
		  	.withCancelButton(ButtonOption.caption("NO")) .open();
	}

	/// Boton Add de tabs
	private void addEventButton() {
		AlumnoCursoVO alumnoCursoVO = new AlumnoCursoVO(); 
		alumnoCursoVO.setAlumnoId(alumnoId);
		formCursoAdd = new AlumnoCursoAddDialog(this::saveAlumnoCurso, this::deleteAlumnoCurso, 
				this.cursosService,	cursoCostoService, alumnoId);
		formCursoAdd.init(AbstractEditorDialog.Operation.ADD); 
		formCursoAdd.open(alumnoCursoVO,
		  AbstractEditorDialog.Operation.ADD); 
	}



	///////////////////////////////////////////////////
	/// Metodos de carga de imagen
	///////////////////////////////////////////////////
	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	private Component createComponent(String mimeType, String fileName, InputStream stream) {
		if (mimeType.startsWith("text")) {
			String text = "";
			try {
				text = IOUtils.toString(stream, "UTF-8");
			} catch (IOException e) {
				text = "exception reading stream";
			}
			return new Text(text);
		} else if (mimeType.startsWith("image")) {
			Image image = new Image();
			try {

				byte[] bytes = IOUtils.toByteArray(stream);
				image.getElement().setAttribute("src",
						new StreamResource(fileName, () -> new ByteArrayInputStream(bytes)));
				try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
					final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
					if (readers.hasNext()) {
						ImageReader reader = readers.next();
						try {
							reader.setInput(in);
							image.setWidth(reader.getWidth(0) + "px");
							image.setHeight(reader.getHeight(0) + "px");
						} finally {
							reader.dispose();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			image.getStyle().set("width", "120px").set("height", "140px");

			return image;
		}
		Div content = new Div();
		String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'", mimeType,
				MessageDigestUtil.sha256(stream.toString()));
		content.setText(text);
		return new Div();

	}

	private void showOutput(String text, Component content, HasComponents outputContainer) {
		// HtmlComponent p = new HtmlComponent(Tag.P);
		// p.getElement().setText(text);
		// outputContainer.add(p);
		outputContainer.add(content);
	}

	////////////////////////////////////////////////////////////////
	// Metodos de guardar y eliminar de Curso
	////////////////////////////////////////////////////////////////
	// Metodo de salvar
	private void saveAlumnoCurso(AlumnoCursoVO alumnoCurso, AbstractEditorDialog.Operation operation) {
		if (operation.getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())) {
			alumnoCurso.setAlumnoId(alumnoId);
			alumnoCursoService.save(alumnoCurso);
		}else{
			alumnoCursoService.update(alumnoCurso);
		}
		/*	cursoCostoService.save(cursoCostos);
			operationKind = " agregado";
		} else {
			cursoCostoService.update(cursoCostos);
			operationKind = " modificado";
		}
		Notification.show("Costo " + operationKind, 3000, Position.BOTTOM_END);
		cursoCostosList.updateView(cursoId);*/
		updateView();
		System.out.println(alumnoCurso);
		
		
	}

	// Eliminar
	private void deleteAlumnoCurso(AlumnoCursoVO alumnoCurso) {
		ConfirmDialog
        .createQuestion()
        .withCaption("Baja de	 Curso")
        .withMessage("Deseas dar de baja el curso?")
        .withOkButton(() -> {
        	if(alumnoCursoService.delete(alumnoCurso.getAlumnoCursoId())){
        		Notification.show("El curso "+alumnoCurso.getCursoNombre() +" se ha dado de baja", 3000, Position.BOTTOM_END);
        		updateView();
        	}else{
        		Notification.show("Ocurrió un error al dar de baja el curso "+ alumnoCurso.getCursoNombre(), 
        				3000, Position.BOTTOM_END);
        	}
        }, ButtonOption.focus(), ButtonOption.caption("SI"))
        .withCancelButton(ButtonOption.caption("NO"))
        .open();
	}
	
	private void updateView() {
        //List<AlumnoCursoVO> lstAlumnos = alumnoCursoService.findByStudent(alumnoId);
		List<AlumnoCursoVO> lstAlumnos = alumnoCursoService.findByStudentActive(alumnoId);
        grid.setItems(lstAlumnos);

        /*if (searchField.getValue().length() > 0) {
            header.setText("Search for “"+ searchField.getValue() +"”");
        } else {
            header.setText("Costos");
        }*/
    }
	
	
	///Botones de cursos asociados al alumno
	//Boton de pagar
    private Button createCoursePayButton(AlumnoCursoVO alumnoCursoVO) {
        Button edit = new Button("");
        edit.addClickListener(event -> 
        	{edit.getUI().ifPresent(ui -> ui.navigate("alumnos/pagos/"+alumnoCursoVO.getAlumnoId()+"/"+alumnoCursoVO.getCursoId()));});
        edit.setIcon(new Icon(VaadinIcon.WALLET));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        edit.setEnabled(alumnoVO.getAlumnoActivo());
        return edit;
    }
    
    //
    private Button createCourseCancelButton(AlumnoCursoVO alumnoCursoVO) {
        Button delete = new Button("", event -> deleteAlumnoCurso(alumnoCursoVO));
//        edit.addClickListener(event -> 
//        	{edit.getUI().ifPresent(ui -> ui.navigate("pagos/"+alumnoCursoVO.getAlumnoId()+"/"+alumnoCursoVO.getCursoId()));});
        delete.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        delete.addClassName("review__edit");
        delete.getElement().setAttribute("theme", "tertiary");
        delete.getElement().setAttribute("title", "Eliminar");
        delete.setEnabled(alumnoVO.getAlumnoActivo());
        return delete;
    }
    
    private Button createCourseEditButton(AlumnoCursoVO alumnoCursoVO) {
        Button edit = new Button("");
        edit.addClickListener(event -> 
        	{
        		formCursoAdd = new AlumnoCursoAddDialog(this::saveAlumnoCurso, this::deleteAlumnoCurso, 
        				this.cursosService,	cursoCostoService, alumnoId);
        		formCursoAdd.open(alumnoCursoVO,
        				AbstractEditorDialog.Operation.EDIT); 
        		formCursoAdd.init(AbstractEditorDialog.Operation.EDIT); 
        	});
        edit.setIcon(new Icon(VaadinIcon.PENCIL));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        edit.setEnabled(alumnoVO.getAlumnoActivo());
        return edit;
    }
	
}
