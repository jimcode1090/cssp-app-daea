package pe.edu.sismos.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import pe.edu.sismos.repository.PersistenciaException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** Crea EntityManager para la unidad de persistencia JPA. */
public final class JpaUtil {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = crearFactory();

    private JpaUtil() { }

    public static EntityManager crearEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static void cerrar() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }

    private static EntityManagerFactory crearFactory() {
        Properties propiedades = cargarPropiedades();
        Map<String, String> configuracion = new HashMap<>();
        configuracion.put("jakarta.persistence.jdbc.driver", valor(propiedades, "driver"));
        configuracion.put("jakarta.persistence.jdbc.url", valor(propiedades, "url"));
        configuracion.put("jakarta.persistence.jdbc.user", valor(propiedades, "usuario"));
        configuracion.put("jakarta.persistence.jdbc.password", valor(propiedades, "contrasena"));
        try {
            return Persistence.createEntityManagerFactory("CsspSismosPU", configuracion);
        } catch (RuntimeException ex) {
            throw new PersistenciaException("No se pudo iniciar JPA. Verifique MySQL y db.properties.", ex);
        }
    }

    private static Properties cargarPropiedades() {
        try (InputStream entrada = JpaUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (entrada == null) throw new PersistenciaException("No se encontró db.properties.", null);
            Properties propiedades = new Properties();
            propiedades.load(entrada);
            return propiedades;
        } catch (IOException ex) {
            throw new PersistenciaException("No se pudo leer db.properties.", ex);
        }
    }

    private static String valor(Properties propiedades, String clave) {
        String valorSistema = System.getProperty("cssp.db." + clave);
        return valorSistema != null && !valorSistema.isBlank() ? valorSistema : propiedades.getProperty(clave, "");
    }
}
