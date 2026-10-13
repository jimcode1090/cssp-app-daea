package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.repository.DistritoRepository;

import java.io.IOException;

// US-27: Desactivar o activar un distrito.
@WebServlet(name = "DistritoEstadoServlet", urlPatterns = "/ubicaciones/distritos/estado")
public class DistritoEstadoServlet extends HttpServlet {
    private final DistritoRepository distritoRepository = new DistritoRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Distrito distrito = distritoRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        if (distrito != null) {
            distrito.setEstado("ACTIVO".equals(distrito.getEstado()) ? "INACTIVO" : "ACTIVO");
            distritoRepository.guardar(distrito);
        }
        response.sendRedirect(request.getContextPath() + "/ubicaciones/distritos");
    }
}
