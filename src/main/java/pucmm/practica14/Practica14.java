package pucmm.practica14;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;
import pucmm.practica14.Vaadin.Calendario;
import pucmm.practica14.Vaadin.EventoCrud;
import pucmm.practica14.Vaadin.RolCrud;
import pucmm.practica14.Vaadin.UsuarioCrud;
import pucmm.practica14.service.UsuarioService;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Practica14  {

	public static void main(String[] args) {
		SpringApplication.run(Practica14.class, args);
	}

}

