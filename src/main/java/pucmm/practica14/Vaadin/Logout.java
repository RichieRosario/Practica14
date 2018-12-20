package pucmm.practica14.Vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.scopes.VaadinUIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import pucmm.practica14.model.Rol;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.service.RolServiceImpl;
import pucmm.practica14.service.UsuarioServiceImpl;

import javax.servlet.http.Cookie;

@Route("logout")
@UIScope
public class Logout extends VerticalLayout {



    UsuarioServiceImpl usuarioService;
    RolServiceImpl rolService;



    public Logout(@Autowired UsuarioServiceImpl usuarioService, @Autowired RolServiceImpl rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        Label l = new Label("Cerrando la sesión");
        Button login = new Button("Volver al login");


        add(login);

        login.addClickListener(event ->{
            Cookie c = new Cookie("user", "");
            c.setMaxAge(0);
            c.setPath("/");
            VaadinService.getCurrentResponse().addCookie(c);
            VaadinSession.getCurrent().close();
            getUI().get().navigate("login");

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