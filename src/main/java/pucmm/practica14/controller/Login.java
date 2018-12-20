package pucmm.practica14.controller;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;

import pucmm.practica14.service.UsuarioServiceImpl;

@Route("login")
@UIScope
@StyleSheet("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
public class Login extends VerticalLayout {

    @Autowired
    UsuarioServiceImpl usuarioService;


    public Login() {
        // two components:
        TextField username = new TextField("Username");
        TextField password = new TextField("Password");
        Button login = new Button("Login");
        // add them to "this" layout (a VerticalLayout)
        add(username, password, login);
        // add logic through event listeners
        login.addClickListener(event ->{

            if(usuarioService.Success(username.getValue(), (password.getValue()))){
                getUI().get().navigate("hello-world");
                System.out.println("entre");
            }

            else{
                System.out.println("no entre");
                Dialog dialog = new Dialog();
                dialog.add(new Label("Close me with the esc-key or an outside click"));

                dialog.setWidth("400px");
                dialog.setHeight("150px");

                Button confirmButton = new Button("OK", event2 -> {

                    dialog.close();
                });

                dialog.add(confirmButton);
            dialog.open();
            }
        });
    }
}