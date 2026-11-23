package com.uniajc.hotellosolivos.dao;

import com.uniajc.hotellosolivos.database.ConexionBD;
import com.uniajc.hotellosolivos.model.Usuario;
import com.uniajc.hotellosolivos.util.SeguridadUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    private UsuarioDAO usuarioDAO;
    private String nombreUsuarioPrueba;

    @BeforeEach
    void preparar() {
        usuarioDAO = new UsuarioDAO();
        nombreUsuarioPrueba = "test_" + System.currentTimeMillis();
    }

    @AfterEach
    void limpiar() {
        String sql = "DELETE FROM usuarios WHERE usuario = ?";

        try (PreparedStatement ps = ConexionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, nombreUsuarioPrueba);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Usuario crearUsuarioDePrueba(String rol, String estado) {
        Usuario u = new Usuario();
        u.setNombre("Usuario de Prueba");
        u.setUsuario(nombreUsuarioPrueba);
        u.setContrasena(SeguridadUtil.hashear("123456"));
        u.setRol(rol);
        u.setEstado(estado);
        return u;
    }


    @Test
    void encontrarUsuarioAdmin() {
        Usuario usuario = usuarioDAO.buscarPorUsuario("admin");

        assertNotNull(usuario);
        assertEquals("admin", usuario.getUsuario());
    }

    @Test
    void retornarNullCuandoUsuarioNoExiste() {
        assertNull(usuarioDAO.buscarPorUsuario("usuarioInexistente"));
    }

    @Test
    void validarUsuarioActivo() {
        Usuario usuario = usuarioDAO.buscarPorUsuario("admin");

        assertEquals("Activo", usuario.getEstado());
    }

    @Test
    void tenerRolValido() {
        Usuario usuario = usuarioDAO.buscarPorUsuario("admin");

        assertNotNull(usuario.getRol());
    }

    @Test
    void iniciarSesionConCredencialesValidas() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        Usuario resultado = usuarioDAO.iniciarSesion(nombreUsuarioPrueba, "123456");

        assertNotNull(resultado);
        assertEquals(nombreUsuarioPrueba, resultado.getUsuario());
    }

    @Test
    void iniciarSesionConContrasenaIncorrecta() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Activo"));

        assertNull(usuarioDAO.iniciarSesion(nombreUsuarioPrueba, "contrasenaMala"));
    }

    @Test
    void iniciarSesionConUsuarioInexistente() {
        assertNull(usuarioDAO.iniciarSesion("no_existe_" + System.currentTimeMillis(), "123456"));
    }

    @Test
    void iniciarSesionConUsuarioInactivo() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Empleado", "Inactivo"));

        assertNull(usuarioDAO.iniciarSesion(nombreUsuarioPrueba, "123456"));
    }

    @Test
    void retornarElRolDelUsuarioAlIniciarSesion() {
        usuarioDAO.guardarUsuario(crearUsuarioDePrueba("Administrador", "Activo"));

        Usuario resultado = usuarioDAO.iniciarSesion(nombreUsuarioPrueba, "123456");

        assertEquals("Administrador", resultado.getRol());
    }

}


