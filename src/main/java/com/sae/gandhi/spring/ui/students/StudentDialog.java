package com.sae.gandhi.spring.ui.students;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;

public class StudentDialog extends AbstractEditorDialog<AlumnosVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7638389744120892806L;
	
	
	private TextField txtName = new TextField("Nombre");
	private TextField txtApPaterno = new TextField("Ap. Paterno");
	private TextField txtApMaterno = new TextField("Ap. Materno");
	private TextField txtTutor = new TextField("Tutor");
	private TextField txtEmail = new TextField("Email");
	private DatePicker dtFechaNac = new DatePicker();
	private TextField txtPhone1 = new TextField("Telefono 1");
	private TextField txtPhone2 = new TextField("Telefono 2");
	private Upload upload;
	private MemoryBuffer buffer;
	

	protected StudentDialog(BiConsumer<AlumnosVO, Operation> itemSaver,
			Consumer<AlumnosVO> itemDeleter) {
		super("Alumno", itemSaver, itemDeleter);
		addStudentData();
		addImageLoader();
		addTutorData();
	}
	
	public void addStudentData(){
		dtFechaNac.setLabel("Fecha de Nacimiento");

		Div div = new Div();
		VerticalLayout vl = new VerticalLayout();
		
		txtName.getStyle().set("width", "100%").set("padding-top", "0px");
		txtApPaterno.getStyle().set("width", "100%").set("padding-top", "0px");
		txtApMaterno.getStyle().set("width", "100%").set("padding-top", "0px");
		dtFechaNac.getStyle().set("width", "100%").set("padding-top", "0px");
		
		
		getBinder().forField(txtName)
			.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
			.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
			.withValidator(new StringLengthValidator(
                "El nombre debe ser por lo menos 3 caracteres",
                3, null))
			.bind(AlumnosVO::getAlumnoNombre, AlumnosVO::setAlumnoNombre); //Establece setter y getter para su bindeo
		
		getBinder().forField(txtApPaterno)
			.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
			.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
			.withValidator(new StringLengthValidator(
					"El apellido debe ser por lo menos 3 caracteres",
					3, null))
			.bind(AlumnosVO::getAlumnoApPaterno, AlumnosVO::setAlumnoApPaterno); //Establece setter y getter para su bindeo
		
		getBinder().forField(txtApMaterno)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.withValidator(new StringLengthValidator(
				"El apellido debe ser por lo menos 3 caracteres",
				3, null))
		.bind(AlumnosVO::getAlumnoApMaterno, AlumnosVO::setAlumnoApMaterno); //Establece setter y getter para su bindeo
		
		getBinder().forField(dtFechaNac)
		.bind(AlumnosVO::getAlumnoFechaNac, AlumnosVO::setAlumnoFechaNac); //Establece setter y getter para su bindeo
		
		/*txtName.getStyle().set("padding-top", "0px");
		txtApPaterno.getStyle().set("padding-top", "0px");
		txtApMaterno.getStyle().set("padding-top", "0px");
		dtFechaNac.getStyle().set("padding-top", "0px");
		*/
		vl.add(txtName);
		vl.add(txtApPaterno);
		vl.add(txtApMaterno);
		vl.add(dtFechaNac);
		div.add(vl);
		getFormLayout().add(div);
	}
	
	public void addImageLoader(){
		
		Div div = new Div();
		Div divImage = new Div();
		
		VerticalLayout vl = new VerticalLayout();
		
		buffer = new MemoryBuffer();
		upload = new Upload(buffer);
		upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

		upload.addSucceededListener(event -> {
		    Component component = createComponent(event.getMIMEType(),
		            event.getFileName(), buffer.getInputStream());
		    divImage.removeAll();
		    showOutput(event.getFileName(), component, divImage);
		});
		
		
		vl.add(upload);
		vl.add(divImage);
		div.add(vl);
		getFormLayout().add(div);
	}

	public void addTutorData(){
		
//		txtTutor.getStyle().set("width", "100%").set("padding-top", "0px");
//		txtEmail.getStyle().set("width", "100%").set("padding-top", "0px");
//		txtPhone1.getStyle().set("width", "100%").set("padding-top", "0px");
//		txtPhone2.getStyle().set("width", "100%").set("padding-top", "0px");
		txtEmail.setValueChangeMode(ValueChangeMode.EAGER);
//		
		Div divTutorPhone = new Div();
		VerticalLayout vlTutorPhone = new VerticalLayout();
		
		Div divEmailPhone = new Div();
		VerticalLayout vlEmailPhone = new VerticalLayout();
		/*
		txtTutor.getStyle().set("padding-top", "0px");
		txtPhone1.getStyle().set("padding-top", "0px");
		txtEmail.getStyle().set("padding-top", "0px");
		txtPhone2.getStyle().set("padding-top", "0px");
		*/
		txtPhone1.setPattern("^\\d{1,10}$"); //10 digitos
		txtPhone1.setPreventInvalidInput(true);
		
		txtPhone2.setPattern("^\\d{1,10}$"); //10 digitos
		txtPhone2.setPreventInvalidInput(true);
		
		txtTutor.getStyle().set("width", "100%").set("padding-top", "0px");
		txtPhone1.getStyle().set("width", "100%").set("padding-top", "0px");
		txtEmail.getStyle().set("width", "100%").set("padding-top", "0px");
		txtPhone2.getStyle().set("width", "100%").set("padding-top", "0px");
		
		getBinder().forField(txtTutor)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.bind(AlumnosVO::getAlumnoTutor, AlumnosVO::setAlumnoTutor); //Establece setter y getter para su bindeo
		
		getBinder().forField(txtEmail)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withValidator(new EmailValidator("Email incorrecto"))
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.bind(AlumnosVO::getAlumnoTutorEmail, AlumnosVO::setAlumnoTutorEmail); //Establece setter y getter para su bindeo
		
		getBinder().forField(txtPhone1)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.bind(AlumnosVO::getAlumnoTutorTelefono1, AlumnosVO::setAlumnoTutorTelefono1); //Establece setter y getter para su bindeo
		
		getBinder().forField(txtPhone2)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.bind(AlumnosVO::getAlumnoTutorTelefono2, AlumnosVO::setAlumnoTutorTelefono2); //Establece setter y getter para su bindeo
		
		
		vlTutorPhone.add(txtTutor);
		vlTutorPhone.add(txtPhone1);
		divTutorPhone.add(vlTutorPhone);
		getFormLayout().add(divTutorPhone);
		
		vlEmailPhone.add(txtEmail);
		vlEmailPhone.add(txtPhone2);
		divEmailPhone.add(vlEmailPhone);
		getFormLayout().add(divEmailPhone);
		
	}
	
	@Override
	protected void confirmDelete() {
		// TODO Auto-generated method stub
		
	}

	public TextField getTxtName() {
		return txtName;
	}

	public void setTxtName(TextField txtName) {
		this.txtName = txtName;
	}

	
	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public MemoryBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(MemoryBuffer buffer) {
		this.buffer = buffer;
	}

	////////////////////////////////////////////////////
	////////////////////////////////////////////////////
	private Component createComponent(String mimeType, String fileName,
            InputStream stream) {
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
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
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
            
            image.getStyle()
        	.set("width", "120px")
        	.set("height", "140px");

            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return new Div();

    }

    private void showOutput(String text, Component content,
            HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }
	
}
