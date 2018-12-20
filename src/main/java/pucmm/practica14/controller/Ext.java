package pucmm.practica14.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import pucmm.practica14.service.UsuarioServiceImpl;

@Route("ext")
@UIScope

public class Ext extends VerticalLayout {

    @Autowired
    UsuarioServiceImpl usuarioService;


    public Ext(@Autowired   UsuarioServiceImpl usuarioService) {
        // two components:
        HorizontalLayout hz = new HorizontalLayout();
        TextField matricula = new TextField();
        Label labelMatricula = new Label("Matricula");
        Button validate = new Button("Validar");

        hz.add(labelMatricula,matricula, validate);
        FormLayout fz = new FormLayout();
        fz.add(hz);


        add(fz);
        // add logic through event listeners
        validate.addClickListener(event ->{
            Notification.show("asdasdasd") ;

            try {

            }catch (){}
        });
    }
}