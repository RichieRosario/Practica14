package pucmm.practica14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pucmm.practica14.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Rol findByNombreRol(String rol);

}
