package com.sae.gandhi.spring.ui.students.cursos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.AlumnoCursoVO;
import com.sae.gandhi.spring.vo.CursoCostosVO;
import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.renderer.TemplateRenderer;

public class AlumnoCursoAddDialog  extends AbstractEditorDialog<AlumnoCursoVO> {

	private CursosService cursosService;
	private CursoCostosService cursoCostoService;
	private Integer alumnoId;
	
	private VerticalLayout vlCourse = new VerticalLayout();
	private ComboBox<CursosVO> comboBox = new ComboBox<>();
	
	private Label lbPagos = new Label();
	private Checkbox chBeca;
	private Checkbox chDiscount;
	private TextField txtBeca;
	private TextField txtDiscount;
	private DatePicker startDatePicker = new DatePicker("Fecha Ingreso");
	
	public AlumnoCursoAddDialog(BiConsumer<AlumnoCursoVO, Operation> itemSaver,
			Consumer<AlumnoCursoVO> itemDeleter, CursosService cursosService,  
			 CursoCostosService cursoCostoService, Integer alumnoId) {
		super("Cursos", itemSaver, itemDeleter);
		
		this.cursosService = cursosService;
		this.cursoCostoService = cursoCostoService;
		this.alumnoId = alumnoId;
	}
	
	
	public void init(Operation operation){
		if(operation==null){
			setMainOperation(AbstractEditorDialog.Operation.ADD);
		}else{
			setMainOperation(operation);
		}
		addCursosCombobox();
		getFormLayout().add(vlCourse);			
	}
	
	public void addCursosCombobox(){
		HorizontalLayout hlCourse = new HorizontalLayout();
		CursosVO cursoEdit = null;
		comboBox.setLabel("Curso");
		comboBox.setRequired(true);
		//Muestra los pagos en un template indicando el nombre y debajo el costo de cada curso 
		comboBox.setRenderer(TemplateRenderer.<CursosVO> of(
		        "<div><strong>[[item.nombre]]</strong></div>")
		        .withProperty("nombre", CursosVO::getCursoNombre));
		//Al seleccionar el curso, pone en el combo solamente el nombre
		comboBox.setItemLabelGenerator(CursosVO::getCursoNombre);
		
		List<CursosVO> lst = cursosService.findCoursesNotInStudent(alumnoId);
		
		if(getMainOperation().getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())){
			lst = cursosService.findCoursesNotInStudent(alumnoId);
			//Fecha de ingreso al curso
			startDatePicker.setVisible(false);
		}else{
			comboBox.setEnabled(false);
			startDatePicker.setEnabled(false);
			//comboBox.setPreventInvalidInput(true);
			cursoEdit = cursosService.findById(getCurrentItem().getCursoId());
			startDatePicker.setValue(getCurrentItem().getAlumnoCursoFechaIngreso());
			lst = new ArrayList<>();
			lst.add(cursoEdit);
		}
		comboBox.setItems(lst);
		
		if(getMainOperation().getNameInText().equals(AbstractEditorDialog.Operation.EDIT.getNameInText())){
			comboBox.setValue(cursoEdit);
			addInfoPayment();
		}
		
		comboBox.addValueChangeListener(event -> addInfoPayment());
		
		hlCourse.add(comboBox, startDatePicker);
		//vlCourse.add(comboBox);
		vlCourse.add(hlCourse);
		
		getBinder().forField(comboBox)
            .bind(AlumnoCursoVO::getCursoVO, AlumnoCursoVO::setCursoVO); //Establece setter y getter para su bindeo
		getBinder().forField(startDatePicker)
        	.bind(AlumnoCursoVO::getAlumnoCursoFechaIngreso, AlumnoCursoVO::setAlumnoCursoFechaIngreso); //Establece setter y getter para su bindeo de la fecha de ingreso
		
	} 
	
	private void addInfoPayment(){
		boolean aplicaBeca = false;
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		StringBuilder htmlPayments = new StringBuilder();
		
		if(comboBox.getOptionalValue()==null){
			return;
		}
		
		//Remover elementos de pago
		//No se usar streams XD
		Stream<Component> stream = vlCourse.getChildren();
		Iterator<Component> iterator = stream.iterator();
		while(iterator.hasNext()){
			Component component = iterator.next();
			if(component.getId().isPresent() && 
					(component.getId().get().startsWith("lbCosto") || component.getId().get().equals("flDiscounts"))  ){
				vlCourse.remove(component);
			}
		}
		
		//Pagos en negritas HTML
		//lbPagos.getElement().setProperty("innerHTML", "<strong>Pagos</strong>");
		htmlPayments.append("<strong>Pagos</strong><ul>");
		Optional<CursosVO> optional = comboBox.getOptionalValue();
		
		List<CursoCostosVO> lstCostoCursosVO = cursoCostoService.findByCurso(optional.get().getCursoId(), true);
		
		//crea la lista de pagos que se realiazran en el curso
		//int labelId = 1;
		for (CursoCostosVO cursoCostosVO : lstCostoCursosVO) {
			htmlPayments.append("<li>" + cursoCostosVO.getCostoNombre() + " " + formatter.format(cursoCostosVO.getCostoMonto()) + "</li>");
			if(cursoCostosVO.getCursoCostoAplicaBeca()){
				aplicaBeca = true;
			}
		}
		htmlPayments.append("</ul>");
		lbPagos.getElement().setProperty("innerHTML", htmlPayments.toString());
		vlCourse.add(lbPagos);
		
		///Si algún pago aplica beca, se muestran las cajas de texto para beca y descuento
		if(aplicaBeca){
			HorizontalLayout flDiscounts = new HorizontalLayout();
			HorizontalLayout hlBeca = new HorizontalLayout();
			HorizontalLayout hlDescuento = new HorizontalLayout();
			
			flDiscounts.setId("flDiscounts");
			
			chBeca = new Checkbox("Beca");
			chBeca.addValueChangeListener(event -> {
				if(event.getValue()){
					txtDiscount.setValue("");
					txtDiscount.setEnabled(false);
					chDiscount.setValue(false);
					txtBeca.setEnabled(true);
				}else{
					txtBeca.setEnabled(false);
					txtBeca.setValue("");
				}});
			txtBeca = new TextField();
			hlBeca.add(chBeca, txtBeca);
			
			txtBeca.setPattern("\\d+(\\.)?(\\d{1,2})?");
			txtBeca.setPreventInvalidInput(true);
			txtBeca.setPrefixComponent(new Span("%"));
			txtBeca.setEnabled(false);
			
			getBinder().forField(chBeca).bind(AlumnoCursoVO::getAlumnoCursoAplicaBeca, AlumnoCursoVO::setAlumnoCursoAplicaBeca);
			getBinder().forField(txtBeca).withConverter(
	                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un porcentaje."))
				.withNullRepresentation(BigDecimal.ZERO)
				.bind(AlumnoCursoVO::getAlumnoCursoBeca, AlumnoCursoVO::setAlumnoCursoBeca);
			
			
			
			chDiscount = new Checkbox("Descuento");
			chDiscount.addValueChangeListener(event -> {
				if(event.getValue()){
					txtBeca.setValue("");
					txtBeca.setEnabled(false);
					chBeca.setValue(false);
					txtDiscount.setEnabled(true);
				}else{
					txtDiscount.setEnabled(false);
					txtDiscount.setValue("");
				}
			});
			txtDiscount = new TextField();
			hlDescuento.add(chDiscount, txtDiscount);
			
			txtDiscount.setPattern("\\d+(\\.)?(\\d{1,2})?");
			txtDiscount.setPreventInvalidInput(true);
			txtDiscount.setPrefixComponent(new Span("$"));
			txtDiscount.setEnabled(false);
			
			getBinder().forField(chDiscount).bind(AlumnoCursoVO::getAlumnoCursoAplicaDescuento, AlumnoCursoVO::setAlumnoCursoAplicaDescuento);
			getBinder().forField(txtDiscount).withConverter(
	                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un monto."))
				.withNullRepresentation(BigDecimal.ZERO)
				.bind(AlumnoCursoVO::getAlumnoCursoDescuento, AlumnoCursoVO::setAlumnoCursoDescuento);
			
			flDiscounts.add(hlBeca, hlDescuento);
			
			//Al momento de editar
			if(getCurrentItem().getAlumnoCursoBeca()!=null){
				chBeca.setValue(true);
				txtBeca.setValue(getCurrentItem().getAlumnoCursoBeca().toString());
				chDiscount.setValue(false);
				txtDiscount.setValue("");
			}
			if(getCurrentItem().getAlumnoCursoDescuento()!=null){
				chDiscount.setValue(true);
				txtDiscount.setValue(getCurrentItem().getAlumnoCursoDescuento().toString());
				chBeca.setValue(false);
				txtBeca.setValue("");
			}
			
			//Poner fecha al campo de fecha de ingreso
			if(getMainOperation().getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())){
				Calendar cal = Calendar.getInstance();
				startDatePicker.setValue(LocalDate.of(cal.get(Calendar.YEAR), 
						cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
				startDatePicker.setVisible(true);
			}
			
			vlCourse.add(flDiscounts);
		}
		
		//vlCourse.add(flInfo);
	}
	
	public void addBecaDiscount(){
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void confirmDelete() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected Boolean validateFields() {
		// TODO Auto-generated method stub
		return true;
	}

}
