package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.enums.Estado;

import java.util.List;

public class PedidoRepository extends BaseRepository<Pedido>{

    public PedidoRepository() {
        super(Pedido.class);
    }

    // Pedidos activos de un usuario
    public List<Pedido> buscarPorUsuario(Long idUsuario) {
        var em = emf.createEntityManager();
        try {
            String jpql = "SELECT p FROM Pedido p WHERE p.usuario.id = :uid AND p.eliminado = false";
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("uid", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Pedidos activos por estado
    public List<Pedido> buscarPorEstado(Estado estado) {
        var em = emf.createEntityManager();
        try {
            String jpql = "SELECT p FROM Pedido p WHERE p.estado = :estado AND p.eliminado = false";
            return em.createQuery(jpql, Pedido.class)
                    .setParameter("estado", estado)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
