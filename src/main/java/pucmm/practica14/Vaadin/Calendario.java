package pucmm.practica14.Vaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.calendar.CalendarComponent;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.Timezone;
import pucmm.practica14.model.Evento;
import pucmm.practica14.service.EventoService;
import pucmm.practica14.service.EventoServiceImpl;
import org.vaadin.calendar.data.AbstractCalendarDataProvider;

import javax.servlet.http.Cookie;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Route("calendario")
@UIScope
public class Calendario extends VerticalLayout {




    public static CalendarComponent<Evento> calendario = new CalendarComponent<Evento>()
                .withItemDateGenerator(Evento::getDate).withItemLabelGenerator(Evento::getTitulo);


    DatePicker fecha = new DatePicker("Fecha");
    TextField tfTitulo = new TextField("Titulo");
    TextField tfDescripcion = new TextField("Descripcion");

    Binder<Evento> binder = new Binder<>(Evento.class);


    @Autowired
    public static EventoServiceImpl eventoService;



    public Calendario(@Autowired EventoServiceImpl eventoService){



        Calendario.eventoService = eventoService;

        crearRutas();

        calendario.setDataProvider(new CustomDataProvider());

        calendario.addEventClickListener(evt -> {
            try {
                VerticalLayout verticalLayout = new VerticalLayout();
                Dialog dialog = new Dialog();
                dialog.add(verticalLayout);
                verticalLayout.add(tfTitulo);
                verticalLayout.add(tfDescripcion);
                verticalLayout.add(fecha);
                dialog.open();
                tfTitulo.setValue(evt.getDetail().getTitulo());
                tfDescripcion.setValue(evt.getDetail().getDescripcion());
                fecha.setValue(evt.getDetail().getFecha());
                NativeButton confirmButton = new NativeButton("Guardar", event -> {
                    Evento evento = new Evento();
                    evento.setId(evt.getDetail().getId());
                    evento.setTitulo(tfTitulo.getValue());
                    evento.setDescripcion(tfDescripcion.getValue());
                    evento.setFecha(fecha.getValue());
                    eventoService.actualizarEvento(evento);
                    dialog.close();
                    calendario.refresh();
                });

                NativeButton cancelButton = new NativeButton("Cancelar", event -> {
                    dialog.close();
                });

                dialog.add(confirmButton, cancelButton);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        add(calendario);


        calendario.addEventClickListener(e -> {

            //////////
        });
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

@SpringComponent
@UIScope
 class CustomDataProvider extends AbstractCalendarDataProvider<Evento> {
    @Override
    public Collection<Evento> getItems(Date desde, Date hasta) {
        List<Evento> events = Calendario.eventoService.buscarTodosEventos();
        return events;
    }


}
