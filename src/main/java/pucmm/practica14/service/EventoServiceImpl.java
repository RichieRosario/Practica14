package pucmm.practica14.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pucmm.practica14.model.Evento;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.repository.EventoRepository;
import pucmm.practica14.repository.UsuarioRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service("eventoService")

public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    public JavaMailSender emailSender;

    @Transactional
    public void crearEvento(Evento evento){

        eventoRepository.save(evento);
    }


    public void actualizarEvento(Evento evento){
        eventoRepository.save(evento);
    }
    public void borrarEventoPorId(long id){
        eventoRepository.deleteById(id);
    }
    public void borrarTodosLosEventos(){
        eventoRepository.deleteAll();
    }

    @Override
    public Evento buscarPorId(long id){
        return eventoRepository.findById(id).get();
    }
    public List<Evento> findAllByFecha(LocalDate fecha) {
        return eventoRepository.findAllByFecha(fecha);
    }
    public List<Evento> buscarTodosEventos(){

        return eventoRepository.findAll();
    }


    public void enviarCorreo(String para, String asunto, String texto){

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(texto);
        emailSender.send(mensaje);
    }

    public List<Evento> eventosPaginados(int offset, int limit){
        return eventoRepository.eventosPaginados(offset, limit);
    }
}

