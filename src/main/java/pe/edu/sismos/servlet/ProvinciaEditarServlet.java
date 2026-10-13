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
import java.util.Optional;

// US-09: Editar una provincia.
@WebServlet(name = "ProvinciaEditarServlet", urlPatterns = "/ubicaciones/provincias/editar")
public class ProvinciaEditarServlet extends HttpServlet {
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();
    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Provincia provincia = provinciaRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        if (provincia == null) {
            response.sendRedirect(request.getContextPath() + "/ubicaciones/provincias");
            return;
        }
        request.setAttribute("provincia", provincia);
        mostrar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        long id = DepartamentoEditarServlet.leerId(request.getParameter("id"));
        Provincia provincia = provinciaRepository.buscarPorId(id).orElse(null);
        if (provincia == null) {
            response.sendRedirect(request.getContextPath() + "/ubicaciones/provincias");
            return;
        }
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        long departamentoId = DepartamentoEditarServlet.leerId(request.getParameter("departamentoId"));
        try {
            Departamento departamento = departamentoRepository.buscarPorId(departamentoId)
                    .filter(d -> "ACTIVO".equals(d.getEstado()))
                    .orElse(null);
            request.setAttribute("provincia", new Provincia(id, nombre, departamento != null ? departamento : provincia.getDepartamento(), provincia.getEstado()));
            if (nombre.isBlank() || departamento == null) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                mostrar(request, response);
                return;
            }
            Optional<Provincia> conNombre = provinciaRepository.buscarPorNombreYDepartamento(nombre, departamentoId);
            if (conNombre.isPresent() && !conNombre.get().getId().equals(id)) {
                request.setAttribute("error", "La provincia ya existe en este departamento");
                mostrar(request, response);
                return;
            }
            provincia.setNombre(nombre);
            provincia.setDepartamento(departamento);
            provinciaRepository.guardar(provincia);
            response.sendRedirect(request.getContextPath() + "/ubicaciones/provincias");
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        }
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("departamentos", departamentoRepository.listarActivos());
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/provincia-editar.jsp").forward(request, response);
    }
}
