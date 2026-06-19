package com.tp.jpa.repository;
import com.tp.jpa.model.entities.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManagerFactory;


import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Base> {

    protected final Class<T> entityClass;
    protected final EntityManagerFactory emf;

    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    // ===== 1. Guardar (T entity) =====
    public T guardar(T entity) {
        var em = emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al guardar la entidad en BaseRepository", e);
        } finally {
            em.close();
        }
    }

    // ===== 2. buscarPorId(Long id) =====
    public Optional<T> buscarPorId(Long id) {
        var em = emf.createEntityManager();

        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity); //retorna empty si es null

        } finally {
            em.close();
        }
    }

    // ===== 3. listarActivos  =====
    public List<T> listarActivos() {
        var em = emf.createEntityManager();

        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.eliminado = false";
            return em.createQuery(jpql, entityClass).getResultList();

        } finally {
            em.close();
        }
    }

    // ===== 3. listarInactivos =====
    public List<T> listarInactivos() {
        var em = emf.createEntityManager();

        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.eliminado = true";
            return em.createQuery(jpql, entityClass).getResultList();

        } finally {
            em.close();
        }
    }


    // ===== 4. eliminarLogico =====

    public boolean eliminarLogico(Long id) {
        var em = emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            T entity = em.find(entityClass, id);

            if (entity == null) {
                return false; //mo existe -> retorna falso
            }

            tx.begin();
            entity.setEliminado(true);
            em.merge(entity);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar lógicamente la entidad", e);

        } finally {
            em.close();
        }
    }

    // Alta logixa

    public boolean AltaLogica(Long id) {
        var em = emf.createEntityManager();
        var tx = em.getTransaction();

        try {
            T entity = em.find(entityClass, id);

            if (entity == null) {
                return false; //no existe -> retorna falso
            }

            tx.begin();
            entity.setEliminado(false);
            em.merge(entity);
            tx.commit();

            return true;

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error al dar de alta la entidad la entidad", e);

        } finally {
            em.close();
        }
    }


}
