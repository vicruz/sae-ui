package com.sae.gandhi.spring;

import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.security.SecurityUtils;
import com.sae.gandhi.spring.service.UsuariosService;
import com.sae.gandhi.spring.ui.cursos.CursosList;
import com.sae.gandhi.spring.ui.dashboard.DashboardUI;
import com.sae.gandhi.spring.ui.pagos.CostosList;
import com.sae.gandhi.spring.ui.students.StudentsList;
import com.sae.gandhi.spring.ui.user.UsersList;
import com.sae.gandhi.spring.utils.SaeConstants;
import com.sae.gandhi.spring.vo.UsuariosVO;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;


/**
 * The main view contains a simple label element and a template element.
 */
@HtmlImport("styles/shared-styles.html")
@Viewport(SaeConstants.VIEWPORT)
@Route("")
public class MainView extends Div implements RouterLayout, PageConfigurator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7313168790614726485L;

	@Autowired
	public MainView(//SessionService sessionService, 
			UsuariosService usuariosService) {

		UsuariosVO usuarioVO = usuariosService.findByUsuarioLogin(SecurityUtils.getUsername());
		//sessionService.setUsuarioVO(usuarioVO);
		boolean isAdmin = false;
		if(usuarioVO!=null && usuarioVO.getUsuarioRol()){
			isAdmin = true;
		}
		UI.getCurrent().getSession().lock();
		UI.getCurrent().getSession().setAttribute("isAdmin", isAdmin);
		UI.getCurrent().getSession().unlock();
		
		H2 title = new H2("Gandhi");
		title.addClassName("main-layout__title");

		RouterLink dahsboard = new RouterLink(null, DashboardUI.class);
		dahsboard.add(new Icon(VaadinIcon.LIST), new Text("Dashboard"));
		// Only show as active for the exact URL, but not for sub paths
		dahsboard.setHighlightCondition(HighlightConditions.sameLocation());
		dahsboard.getElement().getStyle().set("padding-right", "5px");
		
/*
		RouterLink categories = new RouterLink(null, CategoriesList.class);
		categories.add(new Icon(VaadinIcon.ARCHIVES), new Text("Categories"));
		*/
		RouterLink payments = new RouterLink(null, CostosList.class);
		payments.add(new Icon(VaadinIcon.COIN_PILES), new Text("Costos"));
		payments.getElement().getStyle().set("padding-right", "5px");
		
		RouterLink courses = new RouterLink(null, CursosList.class);
		courses.add(new Icon(VaadinIcon.ACADEMY_CAP), new Text("Cursos"));
		courses.getElement().getStyle().set("padding-right", "5px");
		
		RouterLink students = new RouterLink(null, StudentsList.class);
		students.add(new Icon(VaadinIcon.DIPLOMA_SCROLL), new Text("Alumnos"));
		students.getElement().getStyle().set("padding-right", "5px");
		
		RouterLink users = new RouterLink(null, UsersList.class);
		users.add(new Icon(VaadinIcon.USER), new Text("Usuarios"));
		users.getElement().setAttribute("theme", "icon-on-top");
		users.getElement().getStyle().set("padding-right", "5px");
		
		RouterLink logout = new RouterLink();
		logout.add(VaadinIcon.ARROW_RIGHT.create(), new Text("Salir"));
		Div divLogout = new Div();
		divLogout.add(logout);
		divLogout.addClickListener(e->{ 
			UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
			UI.getCurrent().getSession().close();
		});
		divLogout.getElement().getStyle().set("padding-right", "5px");
		divLogout.getElement().getStyle().set("cursor", "pointer");
		
		

		//Div navigation = new Div(reviews, categories, payments, courses);
		Div navigation = new Div(dahsboard, payments, courses, students);//,users,divLogout);
		if (SecurityUtils.isAccessGranted(UsersList.class)) {
			navigation.add(users);
		}
		navigation.add(divLogout);
		
		navigation.addClassName("main-layout__nav");

		Div header = new Div(title,navigation);
		header.addClassName("main-layout__header");
		add(header);

		addClassName("main-layout");

	}

	@Override
	public void configurePage(InitialPageSettings settings) {
		settings.addMetaTag("apple-mobile-web-app-capable", "yes");
		settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
	}

}
