package pucmm.practica14.Vaadin;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pucmm.practica14.model.Rol;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.service.RolServiceImpl;
import pucmm.practica14.service.UsuarioService;
import pucmm.practica14.service.UsuarioServiceImpl;

import javax.servlet.http.Cookie;

@Route("configuracion")
@UIScope
public class Configuracion extends VerticalLayout  {

    UsuarioServiceImpl usuarioService;


    TextField tfNombre;

    TextField tfEmail;

    Button btnAgregar, btnEliminar, btnEditar;
    Binder<Usuario> binder;

    DataProvider<Rol, Void> dataProvider;

    Usuario usuarioactual;




    public Configuracion(@Autowired UsuarioServiceImpl usuarioService){



        this.usuarioService = usuarioService;
        crearRutas();
        usuarioactual = usuarioService.findByUsername(getCookieByName("user").getValue());


        binder = new Binder<>();


        btnAgregar = new Button("Guardar", e->{

                Usuario u = usuarioService.findByUsername(getCookieByName("user").getValue());
                u.setNombre(tfNombre.getValue());
                u.setEmail(tfEmail.getValue());

                usuarioService.actualizarUsuario(u);

                Notification.show("Cambios almacenados con éxito", 100, Notification.Position.MIDDLE);


        });





        FormLayout fl = new FormLayout();
        tfNombre=new TextField("Nombre");
        tfEmail=new TextField("Correo");
        fl.add(tfNombre);
        fl.add(tfEmail);
        HorizontalLayout accionesForm = new HorizontalLayout(btnAgregar);
        VerticalLayout vfl = new VerticalLayout(fl, accionesForm);

        //agregando el diseño.
        HorizontalLayout hz = new HorizontalLayout(vfl);
        hz.setSizeFull();


        //
        add(hz);
        setSizeFull();
        binder.forField(tfNombre).asRequired("Debe indicar un nombre")
                .bind(Usuario::getNombre, Usuario::setNombre);
        binder.forField(tfEmail).asRequired("Debe indicar un correo")
                .bind(Usuario::getEmail, Usuario::setEmail);

        tfNombre.setValue(usuarioactual.getNombre());
        tfEmail.setValue(usuarioactual.getEmail());



    }



    private void crearRutas(){
        HorizontalLayout caja = new HorizontalLayout();
        //con RouterLink el renderizado no recarga la pagina.
        caja.add(new RouterLink("Calendario", Calendario.class));
        caja.add(new RouterLink("Eventos", EventoCrud.class));

        if(getCookieByName("user").getValue().equals("admin")) {
            caja.add(new RouterLink("Usuarios", UsuarioCrud.class));
            caja.add(new RouterLink("Roles", RolCrud.class));
        }

        caja.add(new RouterLink("Configuración", Configuracion.class));
        caja.add(new RouterLink("Cerrar sesión", Logout.class));

        caja.add(new Label("Bienvenido, "+getCookieByName("user").getValue()));

        add(caja);
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
