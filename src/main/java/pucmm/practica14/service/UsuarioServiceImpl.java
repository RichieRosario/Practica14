package pucmm.practica14.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import pucmm.practica14.model.Usuario;
import pucmm.practica14.repository.UsuarioRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service("usuarioService")

public class UsuarioServiceImpl implements UsuarioService {



    @Autowired
    private UsuarioRepository usuarioRepository;


    @Transactional
    public void crearUsuario(Usuario usuario){
        usuario.setPassword((usuario.getPassword()));
        usuarioRepository.save(usuario);
    }

    public boolean Success(String username, String password){
        Usuario user = usuarioRepository.findByUsername(username);
        boolean flag = false;
        if(user != null){
        if(user.getPassword() == password){
                flag= true;
        }}
        return flag;
    }



    public List<Usuario> usuariosPaginados(int offset, int limit){
        return usuarioRepository.usuariosPaginados(offset, limit);
    }

    public Integer cantidadUsuarios(){
        return usuarioRepository.cantidadUsuarios();
    }

    public void actualizarUsuario(Usuario usuario){
        usuarioRepository.save(usuario);
    }
    public void borrarUsuarioPorId(long id){
        usuarioRepository.deleteById(id);
    }
    public void borrarTodosLosUsuarios(){
        usuarioRepository.deleteAll();
    }
    public List<Usuario> buscarTodosUsuarios(){
        return usuarioRepository.findAll();
    }
    @Override
    public Usuario buscarPorId(long id){
        return usuarioRepository.findById(id).get();
    }
    public Usuario findByUsername(String username){
        return usuarioRepository.findByUsername(username);
    }

    public Usuario findByEmail(String email){return usuarioRepository.findByEmail(email);}
    public boolean usuarioExiste(Usuario usuario){
        return findByUsername(usuario.getUsername()) != null;
    }

}

