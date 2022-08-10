package com.github.kokecena.curso.dao;

import com.github.kokecena.curso.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Repository
@Transactional
public class UsuarioDaoImpl implements UsuarioDAO {

    private static final String GET_USUARIOS = "FROM Usuario";
    private static final String VERIFY_USER = "FROM Usuario Where email = :email";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usuario> getUsuarios() {
       return entityManager.createQuery(GET_USUARIOS).getResultList();
    }

    @Override
    public void delete(Long id) {
        Usuario usuario = entityManager.find(Usuario.class,id);
        entityManager.remove(usuario);
    }

    @Override
    public void register(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public Usuario getUserByCredentials(Usuario usuario) {
       List<Usuario> list = entityManager.createQuery(VERIFY_USER)
               .setParameter("email",usuario.getEmail())
               .getResultList();
       if(list.isEmpty()) return null;
       Argon2 arg2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
       String passwordHashed = list.get(0).getPassword();
       if(arg2.verify(passwordHashed,usuario.getPassword().getBytes(StandardCharsets.UTF_8))){
           return list.get(0);
       }
       return null;
    }

}
