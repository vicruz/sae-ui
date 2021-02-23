package com.sae.gandhi.spring.ui.students.pagos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.utils.SaeDateUtils;
import com.sae.gandhi.spring.vo.AlumnoPagoVO;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

public class StudentPaymentDialog extends AbstractEditorDialog<AlumnoPagoVO> {
	
	//private Integer alumnoPagoId;
	private String cursoNombre;
	BigDecimal montoTotal;
	BigDecimal montoPorPagar;
	BigDecimal montoSaldo;
	
	private Label lbCourse;
	private Label lbTotalAmount;
	private Checkbox cbUseSaldo;
	private DatePicker dpFechaPago; 
	private TextField txtPaymentAmount;
	private VerticalLayout vlPayment = new VerticalLayout();
	
	boolean isAdmin;

	protected StudentPaymentDialog(BiConsumer<AlumnoPagoVO, Operation> itemSaver,
			Consumer<AlumnoPagoVO> itemDeleter, Integer alumnoPagoId, String cursoNombre, BigDecimal montoTotal,
			BigDecimal montoPorPagar, BigDecimal montoSaldo, boolean isAdmin) {
		super("Pago", itemSaver, itemDeleter);

		//this.alumnoPagoId = alumnoPagoId;
		this.cursoNombre = cursoNombre;
		this.montoTotal = montoTotal;
		this.montoPorPagar = montoPorPagar;
		this.montoSaldo = montoSaldo;
		this.isAdmin = isAdmin;
		
		addDataPaymentFields();
	}
	
	private void addDataPaymentFields(){
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		StringBuilder sb = new StringBuilder("<ul>");
		
		lbCourse = new Label(cursoNombre);
		lbCourse.getStyle().set("fontWeight", "bold");
		
		sb.append("<li>Monto Total: " + formatter.format(montoTotal) + "</li>");
		sb.append("<li>Monto por pagar: " + formatter.format(montoPorPagar) + "</li>");
		
		lbTotalAmount = new Label();
		lbTotalAmount.getElement().setProperty("innerHTML", sb.toString());
//		lbDebthAmount = new Label("Monto por pagar: " + formatter.format(montoPorPagar));
		
		cbUseSaldo = new Checkbox("Aplicar saldo a favor (" + formatter.format(montoSaldo) + ")");
		getBinder().forField(cbUseSaldo)
		.bind(AlumnoPagoVO::getUsaSaldo, AlumnoPagoVO::setUsaSaldo);
		
		dpFechaPago = new DatePicker();
		dpFechaPago.setLabel("Fecha de pago");
		getBinder().forField(dpFechaPago).bind(AlumnoPagoVO::getAlumnoPagoFechaPago, AlumnoPagoVO::setAlumnoPagoFechaPago);
		dpFechaPago.focus();
		
		txtPaymentAmount = new TextField("Monto a pagar:");
		txtPaymentAmount.setRequired(true);
		txtPaymentAmount.setPattern("\\d+(\\.)?(\\d{1,2})?"); //Formato #0.00
		txtPaymentAmount.setPreventInvalidInput(true);
		txtPaymentAmount.setPrefixComponent(new Span("$"));
		getBinder().forField(txtPaymentAmount)
			.withConverter(
                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un número."))
			.withNullRepresentation(BigDecimal.ZERO)
			.bind(AlumnoPagoVO::getAlumnoPagoPago, AlumnoPagoVO::setAlumnoPagoPago);
		
		
		
		vlPayment.add(lbCourse,lbTotalAmount);
		if(montoSaldo.compareTo(BigDecimal.ZERO) >0){
			vlPayment.add(cbUseSaldo);
		}
		
		//Solo mostrar cuando sea administrador
		if(isAdmin)
			vlPayment.add(dpFechaPago);
		
		vlPayment.add(txtPaymentAmount);
		getFormLayout().add(vlPayment);
		
		dpFechaPago.setValue(SaeDateUtils.calendarToLocalDate(Calendar.getInstance()));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void confirmDelete() {
		// Sin uso en esta clase
		
	}

	@Override
	protected Boolean validateFields() {
		String txtPay = txtPaymentAmount.getValue();
		BigDecimal payAmount = BigDecimal.ZERO;
		NumberFormat nfc = NumberFormat.getCurrencyInstance();
				
		try{
			
			//Toma el monto de la caja de pago
			if(txtPay!=null && !txtPay.equals("")){
				payAmount = payAmount.add(new BigDecimal(txtPay));
			}
			
			//Complementa el monto del saldo a favor
			if(cbUseSaldo.getValue()){
				//Solo entrará si el monto en la caja es menor al monto por pagar
				if(payAmount.compareTo(montoPorPagar)<0){
					BigDecimal tmp = montoPorPagar.subtract(payAmount);
					
					//Si el valor tmp es mayor al saldo, el monto utilizado será el saldo completo
					if(tmp.compareTo(montoSaldo)>=0){
						payAmount = payAmount.add(montoSaldo);
					}
					//En caso contrario, el saldo utilizado será el del tmp
					else{
						payAmount = payAmount.add(tmp);
					}
				}
			}
			
			
			if(payAmount.equals(BigDecimal.ZERO)){
				Notification.show("El monto de pago debe ser mayor a $0.00",5000,Notification.Position.MIDDLE);
				return false;
			}
			
			if(payAmount.compareTo(montoPorPagar)>0){
				String notification = "El monto de pago debe ser menor o igual a " + 
						nfc.format(montoPorPagar);
				Notification.show(notification,5000,Notification.Position.MIDDLE);
				return false;
			}
		}catch(Exception e){
			Notification.show("El monto es inválido",5000,Notification.Position.MIDDLE);
			return false;
		}
		
		return true;
	}


}
