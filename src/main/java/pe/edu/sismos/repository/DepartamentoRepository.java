package pe.edu.sismos.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pe.edu.sismos.config.JpaUtil;
import pe.edu.sismos.model.Departamento;

import java.util.List;
import java.util.Optional;

public class DepartamentoRepository {
    public List<Departamento> listar() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT d FROM Departamento d ORDER BY d.nombre", Departamento.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron listar los departamentos.", ex);
        } finally {
            em.close();
        }
    }

    public List<Departamento> listarActivos() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT d FROM Departamento d WHERE d.estado = 'ACTIVO' ORDER BY d.nombre", Departamento.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron listar los departamentos activos.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Departamento> buscarPorId(long id) {
        return buscarUnico("SELECT d FROM Departamento d WHERE d.id = :valor", id);
    }

    public Optional<Departamento> buscarPorNombre(String nombre) {
        return buscarUnico("SELECT d FROM Departamento d WHERE d.nombre = :valor", nombre);
    }

    public Departamento guardar(Departamento departamento) {
        EntityManager em = JpaUtil.crearEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Departamento resultado = departamento.getId() == null ? departamento : em.merge(departamento);
            if (departamento.getId() == null) em.persist(departamento);
            tx.commit();
            return resultado;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("No se pudo guardar el departamento. El nombre debe ser único.", ex);
        } finally {
            em.close();
        }
    }

    private Optional<Departamento> buscarUnico(String jpql, Object valor) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(jpql, Departamento.class).setParameter("valor", valor).setMaxResults(1).getResultList().stream().findFirst();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudo buscar el departamento.", ex);
        } finally {
            em.close();
        }
    }
}
