package pucmm.practica14.Vaadin;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pucmm.practica14.model.Rol;
import pucmm.practica14.service.RolServiceImpl;


import java.awt.*;

@Route("roles")
@Component
@UIScope
public class RolCrud extends VerticalLayout {

    RolServiceImpl rolService;

    Grid<Rol> tablaRoles;
    TextField tfNombreRol;

    Button btnAgregar, btnEliminar, btnEditar;
    Binder<Rol> binder;

    DataProvider<Rol, Void> dataProvider;

    Rol rolSeleccionado;
    /**
     * Constructor aplicando la inyección de dependecia
     * @param rolService
     */

    public RolCrud(@Autowired RolServiceImpl rolService){

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
                    return rolService.buscarTodosRoles().stream();
                },
                query -> {
                    //Indicando la cantidad maxima de elementos.
                    return Math.toIntExact(rolService.buscarTodosRoles().size());
                }
        );

        binder = new Binder<>();
        tablaRoles = new Grid<>();
        tablaRoles.setDataProvider(dataProvider);
        tablaRoles.addColumn(Rol::getId).setHeader("Codigo");
        tablaRoles.addColumn(Rol::getNombreRol).setHeader("Rol");
        tablaRoles.addColumn(new NativeButtonRenderer<Rol>("Elminiar", e->{
            Notification.show("Eliminando el rol: "+e.getId());
            rolService.borrarRolPorId(e.getId());
            dataProvider.refreshAll();
        })).setHeader("Acciones");

        //evento de la tabla
        tablaRoles.addSelectionListener(s->{
            if(s.getFirstSelectedItem().isPresent()){
                rolSeleccionado = s.getFirstSelectedItem().get();
                binder.readBean(rolSeleccionado);
                btnEditar.setEnabled(true);
                btnEliminar.setEnabled(true);
            }else{
                tfNombreRol.clear();
                btnEditar.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        });

        tablaRoles.setWidth("50%");
        //los campos.
        tfNombreRol = new TextField("Rol");

        btnAgregar = new Button("Agregar", e->{
            try {
                Rol tempRol = new Rol();
                binder.writeBean(tempRol);
                rolService.crearRol(tempRol);
                //refrescando el data set.
                dataProvider.refreshItem(tempRol);
                dataProvider.refreshAll();


            }catch (ValidationException ex){
                Notification.show("Error...: "+ex.getMessage());
            }

        });

      tfNombreRol.clear();

        btnEditar = new Button("Editar", e->{
            rolSeleccionado.setNombreRol(tfNombreRol.getValue());
            rolService.actualizarRol(rolSeleccionado);
            dataProvider.refreshAll();
        });
        btnEditar.setEnabled(false);

        btnEliminar = new Button("Eliminar", e->{
            rolService.borrarRolPorId(rolSeleccionado.getId());
            dataProvider.refreshAll();
        });
        btnEliminar.setEnabled(false);


        binder.forField(tfNombreRol).asRequired("Debe indicar un rol")
                .bind(Rol::getNombreRol, Rol::setNombreRol);


        //layout para formularios.

        FormLayout fl = new FormLayout();
        fl.add(tfNombreRol);

        HorizontalLayout accionesForm = new HorizontalLayout(btnAgregar, btnEditar, btnEliminar);
        VerticalLayout vfl = new VerticalLayout(fl, accionesForm);

        //agregando el diseño.
        HorizontalLayout hz = new HorizontalLayout(tablaRoles, vfl);
        hz.setSizeFull();


        //
        add(hz);
        setSizeFull();
        //refrescando la tabla.
        dataProvider.refreshAll();
    }
    private void crearRutas(){
        HorizontalLayout caja = new HorizontalLayout();
        //con RouterLink el renderizado no recarga la pagina.
        caja.add(new RouterLink("Calendario", Calendario.class));
        caja.add(new RouterLink("Eventos", EventoCrud.class));
        caja.add(new RouterLink("Usuarios", UsuarioCrud.class));
        caja.add(new RouterLink("Roles", RolCrud.class));

        add(caja);
    }
}
