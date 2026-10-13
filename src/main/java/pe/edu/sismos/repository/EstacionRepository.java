package pe.edu.sismos.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pe.edu.sismos.config.JpaUtil;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.model.Provincia;

import java.util.List;
import java.util.Optional;

public class EstacionRepository {
    public List<Estacion> listar() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT e FROM Estacion e JOIN FETCH e.provincia p JOIN FETCH p.departamento ORDER BY e.codigo", Estacion.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron listar las estaciones.", ex);
        } finally {
            em.close();
        }
    }

    public List<Estacion> listarActivas() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT e FROM Estacion e JOIN FETCH e.provincia p JOIN FETCH p.departamento WHERE e.estado = 'ACTIVO' ORDER BY e.codigo", Estacion.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron listar las estaciones activas.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Estacion> buscarPorCodigo(String codigo) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT e FROM Estacion e JOIN FETCH e.provincia p JOIN FETCH p.departamento WHERE e.codigo = :codigo", Estacion.class)
                    .setParameter("codigo", codigo)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudo buscar la estación.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Estacion> buscarPorId(long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return Optional.ofNullable(em.find(Estacion.class, id));
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudo buscar la estación.", ex);
        } finally {
            em.close();
        }
    }

    public long contarActivasDeProvincia(long provinciaId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(e) FROM Estacion e WHERE e.provincia.id = :provinciaId AND e.estado = 'ACTIVO'", Long.class)
                    .setParameter("provinciaId", provinciaId)
                    .getSingleResult();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron contar las estaciones de la provincia.", ex);
        } finally {
            em.close();
        }
    }

    public Estacion guardar(Estacion estacion) {
        EntityManager em = JpaUtil.crearEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Estacion guardada;
            if (estacion.getId() == null) {
                estacion.setProvincia(em.getReference(Provincia.class, estacion.getProvincia().getId()));
                em.persist(estacion);
                guardada = estacion;
            } else {
                estacion.setProvincia(em.getReference(Provincia.class, estacion.getProvincia().getId()));
                guardada = em.merge(estacion);
            }
            tx.commit();
            return guardada;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("No se pudo guardar la estación. El código debe ser único.", ex);
        } finally {
            em.close();
        }
    }
}
