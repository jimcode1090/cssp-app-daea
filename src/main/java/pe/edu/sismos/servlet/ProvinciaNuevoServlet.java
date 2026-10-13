package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Departamento;
import pe.edu.sismos.model.Provincia;
import pe.edu.sismos.repository.DepartamentoRepository;
import pe.edu.sismos.repository.PersistenciaException;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "ProvinciaNuevoServlet", urlPatterns = "/ubicaciones/provincias/nuevo")
public class ProvinciaNuevoServlet extends HttpServlet {
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();
    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        long departamentoId = DepartamentoEditarServlet.leerId(request.getParameter("departamentoId"));
        request.setAttribute("nombreIngresado", nombre);
        request.setAttribute("departamentoIdIngresado", departamentoId);
        try {
            Departamento departamento = departamentoRepository.buscarPorId(departamentoId)
                    .filter(d -> "ACTIVO".equals(d.getEstado()))
                    .orElse(null);
            if (nombre.isBlank() || departamento == null) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                ProvinciaListarServlet.mostrar(request, response);
                return;
            }
            if (provinciaRepository.buscarPorNombreYDepartamento(nombre, departamentoId).isPresent()) {
                request.setAttribute("error", "La provincia ya existe en este departamento");
                ProvinciaListarServlet.mostrar(request, response);
                return;
            }
            provinciaRepository.guardar(new Provincia(null, nombre, departamento, "ACTIVO"));
            response.sendRedirect(request.getContextPath() + "/ubicaciones/provincias");
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            ProvinciaListarServlet.mostrar(request, response);
        }
    }
}
