package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Usuario;

import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario> {
    public UsuarioRepository() {
        super(Usuario.class);
    }
    // Consulta JPQL: busca un usuario activo por su mail
    public Optional<Usuario> buscarPorMail(String mail) {
        var em = emf.createEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.mail = :mail AND u.eliminado = false";
            var q = em.createQuery(jpql, Usuario.class);
            q.setParameter("mail", mail);
            var res = q.getResultList();
            return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
        } finally {
            em.close();
        }
    }
}
