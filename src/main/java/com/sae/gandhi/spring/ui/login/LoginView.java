package com.sae.gandhi.spring.ui.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
public class LoginView extends VerticalLayout {
        /**
         * 
         */
	private static final long serialVersionUID = 1L;

		public static final String ROUTE = "login";

        private LoginOverlay login = new LoginOverlay(); // 

        public LoginView(){
            login.setAction("login"); // 
            login.setOpened(true); //s 
            login.setTitle("Gandhi");
            login.setDescription("Inicio de sesión");
            getElement().appendChild(login.getElement()); // 
        }
}