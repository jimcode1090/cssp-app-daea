package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.repository.EstacionRepository;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;

@WebServlet(name = "EstacionDetalleServlet", urlPatterns = "/estaciones/detalle")
public class EstacionDetalleServlet extends HttpServlet {
    private final EstacionRepository estacionRepository = new EstacionRepository();
    private final SismoRepository sismoRepository = new SismoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Estacion estacion = estacionRepository.buscarPorCodigo(DepartamentoNuevoServlet.limpiar(request.getParameter("codigo"))).orElse(null);
        if (estacion == null) {
            response.sendRedirect(request.getContextPath() + "/estaciones");
            return;
        }
        request.setAttribute("estacion", estacion);
        request.setAttribute("sismos", sismoRepository.listarPorEstacion(estacion.getCodigo()));
        request.getRequestDispatcher("/WEB-INF/views/estaciones/detalle.jsp").forward(request, response);
    }
}
