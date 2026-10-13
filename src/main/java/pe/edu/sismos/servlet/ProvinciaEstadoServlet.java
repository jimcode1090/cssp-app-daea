package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Provincia;
import pe.edu.sismos.repository.DistritoRepository;
import pe.edu.sismos.repository.EstacionRepository;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;

@WebServlet(name = "ProvinciaEstadoServlet", urlPatterns = "/ubicaciones/provincias/estado")
public class ProvinciaEstadoServlet extends HttpServlet {
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();
    private final DistritoRepository distritoRepository = new DistritoRepository();
    private final EstacionRepository estacionRepository = new EstacionRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Provincia provincia = provinciaRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        if (provincia != null) {
            if ("ACTIVO".equals(provincia.getEstado())) {
                if (distritoRepository.contarActivosDeProvincia(provincia.getId()) > 0) {
                    request.setAttribute("error", "La provincia tiene distritos asociados");
                    ProvinciaListarServlet.mostrar(request, response);
                    return;
                }
                if (estacionRepository.contarActivasDeProvincia(provincia.getId()) > 0) {
                    request.setAttribute("error", "La provincia tiene estaciones asociadas");
                    ProvinciaListarServlet.mostrar(request, response);
                    return;
                }
                provincia.setEstado("INACTIVO");
            } else {
                provincia.setEstado("ACTIVO");
            }
            provinciaRepository.guardar(provincia);
        }
        response.sendRedirect(request.getContextPath() + "/ubicaciones/provincias");
    }
}
