package com.sae.gandhi.spring.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.UsuariosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.utils.SaeUtils;
import com.sae.gandhi.spring.vo.UsuariosVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "users", layout = MainView.class)
@PageTitle("Usuarios")
@Secured(SaeUtils.ROLE_ADMIN)
public class UsersList extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final TextField searchField = new TextField("", "Buscar Usuario");
    private final H3 header = new H3("Usuarios");
    
    private final UsersEditorDialog form = new UsersEditorDialog(this::saveUser, this::deleteUser);
    
    private UsuariosService usuariosService;
    private List<UsuariosVO> lstUsuarios;
    
    private final Grid<UsuariosVO> grid;
    private Editor<UsuariosVO> editor;
    private Binder<UsuariosVO> binder;
    
    @Autowired
    public UsersList(UsuariosService usuariosService){
    	this.usuariosService = usuariosService;
    	
    	grid = new Grid<>();
    	binder = new Binder<>(UsuariosVO.class);
    	
    	initView();
    	addSearchBar();
    	addContent();
    	
    }
    
    private void initView() {
        addClassName("usuarios-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }
     
    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        Button newButton = new Button("Nuevo Usuario",event -> form.open(new UsuariosVO(),
                AbstractEditorDialog.Operation.ADD));
        newButton.setIcon(new Icon(VaadinIcon.PLUS));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> form.open(new UsuariosVO(),
                AbstractEditorDialog.Operation.ADD));

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }
    
    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);
        
        editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        grid.addColumn(UsuariosVO::getUsuarioLogin).setHeader("Login").setWidth("6em").setResizable(true);
        Column<UsuariosVO> editNameColumn = grid.addColumn(UsuariosVO::getUsuarioNombre).setHeader("Nombre").setWidth("6em").setResizable(true);
        Column<UsuariosVO> editEmailColumn = grid.addColumn(UsuariosVO::getUsuarioEmail).setHeader("Email").setWidth("6em").setResizable(true);
        
        //Se envian metodos que cumplen con la funcion requerida
        Column<UsuariosVO> editCheckColumn = grid.addColumn(new ComponentRenderer<>(this::createAdminCheck)).setHeader("Admin").setFlexGrow(0);
        Column<UsuariosVO> editorColumn = grid.addComponentColumn(vo -> {
        	Button buttonEdit = new Button();
        	buttonEdit.setIcon(new Icon(VaadinIcon.PENCIL));
        	buttonEdit.addClassName("review__edit");
        	buttonEdit.getElement().setAttribute("theme", "tertiary");
        	buttonEdit.getElement().setAttribute("title", "Editar");
        	buttonEdit.addClickListener(e -> editor.editItem(vo));
        	
			return buttonEdit;
		}).setHeader("Editar");
        grid.setSelectionMode(SelectionMode.SINGLE);
        loadData();
        
        ///////////////////////////////////////
        //Campo de nombre
        Div divName = new Div();
        divName.setId("divName");
                
        TextField txtName = new TextField();
        binder.forField(txtName)
        .withValidator(name -> !name.isEmpty(),
                "El nombre no debe ser vacío!")
        .withStatusLabel(divName).bind("usuarioNombre");
        
        editNameColumn.setEditorComponent(txtName);
        
        ///////////////////////////////////////
        //Campo de Email
        Div divEmail = new Div();
        divEmail.getStyle().set("color", "red");
        divEmail.setId("email-validation");
        
        TextField txtEmail = new TextField();
        binder.forField(txtEmail)
        	.withValidator(new EmailValidator("Email invorrecto"))
        	.withStatusLabel(divEmail).bind("usuarioEmail");
       
        editEmailColumn.setEditorComponent(txtEmail);
		///////////////////////////////////////
		//Campo de Administrador
		Div divAdmin = new Div();
		divAdmin.getStyle().set("color", "red");
		divAdmin.setId("email-validation");
		
		Checkbox checkAdmin = new Checkbox();
		binder.bind(checkAdmin, "usuarioRol");
		editCheckColumn.setEditorComponent(checkAdmin);
		
		checkAdmin.addValueChangeListener(event ->{
			// Only updates from the client-side should be taken into account
		    if (event.isFromClient()) {
		    	
		    }
		    
		});
		
		
        
        
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
        
     	Div divButtons = new Div(save, cancel);
     	editorColumn.setEditorComponent(divButtons);
     	
     	// Al momento de guardar actualiza el registro
     	editor.addSaveListener(event -> {
     		if(event.getItem()!=null && event.getItem().getUsuarioLogin()!=null){
     			usuariosService.save(event.getItem());     			
     		}
     		System.out.println(event.getItem().toString());
     	});
     	
        
        container.add(header, grid);
        add(container);
    }
    
    //Boton de editar
    private Checkbox createAdminCheck(UsuariosVO usuario) {
    	Checkbox cbAdmin = new Checkbox();
    	cbAdmin.setReadOnly(true);
    	cbAdmin.setValue(false);
    	
    	if(usuario.getUsuarioRol()!=null &&usuario.getUsuarioRol()){
    		cbAdmin.setValue(true);
    	}
           
        return cbAdmin;
    }
    
    //Carga los datos del grid
    private void loadData(){
    	lstUsuarios = usuariosService.findAll();
        grid.setItems(lstUsuarios);
    }
    
    //Filtro 
    private void updateView() {
    	List<UsuariosVO> lstUsuariosGrid = new ArrayList<>();
        if (searchField.getValue().length() > 0 && !searchField.getValue().trim().equals("")) {
        	for(UsuariosVO vo: lstUsuarios){
        		if(vo.getUsuarioNombre().toUpperCase().contains(searchField.getValue().toUpperCase())){
        			lstUsuariosGrid.add(vo);
        		}
        	}
        } else {
        	lstUsuariosGrid = lstUsuarios;
        }
        grid.setItems(lstUsuariosGrid);
    }
    
    //Metodo de salvar
    private void saveUser(UsuariosVO usuario,
            AbstractEditorDialog.Operation operation) {
    	if(operation.getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())){
    		usuariosService.save(usuario);
    	}
    	
    	lstUsuarios.add(usuario);
    	
        Notification.show(
                "Usuario " + usuario.getUsuarioNombre() + " creado", 3000, Position.BOTTOM_END);
        updateView();
    }
    
    private void deleteUser(UsuariosVO costos) {
    	
    	
    }
    
}
