package pucmm.practica14.service;

import pucmm.practica14.model.Usuario;

import java.util.List;

public interface UsuarioService {

    void crearUsuario(Usuario usuario);
    void actualizarUsuario(Usuario usuario);
    void borrarUsuarioPorId(long id);
    void borrarTodosLosUsuarios();
    List<Usuario> buscarTodosUsuarios();
    Usuario buscarPorId(long id);
    Usuario findByUsername(String username);
    Usuario findByEmail(String email);
    boolean usuarioExiste(Usuario usuario);
    List<Usuario> usuariosPaginados(int offset, int limit);
    Integer cantidadUsuarios();


}
