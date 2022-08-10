package com.github.kokecena.curso.controllers;

import com.github.kokecena.curso.dao.UsuarioDAO;
import com.github.kokecena.curso.models.Usuario;
import com.github.kokecena.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/usuarios",method = RequestMethod.GET)
    public List<Usuario> getUsuarios(@RequestHeader(value = "Authorization") String token){
        return validToken(token) ? usuarioDao.getUsuarios():null;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario) {
        Argon2 arg2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = arg2.hash(1, 1024, 1, usuario.getPassword().getBytes(StandardCharsets.UTF_8));
        usuario.setPassword(hash);
        usuarioDao.register(usuario);
    }

    @RequestMapping(value = "api/usuarios/{id}",method = RequestMethod.DELETE)
    public void eliminar(@RequestHeader(value = "Authorization") String token,@PathVariable Long id){
       if(!validToken(token)) {
           return;
       }
       usuarioDao.delete(id);
    }

    private boolean validToken(String token){
        String idUser = jwtUtil.getKey(token);
        return idUser != null;
    }
}
