package com.tp.jpa.repository;

import com.tp.jpa.model.entities.Producto;

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

    //Consulta JPQL que busca producto por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        var em = emf.createEntityManager();
        try {
            String jpql = "SELECT p FROM Producto p " +
                    "WHERE LOWER(p.nombre) = LOWER(:nombre) " +
                    "AND p.eliminado = false";
            return em.createQuery(jpql, Producto.class)
                    .setParameter("nombre", nombre)
                    .getResultList();
        } finally {
            em.close();
        }
    }


}



