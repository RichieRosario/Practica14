package pucmm.practica14.Vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.SpringVaadinSession;
import com.vaadin.flow.spring.VaadinServletConfiguration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.FormLayout;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import pucmm.practica14.model.Rol;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.service.RolServiceImpl;
import pucmm.practica14.service.UsuarioServiceImpl;

import javax.servlet.http.Cookie;

@Route("login")
@UIScope
public class Login extends VerticalLayout {



    UsuarioServiceImpl usuarioService;
    RolServiceImpl rolService;



    public Login(@Autowired UsuarioServiceImpl usuarioService, @Autowired RolServiceImpl rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;


        if(usuarioService.buscarTodosUsuarios().size() == 0) {
            Usuario u = new Usuario();
            u.setActive(1);
            u.setEmail("ricardojoserosario1431@gmail.com");
            u.setNombre("admin");
            u.setUsername("admin");
            u.setPassword("admin");
            Rol r = new Rol();
            r.setNombreRol("ADMIN");
            rolService.crearRol(r);
            Rol r2 = new Rol();
            r2.setNombreRol("GERENTE");
            rolService.crearRol(r2);
            u.setRol(r);
            usuarioService.crearUsuario(u);
        }

        TextField username = new TextField("Username");
        TextField password = new TextField("Password");
        Button login = new Button("Login");


        add(username,password, login);


        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.CENTER);

        if(getCookieByName("user")!=null){
            login.setEnabled(false);
        }
        // add logic through event listeners
        login.addClickListener(event ->{

            if(usuarioService.Success(username.getValue(), (password.getValue()))) {
                Cookie c = new Cookie("user", username.getValue());
                c.setMaxAge(12000);
                c.setPath("/");
                VaadinSession n = new VaadinSession(com.vaadin.server.VaadinService.getCurrent());
                VaadinSession.setCurrent(n);
                VaadinService.getCurrentResponse().addCookie(c);


               UI.getCurrent().navigate("calendario");
            }

            else{
                Notification.show("Datos incorrectos.", 1000, Notification.Position.MIDDLE);

                }


        });
    }

    private Cookie getCookieByName(String name) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
    
}