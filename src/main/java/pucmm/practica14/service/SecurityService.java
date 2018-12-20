package pucmm.practica14.service;

public interface SecurityService {

    String usuarioLogueado();

    void autoLogin(String username, String password);
}
