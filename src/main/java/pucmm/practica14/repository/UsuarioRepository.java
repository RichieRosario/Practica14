package pucmm.practica14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pucmm.practica14.model.Usuario;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);
    Usuario findByEmail(String email);

    @Query(value = "SELECT * FROM usuario ORDER BY id OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY;", nativeQuery = true)
    List<Usuario> usuariosPaginados(int offset, int limit);

    @Query("select count(usuario) from Usuario usuario")
    Integer cantidadUsuarios();

    

    @Query("select count(usuario) from Usuario usuario")
    Integer contar();

    @Query(value = "SELECT * FROM usuario m offset(?1) limit(?2)", nativeQuery = true)
    List<Usuario> paginacionUsuario(int offset, int limit);
}
