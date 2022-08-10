package com.github.kokecena.curso.controllers;

import com.github.kokecena.curso.dao.UsuarioDAO;
import com.github.kokecena.curso.models.Usuario;
import com.github.kokecena.curso.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UsuarioDAO usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String login(@RequestBody Usuario usuario){
        Usuario userLogged = usuarioDao.getUserByCredentials(usuario);
        if(userLogged != null){
            String token = jwtUtil.create(String.valueOf(userLogged.getId()), usuario.getEmail());
            return token;
        }
        return "Failed";
    }

}
