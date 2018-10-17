package com.sae.gandhi.spring;

import com.sae.gandhi.spring.ui.alumnos.CategoriesList;
import com.sae.gandhi.spring.ui.cursos.CursosList;
import com.sae.gandhi.spring.ui.pagos.CostosList;
import com.sae.gandhi.spring.ui.students.StudentsList;
import com.sae.gandhi.spring.utils.SaeConstants;
import com.vaadin.flow.component.Text;
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

	public MainView() {

		H2 title = new H2("Gandhi");
		title.addClassName("main-layout__title");

		RouterLink reviews = new RouterLink();
		reviews.add(new Icon(VaadinIcon.LIST), new Text("Dashboard"));
		// Only show as active for the exact URL, but not for sub paths
		reviews.setHighlightCondition(HighlightConditions.sameLocation());
/*
		RouterLink categories = new RouterLink(null, CategoriesList.class);
		categories.add(new Icon(VaadinIcon.ARCHIVES), new Text("Categories"));
		*/
		RouterLink payments = new RouterLink(null, CostosList.class);
		payments.add(new Icon(VaadinIcon.COIN_PILES), new Text("Costos"));
		
		RouterLink courses = new RouterLink(null, CursosList.class);
		courses.add(new Icon(VaadinIcon.ACADEMY_CAP), new Text("Cursos"));
		
		RouterLink students = new RouterLink(null, StudentsList.class);
		students.add(new Icon(VaadinIcon.DIPLOMA_SCROLL), new Text("Alumnos"));

		//Div navigation = new Div(reviews, categories, payments, courses);
		Div navigation = new Div(reviews, payments, courses, students);
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
