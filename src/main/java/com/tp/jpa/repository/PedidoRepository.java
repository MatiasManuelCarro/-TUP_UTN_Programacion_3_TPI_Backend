package com.tp.jpa.repository;

import com.tp.jpa.model.dtos.DetalleTemporal;
import com.tp.jpa.model.entities.DetallePedido;
import com.tp.jpa.model.entities.Pedido;
import com.tp.jpa.model.entities.Producto;
import com.tp.jpa.model.entities.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Pedido altaPedido(Usuario usuario, FormaPago formaPago, List<DetalleTemporal> detallesTemp) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Crear pedido
            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setFecha(LocalDate.now());
            pedido.setEstado(Estado.PENDIENTE);
            pedido.setFormapago(formaPago);

            // Agregar detalles
            for (DetalleTemporal dt : detallesTemp) {
                Producto prod = em.find(Producto.class, dt.getIdProducto());

                if (prod == null || !prod.isDisponible()) {
                    throw new IllegalArgumentException("Producto inválido o no disponible.");
                }
                if (dt.getCantidad() <= 0 || dt.getCantidad() > prod.getStock()) {
                    throw new IllegalArgumentException("Stock insuficiente para producto: " + prod.getNombre());
                }

                // Crear detalle
                DetallePedido detalle = DetallePedido.builder()
                        .cantidad(dt.getCantidad())
                        .producto(prod)
                        .subtotal(prod.getPrecio() * dt.getCantidad())
                        .pedido(pedido)
                        .build();

                pedido.getDetallePedidos().add(detalle);

                // Reducir stock
                prod.setStock(prod.getStock() - dt.getCantidad());
            }

            // Calcular total
            pedido.calcularTotal();

            // Persistir pedido (cascade persiste detalles)
            em.persist(pedido);

            tx.commit();
            return pedido;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }



}
