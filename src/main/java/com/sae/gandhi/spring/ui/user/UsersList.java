package com.sae.gandhi.spring.ui.user;

import org.springframework.security.access.annotation.Secured;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.utils.SaeUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

}
