package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Producto;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductoRepository extends BaseRepository<Producto> {

    public ProductoRepository() {
        super(Producto.class);
    }


    // Consulta JPQL que obtiene todos los productos activos de una categoría
    // filtrando por p.categoria.id = :categoriaId y p.eliminado = false.
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        var em = emf.createEntityManager();

        try {
            String jpql = "SELECT p FROM Producto p " +
                    "WHERE p.categoria.id = :categoriaId " +
                    "AND p.eliminado = false";

            return em.createQuery(jpql, Producto.class)
                    .setParameter("categoriaId", categoriaId)
                    .getResultList();

        } finally {
            em.close();
        }
    }

    public List<Producto> buscarPorNombre(String nombre) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Producto p WHERE p.nombre = LOWER(:nombre)",
                            Producto.class
                    ).setParameter("nombre", nombre)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
