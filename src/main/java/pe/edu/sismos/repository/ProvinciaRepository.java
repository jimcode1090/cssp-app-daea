package pe.edu.sismos.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pe.edu.sismos.config.JpaUtil;
import pe.edu.sismos.model.Departamento;
import pe.edu.sismos.model.Provincia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProvinciaRepository {
    public List<Provincia> listar() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT p FROM Provincia p JOIN FETCH p.departamento ORDER BY p.departamento.nombre, p.nombre", Provincia.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos cargar las provincias.", ex);
        } finally {
            em.close();
        }
    }

    public List<Provincia> listarActivas() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT p FROM Provincia p JOIN FETCH p.departamento WHERE p.estado = 'ACTIVO' ORDER BY p.departamento.nombre, p.nombre", Provincia.class).getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos cargar las provincias activas.", ex);
        } finally {
            em.close();
        }
    }

    public List<Provincia> listarActivasPorDepartamento(long departamentoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT p FROM Provincia p WHERE p.departamento.id = :departamentoId AND p.estado = 'ACTIVO' ORDER BY p.nombre", Provincia.class)
                    .setParameter("departamentoId", departamentoId)
                    .getResultList();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos cargar las provincias del departamento.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Provincia> buscarPorId(long id) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return Optional.ofNullable(em.find(Provincia.class, id));
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos encontrar la provincia.", ex);
        } finally {
            em.close();
        }
    }

    public Optional<Provincia> buscarPorNombreYDepartamento(String nombre, long departamentoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT p FROM Provincia p WHERE p.nombre = :nombre AND p.departamento.id = :departamentoId", Provincia.class)
                    .setParameter("nombre", nombre)
                    .setParameter("departamentoId", departamentoId)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos comprobar el nombre de la provincia.", ex);
        } finally {
            em.close();
        }
    }

    public long contarActivasDeDepartamento(long departamentoId) {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Provincia p WHERE p.departamento.id = :departamentoId AND p.estado = 'ACTIVO'", Long.class)
                    .setParameter("departamentoId", departamentoId)
                    .getSingleResult();
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos revisar las provincias del departamento.", ex);
        } finally {
            em.close();
        }
    }

    public Map<Long, Long> contarActivasPorDepartamento() {
        EntityManager em = JpaUtil.crearEntityManager();
        try {
            Map<Long, Long> conteo = new HashMap<>();
            List<Object[]> filas = em.createQuery("SELECT p.departamento.id, COUNT(p) FROM Provincia p WHERE p.estado = 'ACTIVO' GROUP BY p.departamento.id", Object[].class).getResultList();
            for (Object[] fila : filas) {
                conteo.put((Long) fila[0], (Long) fila[1]);
            }
            return conteo;
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No pudimos revisar las provincias por departamento.", ex);
        } finally {
            em.close();
        }
    }

    public Provincia guardar(Provincia provincia) {
        EntityManager em = JpaUtil.crearEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Provincia guardada;
            if (provincia.getId() == null) {
                provincia.setDepartamento(em.getReference(Departamento.class, provincia.getDepartamento().getId()));
                em.persist(provincia);
                guardada = provincia;
            } else {
                provincia.setDepartamento(em.getReference(Departamento.class, provincia.getDepartamento().getId()));
                guardada = em.merge(provincia);
            }
            tx.commit();
            return guardada;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw new PersistenciaException("No pudimos guardar la provincia. Revisa que el nombre no esté repetido en este departamento.", ex);
        } finally {
            em.close();
        }
    }
}
