package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Provincia;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;

// US-49: Desactivar o activar una provincia.
@WebServlet(name = "ProvinciaEstadoServlet", urlPatterns = "/ubicaciones/provincias/estado")
public class ProvinciaEstadoServlet extends HttpServlet {
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Provincia provincia = provinciaRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        if (provincia != null) {
            provincia.setEstado("ACTIVO".equals(provincia.getEstado()) ? "INACTIVO" : "ACTIVO");
            provinciaRepository.guardar(provincia);
        }
        response.sendRedirect(request.getContextPath() + "/ubicaciones/provincias");
    }
}
