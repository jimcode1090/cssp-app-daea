package pe.edu.sismos.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pe.edu.sismos.config.JpaUtil;

/** Libera el EntityManagerFactory cuando se retira la aplicación. */
@WebListener
public class JpaContextListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        JpaUtil.cerrar();
    }
}
