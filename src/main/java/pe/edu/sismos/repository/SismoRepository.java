package pe.edu.sismos.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pe.edu.sismos.config.JpaUtil;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.model.Sismo;

import java.util.List;
import java.util.Optional;

public class SismoRepository {
    private static final String CONSULTA_BASE = "SELECT s FROM Sismo s JOIN FETCH s.distrito d JOIN FETCH d.provincia p JOIN FETCH p.departamento JOIN FETCH s.estacion WHERE s.estadoRegistro = 'ACTIVO'";

    public List<Sismo> listar() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(CONSULTA_BASE + " ORDER BY s.id", Sismo.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos cargar los sismos.", ex);
        } finally {
            em.close();
        }
    }

    public List<Sismo> listarPorEstacion(String codigoEstacion) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(CONSULTA_BASE + " AND s.estacion.codigo = :codigo ORDER BY s.id", Sismo.class)
                    .setParameter("codigo", codigoEstacion)
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos cargar los sismos de la estación.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Sismo> buscarPorCodigo(String codigo) {
        if (codigo == null) {
            return Optional.empty();
        }
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery(CONSULTA_BASE + " AND s.codigo = :codigo", Sismo.class)
                    .setParameter("codigo", codigo.trim())
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos encontrar el sismo.", ex);
        } finally {
            em.close();
        }
    }

    public boolean existeCodigo(String codigo) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(s) FROM Sismo s WHERE s.codigo = :codigo", Long.class)
                    .setParameter("codigo", codigo.trim())
                    .getSingleResult() > 0;
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos comprobar el código del sismo.", ex);
        } finally {
            em.close();
        }
    }

    public long contarActivosDeDistrito(long distritoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(s) FROM Sismo s WHERE s.distrito.id = :distritoId AND s.estadoRegistro = 'ACTIVO'", Long.class)
                    .setParameter("distritoId", distritoId)
                    .getSingleResult();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos revisar los sismos del distrito.", ex);
        } finally {
            em.close();
        }
    }

    public Sismo guardar(Sismo sismo) {
        EntityManager em = JpaUtil.crearEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Sismo guardado;
            sismo.setDistrito(em.getReference(Distrito.class, sismo.getDistrito().getId()));
            sismo.setEstacion(em.getReference(Estacion.class, sismo.getEstacion().getId()));
            if (sismo.getId() == null) {
                em.persist(sismo);
                guardado = sismo;
            } else {
                guardado = em.merge(sismo);
            }
            tx.commit();
            return guardado;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("No pudimos guardar el sismo. Revisa que el código no esté repetido.", ex);
        } finally {
            em.close();
        }
    }

    public boolean eliminar(String codigo) {
        EntityManager em = JpaUtil.crearEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Optional<Sismo> encontrado = em.createQuery("SELECT s FROM Sismo s WHERE s.codigo = :codigo AND s.estadoRegistro = 'ACTIVO'", Sismo.class)
                    .setParameter("codigo", codigo.trim())
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst();
            if (encontrado.isEmpty()) {
                tx.rollback();
                return false;
            }
            encontrado.get().setEstadoRegistro("INACTIVO");
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("No pudimos eliminar el sismo.", ex);
        } finally {
            em.close();
        }
    }
}
