package pe.edu.sismos.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pe.edu.sismos.repository.SismoRepository;

@WebListener
public class AplicacionListener implements ServletContextListener {

    public static final String REPOSITORIO_SISMOS = "repositorioSismos";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(
                REPOSITORIO_SISMOS,
                new SismoRepository()
        );
    }
}
