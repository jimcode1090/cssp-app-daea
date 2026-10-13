package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Departamento;
import pe.edu.sismos.repository.DepartamentoRepository;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;

// US-19: Desactivar o activar un departamento.
@WebServlet(name = "DepartamentoEstadoServlet", urlPatterns = "/ubicaciones/departamentos/estado")
public class DepartamentoEstadoServlet extends HttpServlet {
    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Departamento departamento = departamentoRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        if (departamento != null) {
            if ("ACTIVO".equals(departamento.getEstado())) {
                if (provinciaRepository.contarActivasDeDepartamento(departamento.getId()) > 0) {
                    request.setAttribute("error", "El departamento tiene provincias asociadas");
                    DepartamentoListarServlet.mostrar(request, response);
                    return;
                }
                departamento.setEstado("INACTIVO");
            } else {
                departamento.setEstado("ACTIVO");
            }
            departamentoRepository.guardar(departamento);
        }
        response.sendRedirect(request.getContextPath() + "/ubicaciones/departamentos");
    }
}
