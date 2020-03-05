package com.sae.gandhi.spring.ui.students.pagos;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.AlumnoPagoService;
import com.sae.gandhi.spring.service.AlumnosService;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.service.ReportService;
//import com.sae.gandhi.spring.service.SessionService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.utils.StreamImage;
import com.sae.gandhi.spring.vo.AlumnoPagoVO;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

@Route(value = "alumnos/pagos", layout = MainView.class)
@PageTitle("Alumno")
public class StudentPaymentsEditorPage extends VerticalLayout implements HasUrlParameter<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7895496222777946634L;

	private final TextField searchField = new TextField("", "Buscar Pagos");
	private final H3 header = new H3("Pagos de Alumno");

	private AlumnosVO alumnoVO = null;;
	private CursosVO cursosVO = null;
	private Integer alumnoId;
	private Integer cursoId;

	private AlumnosService alumnosService;
	private CursosService cursosService;
	private AlumnoPagoService alumnoPagoService;
	private ReportService reportService;
//	private SessionService sessionService;
	
	private Grid<AlumnoPagoVO> grid;
	private Binder<AlumnoPagoVO> binder;
	private Editor<AlumnoPagoVO> editor;

	private StudentPaymentDialog form;
	private boolean isAdmin;

	@Autowired
	public StudentPaymentsEditorPage(//SessionService sessionService, 
			AlumnosService alumnosService, 
			CursosService cursosService, AlumnoPagoService alumnoPagoService, ReportService reportService) {
		this.alumnosService = alumnosService;
		this.cursosService = cursosService;
		this.alumnoPagoService = alumnoPagoService;
		this.reportService = reportService;
		//this.sessionService = sessionService;
		isAdmin = UI.getCurrent().getSession().getAttribute("isAdmin")==null?false:
    		(boolean)UI.getCurrent().getSession().getAttribute("isAdmin");
	}

	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
		grid = new Grid<>();
		binder = new Binder<>(AlumnoPagoVO.class);

		addTitle();
		// Si no hay parámetros no agrega ningún dato
		if (parameter != null && !parameter.equals("")) {
			StringTokenizer st = new StringTokenizer(parameter, "/");
			try {
				alumnoId = Integer.valueOf(st.nextToken());
				if (st.hasMoreTokens()) {
					cursoId = Integer.valueOf(st.nextToken());
				}

				addStudentData();
				addGridLayout();

				// Carga la información del alumno "Nombre", "Curso", "Saldo" a
				// favor si existe y "Boton para regresar a la edicion del
				// alumno"
			} catch (Exception e) {
				// Los parametros no son numeros, por lo tanto no carga datos
				return;
			}
		}

	}

	private void addTitle() {
		Div viewToolbar = new Div();
		viewToolbar.addClassName("view-toolbar");

		searchField.setPrefixComponent(new Icon("lumo", "search"));
		searchField.addClassName("view-toolbar__search-field");
		// searchField.addValueChangeListener(e -> updateView());
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

		viewToolbar.add(searchField);
		viewToolbar.getStyle().set("width", "100%");
		add(viewToolbar);
		add(header);
	}

	// Datos del alumno
	public void addStudentData() {
		HorizontalLayout hlLayout = new HorizontalLayout();
		String alumnoNombre;
		alumnoVO = alumnosService.findById(alumnoId);
		if (cursoId != null) {
			cursosVO = cursosService.findById(cursoId);
		}
		
		alumnoNombre = alumnoVO.getAlumnoNombre() + " " + alumnoVO.getAlumnoApPaterno() + " " + alumnoVO.getAlumnoApMaterno();

		// Imagen del alumno
		Image image;
		Div divImage = new Div();
		AbstractStreamResource asr;

		divImage.getStyle().set("margin-right", "10px").set("width", "40px").set("height", "40px");

		if (Objects.isNull(alumnoVO.getAlumnoImagen())) {
			image = new Image("frontend/images/usuario.jpg", "");
		} else {
			// ByteArrayInputStream bis = new
			// ByteArrayInputStream(alumno.getAlumnoImagen());
			StreamImage stream = new StreamImage(alumnoVO.getAlumnoImagen());
			asr = new StreamResource("image", stream);
			image = new Image(asr, "");
		}

		image.getStyle().set("border-radius", "50%").set("width", "60px").set("height", "60px").set("background-color",
				"lightgray");

		divImage.add(image);

		// Nombre del alumno
		// Tamaño 14 y en negritas
		Label lbStudent = new Label();
		lbStudent.getStyle().set("fontSize", "14px");
		lbStudent.getStyle().set("fontWeight", "bold");
		lbStudent.getStyle().set("width", "30%");
		lbStudent.setText(alumnoNombre);

		// Nombre del curso
		// Tamaño 14 y en negritas
		Label lbCurso = new Label();
		lbCurso.getStyle().set("fontSize", "14px");
		lbCurso.getStyle().set("fontWeight", "bold");
		lbCurso.getStyle().set("width", "30%");

		if (cursosVO != null)
			lbCurso.setText(cursosVO.getCursoNombre());
		else
			lbCurso.setText("");

		// Boton de regresar a la edicion del alumno
		Button edit = new Button("Editar");
		edit.addClickListener(event -> {
			edit.getUI().ifPresent(ui -> ui.navigate("alumnos/edit/" + alumnoVO.getAlumnoId()));
		});
		edit.setIcon(new Icon(VaadinIcon.EDIT));
		edit.addClassName("review__edit");
		edit.getElement().setAttribute("theme", "tertiary");
		edit.getElement().setAttribute("title", "Editar");
		edit.setWidth("30%");
		
		
		Anchor download = new Anchor(new StreamResource(alumnoNombre+".pdf", () -> getReport()),"");
		download.getElement().setAttribute("download", true);
		Button report = new Button("Reporte");
		report.setIcon(new Icon(VaadinIcon.CLOUD_DOWNLOAD));
		report.addClassName("review__edit");
		report.getElement().setAttribute("theme", "tertiary");
		report.getElement().setAttribute("title", "Descargar");
		//report.setWidth("30%");
		//download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
		download.add(report);
		 
		hlLayout.getStyle().set("width", "100%");
		hlLayout.add(divImage, lbStudent, lbCurso, edit, download);
		add(hlLayout);

	}

	private void addGridLayout() {

		editor = grid.getEditor();
		editor.setBinder(binder);
		editor.setBuffered(true);

		grid.addColumn(new ComponentRenderer<>(this::createConceptLabel)).setHeader("Concepto").setFlexGrow(5)
				.setResizable(true);
		Column<AlumnoPagoVO> columnMonto = grid.addColumn(new NumberRenderer<>(AlumnoPagoVO::getAlumnoPagoMonto, NumberFormat.getCurrencyInstance()))
				.setHeader("Monto").setFlexGrow(1).setResizable(true);
		//Fecha Límite
		Column<AlumnoPagoVO> columnDate = grid
				.addColumn(new LocalDateRenderer<>(AlumnoPagoVO::getAlumnoPagoFechaLimite,
						DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
				.setHeader("Fecha Límite").setFlexGrow(2).setResizable(true);
		// Boton para editar
		Column<AlumnoPagoVO> editorColumn = grid.addComponentColumn(vo -> {
			if (vo.getEstatusId() == SaeEnums.Pago.ADEUDO.getStatusId() //tiene adeudo
					|| vo.getEstatusId() == SaeEnums.Pago.PREPARADO.getStatusId() //esta preparado
					|| alumnoVO.getAlumnoActivo() //alumno esta activo
					) {
				Button buttonEdit = new Button();
				buttonEdit.setIcon(new Icon(VaadinIcon.PENCIL));
				buttonEdit.addClassName("review__edit");
				buttonEdit.getElement().setAttribute("theme", "tertiary");
				buttonEdit.getElement().setAttribute("title", "Editar");
				buttonEdit.addClickListener(e -> {editor.editItem(vo); grid.select(vo); });
				
				//if(!sessionService.isAdmin()){
				if(!isAdmin){
					buttonEdit.setEnabled(false);
				}

				return buttonEdit;
			} else {
				return new Label();
			}
		});
		grid.addColumn(new NumberRenderer<>(AlumnoPagoVO::getAlumnoPagoPago, NumberFormat.getCurrencyInstance()))
				.setHeader("Pago").setResizable(true).setFlexGrow(1);
		grid.addColumn(new LocalDateRenderer<>(AlumnoPagoVO::getAlumnoPagoFechaPago,
				DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))).setHeader("Fecha Pago").setResizable(true).setFlexGrow(2);
		grid.addColumn(new ComponentRenderer<>(this::createEstatusLabel)).setHeader("Estatus").setFlexGrow(1);
		grid.addColumn(new ComponentRenderer<>(this::createPayButton)).setHeader("Pago").setFlexGrow(1);
		
		//if(sessionService.isAdmin())
		if(isAdmin)
			grid.addColumn(new ComponentRenderer<>(this::createDeleteButton)).setHeader("Eliminar").setFlexGrow(1);
		
		grid.setWidth("100%");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.getStyle().set("fontSize", "14px");
		update();

		//// Editor de fecha
		Div validationStatus = new Div();
		validationStatus.setId("dateChange");

		DatePicker dp = new DatePicker();
		binder.forField(dp)
		.withStatusLabel(validationStatus).bind("alumnoPagoFechaLimite");
		columnDate.setEditorComponent(dp);

		//// Editor de monto
		TextField tfMonto = new TextField();
		tfMonto.setPattern("\\d+(\\.)?(\\d{1,2})?");
		tfMonto.setPrefixComponent(new Span("$"));
		Label lbMonto = new Label();
		Runnable bindMonto = () -> binder.forField(tfMonto).withConverter(
                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un costo."))
				.bind("alumnoPagoMonto");
		
		Runnable setMonto = () -> columnMonto.setEditorComponent(item -> {
			if(item.getAlumnoPagoPago()!=null && item.getAlumnoPagoPago().compareTo(BigDecimal.ZERO)==	0){
				tfMonto.setValue(NumberFormat.getCurrencyInstance().format(item.getAlumnoPagoMonto()));
				bindMonto.run();
				return tfMonto;
			}else{
				lbMonto.setText(NumberFormat.getCurrencyInstance().format(item.getAlumnoPagoMonto()));
				return lbMonto;
			}
		});
		
		setMonto.run();
		
		
		// Botones que apareceran al momento de editar
		Button save = new Button("", e -> editor.save());
		save.setIcon(new Icon(VaadinIcon.CHECK));
		save.addClassName("review__edit");
		save.getElement().setAttribute("theme", "tertiary");
		save.getElement().setAttribute("title", "Guardar");

		Button cancel = new Button("", e -> editor.cancel());
		cancel.setIcon(new Icon(VaadinIcon.CLOSE_SMALL));
		cancel.addClassName("review__edit");
		cancel.getElement().setAttribute("theme", "tertiary");
		cancel.getElement().setAttribute("title", "Cancelar");

		// Add a keypress listener that listens for an escape key up event.
		// Note! some browsers return key as Escape and some as Esc
		grid.getElement().addEventListener("keyup", event -> editor.cancel())
				.setFilter("event.key === 'Escape' || even.key === 'Esc'");

		Div buttons = new Div(save, cancel);
		editorColumn.setEditorComponent(buttons);

		// Al momento de guardar actualiza el registro
		editor.addSaveListener(event -> {
			// System.out.println(event.getItem().getAlumnoPagoId() + " - "
			// +event.getItem().getAlumnoPagoFechaLimite());
			alumnoPagoService.updateFechaMonto(event.getItem());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			Notification
					.show("Se ha actualizado el monto a $" +NumberFormat.getCurrencyInstance().format(event.getItem().getAlumnoPagoMonto()) + 
							" y la fecha límite de pago a " + 
							event.getItem().getAlumnoPagoFechaLimite().format(formatter));
		});

		add(grid);
	}

	private Label createConceptLabel(AlumnoPagoVO vo) {
		Label label = new Label();
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(vo.getAlumnoPagoFechaLimite());
		/*System.out.println("-----------" + vo.getAlumnoPagoFechaLimite().getMonth().name() + " "
				+ vo.getAlumnoPagoFechaLimite().getYear());*/
		StringBuilder sb = new StringBuilder(vo.getCostoNombre());
		sb.append(" ").append(SaeEnums.Mes.getMes(vo.getAlumnoPagoFechaLimite().getMonth().getValue()))
				// .append(vo.getAlumnoPagoFechaLimite().getMonth().name())
				.append(" ")
				// .append(cal.get(Calendar.YEAR));
				.append(vo.getAlumnoPagoFechaLimite().getYear());

		label.setText(sb.toString());

		return label;
	}

	private Label createEstatusLabel(AlumnoPagoVO vo) {
		Label label = new Label(SaeEnums.Pago.getPago(vo.getEstatusId()).getStatus());
		return label;
	}

	private Button createPayButton(AlumnoPagoVO vo) {
		Button button = new Button("");
		button.addClassName("review__edit");
		button.getElement().setAttribute("theme", "tertiary");
		button.getElement().setAttribute("title", "Editar");
		if(vo.getEstatusId() != SaeEnums.Pago.COMPLETO.getStatusId()){
			button.setIcon(new Icon(VaadinIcon.DOLLAR));
			button.addClickListener(event -> {
				grid.select(vo);
				alumnoVO = alumnosService.findById(alumnoVO.getAlumnoId());
				form = new StudentPaymentDialog(this::savePayment, this::deletePayment,
						vo.getAlumnoPagoId(), createConceptLabel(vo).getText(), vo.getAlumnoPagoMonto(), 
						vo.getAlumnoPagoMonto().subtract(vo.getAlumnoPagoPago()), alumnoVO.getAlumnoSaldo(),
						//sessionService.isAdmin());
						isAdmin);
				vo.setAlumnoPagoPago(BigDecimal.ZERO);
				form.open(vo, AbstractEditorDialog.Operation.ADD);
			});	
			
			if(!alumnoVO.getAlumnoActivo()){
				button.setEnabled(false);
			}
		}
		return button;
	}
	
	private Button createDeleteButton(AlumnoPagoVO vo) {
		Button button = new Button("");
		button.addClassName("review__edit");
		button.getElement().setAttribute("theme", "tertiary");
		button.getElement().setAttribute("title", "Editar");
		if(vo.getAlumnoPagoPago() == null || vo.getAlumnoPagoPago().compareTo(BigDecimal.ZERO)==0 ){
			grid.select(vo);
			button.setIcon(new Icon(VaadinIcon.CLOSE_SMALL));
			button.addClickListener(event -> {
				deletePayment(vo);
			});			
		}
		
		if(!alumnoVO.getAlumnoActivo()){
			button.setEnabled(false);
		}
		return button;
	}

	private void update() {
		List<AlumnoPagoVO> lstAlumno;
		if (cursoId != null)
			lstAlumno = alumnoPagoService.findByAlumnoIdAndCursoId(alumnoId, cursoId);
		else
			lstAlumno = alumnoPagoService.findByAlumnoId(alumnoId);
		grid.setItems(lstAlumno);
	}

	////////////////////////////////////////////////////////////////
	// Metodos de guardar y eliminar de Curso
	////////////////////////////////////////////////////////////////
	// Metodo de salvar
	private void savePayment(AlumnoPagoVO alumnoPagoVO, AbstractEditorDialog.Operation operation) {
		if(Objects.isNull(alumnoPagoVO.getAlumnoPagoPago())){
			alumnoPagoVO.setAlumnoPagoPago(BigDecimal.ZERO);
		}
		
		if(alumnoPagoVO.getAlumnoPagoPago().compareTo(BigDecimal.ZERO)>0 || alumnoPagoVO.getUsaSaldo()){
			if(alumnoPagoVO.getAlumnoPagoFechaPago()==null){
				alumnoPagoVO.setAlumnoPagoFechaPago(SaeDateUtils.calendarToLocalDate(Calendar.getInstance()));				
			}
			AlumnoPagoVO alumnoPagoVOtmp = alumnoPagoService.save(alumnoPagoVO, alumnoVO.getAlumnoId(), alumnoVO.getAlumnoSaldo());
			
			//restar el saldo al alumno/vista
			//if(alumnoPagoVO.getUsaSaldo())
			//alumnoVO.setAlumnoSaldo(alumnoVO.getAlumnoSaldo().subtract(alumnoPagoVO.get));
			
			try {
				BeanUtils.copyProperties(alumnoPagoVO, alumnoPagoVOtmp);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			grid.getDataProvider().refreshItem(alumnoPagoVO);
		}
		
		System.out.println(alumnoPagoVO);

	}

	// Eliminar
	private void deletePayment(AlumnoPagoVO alumnoPagoVO) {
		ConfirmDialog
        .createQuestion()
        .withCaption("Eliminar Pago")
        .withMessage("Deseas eliminar el Pago?")
        .withOkButton(() -> {
//            System.out.println("YES. Implement logic here.");
        	alumnoPagoService.delete(alumnoPagoVO);
            Notification.show("Pago eliminado", 3000, Position.BOTTOM_END);
            //RouterLink courses = new RouterLink(null, CursosEditorPage.class);
    		//courses.add(new Icon(VaadinIcon.ACADEMY_CAP), new Text("Cursos"));
            update();
        }, ButtonOption.focus(), ButtonOption.caption("SI"))
        .withCancelButton(ButtonOption.caption("NO"))
        .open();
	}

	
	private InputStream getReport(){
		return reportService.generateReport(alumnoVO, cursosVO, alumnoId, cursoId);
	}
}
