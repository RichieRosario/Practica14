package pucmm.practica14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pucmm.practica14.model.Evento;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByFecha(LocalDate fecha);

    @Query(value = "SELECT * FROM evento m offset(?1) limit(?2)", nativeQuery = true)
    List<Evento> eventosPaginados(int offset, int limit);

     //otra manera

    @Query("select evento from Evento evento where evento.fecha between ?1 and ?2")
    List<Evento> eventosPorFecha(Date fechaInicio, Date fechaFin);

}
