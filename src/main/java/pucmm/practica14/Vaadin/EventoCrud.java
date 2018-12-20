package pucmm.practica14.Vaadin;

import ch.qos.logback.core.boolex.EventEvaluatorBase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import pucmm.practica14.model.Evento;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.service.EventoServiceImpl;
import pucmm.practica14.service.UsuarioServiceImpl;


@Route("eventos")
@Component
@UIScope
public class EventoCrud extends VerticalLayout {

    EventoServiceImpl eventoService;
    UsuarioServiceImpl usuarioService;

    Grid<Evento> tablaEventos;
    TextField tfTitulo;
    TextField tfDescripcion;
    DatePicker fecha;

    Button btnAgregar, btnEliminar, btnEditar;
    Binder<Evento> binder;

    DataProvider<Evento, Void> dataProvider;

    Evento eventoSeleccionado;


    /**
     * Constructor aplicando la inyección de dependecia
     * @param eventoService
     */

    public EventoCrud(@Autowired EventoServiceImpl eventoService, @Autowired UsuarioServiceImpl usuarioService){

        this.eventoService = eventoService;
        this.usuarioService = usuarioService;
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
                    return eventoService.eventosPaginados(offset, limit).stream();
                    //return eventoService.buscarTodosEventos().stream();

                   // return  eventoService.findAllByFecha(fecha).stream();
                },
                query -> {
                    //Indicando la cantidad maxima de elementos.
                    return Math.toIntExact(eventoService.eventosPaginados(0, 5).size());
                }
        );

        binder = new Binder<>();
        tablaEventos = new Grid<>();
        tablaEventos.setDataProvider(dataProvider);
        tablaEventos.addColumn(Evento::getId).setHeader("Codigo");
        tablaEventos.addColumn(Evento::getTitulo).setHeader("Titulo");
        tablaEventos.addColumn(Evento::getDescripcion).setHeader("Descripcion");
        tablaEventos.addColumn(Evento::getFecha).setHeader("Fecha");
        tablaEventos.addColumn(new NativeButtonRenderer<Evento>("Elminiar", e->{
            Notification.show("Eliminando el evento: "+e.getId());
            eventoService.borrarEventoPorId(e.getId());
            dataProvider.refreshAll();
        })).setHeader("Acciones");

        //evento de la tabla
        tablaEventos.addSelectionListener(s->{
            if(s.getFirstSelectedItem().isPresent()){
                eventoSeleccionado= s.getFirstSelectedItem().get();
                binder.readBean(eventoSeleccionado);
                btnEliminar.setEnabled(true);
            }else{
                tfTitulo.clear();
                tfDescripcion.clear();
                fecha.clear();

                btnEliminar.setEnabled(false);
            }
        });

        tablaEventos.setWidth("50%");
        //los campos.
        tfTitulo = new TextField("Titulo");
        tfDescripcion=new TextField("Descripcion");
        fecha=new DatePicker("Fecha");


        btnAgregar = new Button("Agregar", e->{
            try {
                Evento tempEvento= new Evento();
                binder.writeBean(tempEvento);
                eventoService.crearEvento(tempEvento);
                for (Usuario usuario : usuarioService.buscarTodosUsuarios()){

                    eventoService.enviarCorreo(usuario.getEmail(), tempEvento.getTitulo(), tempEvento.getDescripcion());
                }
                //refrescando el data set.
                dataProvider.refreshItem(tempEvento);
                dataProvider.refreshAll();


            }catch (ValidationException ex){
                Notification.show("Error...: "+ex.getMessage());
            }

        });

        tfTitulo.clear();
        tfDescripcion.clear();
        fecha.clear();

        btnEliminar = new Button("Eliminar", e->{
            eventoService.borrarEventoPorId(eventoSeleccionado.getId());
            dataProvider.refreshAll();
        });
        btnEliminar.setEnabled(false);


        binder.forField(tfTitulo).asRequired("Debe indicar un titulo")
                .bind(Evento::getTitulo, Evento::setTitulo);

        binder.forField(tfDescripcion).asRequired("Debe indicar una descripcion")
                .bind(Evento::getDescripcion, Evento::setDescripcion);

        binder.forField(fecha).asRequired("Debe indicar una fecha")
                .bind(Evento::getFecha, Evento::setFecha);


        //layout para formularios.

        FormLayout fl = new FormLayout();
        fl.add(tfTitulo);
        fl.add(tfDescripcion);
        fl.add(fecha);

        HorizontalLayout accionesForm = new HorizontalLayout(btnAgregar, btnEliminar);
        VerticalLayout vfl = new VerticalLayout(fl, accionesForm);

        //agregando el diseño.
        HorizontalLayout hz = new HorizontalLayout(tablaEventos, vfl);
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
