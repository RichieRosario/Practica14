package pucmm.practica14.Vaadin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.server.VaadinSession;
import javafx.scene.control.PasswordField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pucmm.practica14.model.Rol;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.service.RolServiceImpl;
import pucmm.practica14.service.UsuarioServiceImpl;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

@Route("usuarios")
@UIScope
public class UsuarioCrud extends VerticalLayout {

    @Autowired
    UsuarioServiceImpl usuarioService;
    @Autowired
    RolServiceImpl rolService;

    Grid<Usuario> tablaUsuarios;
    TextField tfUserName;
    TextField tfPassword;
    TextField tfEmail;
    TextField tfNombre;
    ComboBox<String> cbRoles;

    Button btnAgregar, btnEliminar, btnEditar;
    Binder<Usuario> binder;
    Binder<Rol> binderRol;

    DataProvider<Usuario, Void> dataProvider;

    Usuario usuarioSeleccionado;





    public UsuarioCrud(@Autowired UsuarioServiceImpl usuarioService, @Autowired RolServiceImpl rolService){

      this.usuarioService = usuarioService;
        this.rolService = rolService;

        crearRutas();


        //Instanciando el dato provider.
        dataProvider = DataProvider.fromCallbacks(
                //indicando la consulta que retorna la información
                query -> {

                    // Indicando el primer elemento cargado.
                    int offset = query.getOffset();
                    System.out.println("El offset: "+offset);
                    // La cantidad maxima a cargar
                    int limit = query.getLimit();
                    System.out.println("El limit: "+limit);
                    //Enviando el flujo
                  //  return usuarioService.usuariosPaginados(offset, limit).stream();
                    return usuarioService.buscarTodosUsuarios().stream();
                },
                query -> {
                    //Indicando la cantidad maxima de elementos.
                    return Math.toIntExact(usuarioService.cantidadUsuarios());
                }
        );

        binder = new Binder<>();
        tablaUsuarios = new Grid<>();
        tablaUsuarios.setDataProvider(dataProvider);
        tablaUsuarios.addColumn(Usuario::getId).setHeader("Codigo");
        tablaUsuarios.addColumn(Usuario::getNombre).setHeader("Nombre");
        tablaUsuarios.addColumn(Usuario::getEmail).setHeader("Correo");
        tablaUsuarios.addColumn(Usuario::getNombreRol).setHeader("Rol");
        tablaUsuarios.addColumn(new NativeButtonRenderer<Usuario>("Eliminar", e->{
            Notification.show("Eliminando el usuario: "+e.getId());
            usuarioService.borrarUsuarioPorId(e.getId());
            dataProvider.refreshAll();
        })).setHeader("Acciones");

        //evento de la tabla
        tablaUsuarios.addSelectionListener(s->{
            if(s.getFirstSelectedItem().isPresent()){
                usuarioSeleccionado = s.getFirstSelectedItem().get();
                binder.readBean(usuarioSeleccionado);
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }else{
                tfUserName.clear();
                tfEmail.clear();
                tfNombre.clear();
            //    tfPassword.clear();
                btnEditar.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        });

        tablaUsuarios.setWidth("50%");
        //los campos.
        tfUserName = new TextField("Username");
        tfNombre=new TextField("Nombre");
        tfEmail=new TextField("Correo");
        cbRoles = new ComboBox("Rol");
       // tfPassword = new PasswordField();

        List<Rol> roles = rolService.buscarTodosRoles();
        List<String> nombresRol = new ArrayList<>();
        for (Rol rol : roles){
            nombresRol.add(rol.getNombreRol());
        }

        cbRoles.setItems(nombresRol);

        btnAgregar = new Button("Agregar", e->{
            try {
                Usuario tempUsuario = new Usuario();
                tempUsuario.setRol(rolService.findByNombreRol(cbRoles.getValue()));
                binder.writeBean(tempUsuario);
                usuarioService.crearUsuario(tempUsuario);
                //refrescando el data set.
                dataProvider.refreshItem(tempUsuario);
                dataProvider.refreshAll();


            }catch (ValidationException ex){
                Notification.show("Error...: "+ex.getMessage());
            }

        });



        tfUserName.clear();
        tfEmail.clear();
        tfNombre.clear();


        btnEditar = new Button("Editar", e->{
            usuarioSeleccionado.setUsername(tfUserName.getValue());
            usuarioSeleccionado.setEmail(tfEmail.getValue());
            usuarioSeleccionado.setNombre(tfNombre.getValue());
            usuarioSeleccionado.setRol(rolService.findByNombreRol(cbRoles.getValue()));
            usuarioService.actualizarUsuario(usuarioSeleccionado);
            dataProvider.refreshAll();
        });
        btnEditar.setEnabled(false);


        btnEliminar = new Button("Eliminar", e->{
            usuarioService.borrarUsuarioPorId(usuarioSeleccionado.getId());
            dataProvider.refreshAll();
        });
        btnEliminar.setEnabled(false);


        binder.forField(tfUserName).asRequired("Debe indicar un username")
                .bind(Usuario::getUsername, Usuario::setUsername);

        binder.forField(tfNombre).asRequired("Debe indicar un nombre")
                .bind(Usuario::getNombre, Usuario::setNombre);

        binder.forField(tfEmail).asRequired("Debe indicar un correo")
                .bind(Usuario::getEmail, Usuario::setEmail);


        //layout para formularios.

        FormLayout fl = new FormLayout();
        fl.add(tfUserName);
        fl.add(tfNombre);
        fl.add(tfEmail);
        fl.add(cbRoles);
        HorizontalLayout accionesForm = new HorizontalLayout(btnAgregar,btnEditar, btnEliminar);
        VerticalLayout vfl = new VerticalLayout(fl, accionesForm);

        //agregando el diseño.
        HorizontalLayout hz = new HorizontalLayout(tablaUsuarios, vfl);
        hz.setSizeFull();


        //
        add(hz);
        setSizeFull();
        //refrescando la tabla.
        dataProvider.refreshAll();
        Cookie c = getCookieByName("user");
        if (c == null) {
            UI.getCurrent().navigate("login");
        } else if(usuarioService.findByUsername(c.getValue()).getNombreRol().equals("GERENTE")){
            UI.getCurrent().navigate("calendario");
        }

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
