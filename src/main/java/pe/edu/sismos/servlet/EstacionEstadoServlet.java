package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.repository.EstacionRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "EstacionEstadoServlet", urlPatterns = "/estaciones/estado")
public class EstacionEstadoServlet extends HttpServlet {
    private final EstacionRepository estacionRepository = new EstacionRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Estacion estacion = estacionRepository.buscarPorCodigo(DepartamentoNuevoServlet.limpiar(request.getParameter("codigo"))).orElse(null);
        String resultado = "";
        if (estacion != null) {
            estacion.setEstado("ACTIVO".equals(estacion.getEstado()) ? "INACTIVO" : "ACTIVO");
            estacionRepository.guardar(estacion);
            resultado = "?resultado=" + ("INACTIVO".equals(estacion.getEstado()) ? "desactivada" : "activada") + "&codigo=" + URLEncoder.encode(estacion.getCodigo(), StandardCharsets.UTF_8);
        }
        response.sendRedirect(request.getContextPath() + "/estaciones" + resultado);
    }
}
