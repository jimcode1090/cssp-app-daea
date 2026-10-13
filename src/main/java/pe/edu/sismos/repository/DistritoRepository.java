package pe.edu.sismos.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pe.edu.sismos.config.JpaUtil;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.model.Provincia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DistritoRepository {
    public List<Distrito> listar() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT d FROM Distrito d JOIN FETCH d.provincia p JOIN FETCH p.departamento ORDER BY p.departamento.nombre, p.nombre, d.nombre", Distrito.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron listar los distritos.", ex);
        } finally {
            em.close();
        }
    }

    public List<Distrito> listarActivosPorProvincia(long provinciaId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT d FROM Distrito d WHERE d.provincia.id = :provinciaId AND d.estado = 'ACTIVO' ORDER BY d.nombre", Distrito.class)
                    .setParameter("provinciaId", provinciaId)
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron listar los distritos de la provincia.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Distrito> buscarPorId(long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return Optional.ofNullable(em.find(Distrito.class, id));
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudo buscar el distrito.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Distrito> buscarPorNombreYProvincia(String nombre, long provinciaId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT d FROM Distrito d WHERE d.nombre = :nombre AND d.provincia.id = :provinciaId", Distrito.class)
                    .setParameter("nombre", nombre)
                    .setParameter("provinciaId", provinciaId)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudo validar el nombre del distrito.", ex);
        } finally {
            em.close();
        }
    }

    public long contarActivosDeProvincia(long provinciaId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(d) FROM Distrito d WHERE d.provincia.id = :provinciaId AND d.estado = 'ACTIVO'", Long.class)
                    .setParameter("provinciaId", provinciaId)
                    .getSingleResult();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron contar los distritos de la provincia.", ex);
        } finally {
            em.close();
        }
    }

    public Map<Long, Long> contarActivosPorProvincia() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            Map<Long, Long> conteo = new HashMap<>();
            List<Object[]> filas = em.createQuery("SELECT d.provincia.id, COUNT(d) FROM Distrito d WHERE d.estado = 'ACTIVO' GROUP BY d.provincia.id", Object[].class).getResultList();
            for (Object[] fila : filas) {
                conteo.put((Long) fila[0], (Long) fila[1]);
            }
            return conteo;
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudieron contar los distritos por provincia.", ex);
        } finally {
            em.close();
        }
    }

    public Distrito guardar(Distrito distrito) {
        EntityManager em = JpaUtil.crearEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Distrito guardado;
            if (distrito.getId() == null) {
                distrito.setProvincia(em.getReference(Provincia.class, distrito.getProvincia().getId()));
                em.persist(distrito);
                guardado = distrito;
            } else {
                distrito.setProvincia(em.getReference(Provincia.class, distrito.getProvincia().getId()));
                guardado = em.merge(distrito);
            }
            tx.commit();
            return guardado;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("No se pudo guardar el distrito. El nombre debe ser único dentro de la provincia.", ex);
        } finally {
            em.close();
        }
    }
}
