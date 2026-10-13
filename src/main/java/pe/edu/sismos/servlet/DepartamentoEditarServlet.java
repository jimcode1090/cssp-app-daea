package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Departamento;
import pe.edu.sismos.repository.DepartamentoRepository;
import pe.edu.sismos.repository.PersistenciaException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet(name = "DepartamentoEditarServlet", urlPatterns = "/ubicaciones/departamentos/editar")
public class DepartamentoEditarServlet extends HttpServlet {
    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Departamento departamento = departamentoRepository.buscarPorId(leerId(request.getParameter("id"))).orElse(null);
        if (departamento == null) {
            response.sendRedirect(request.getContextPath() + "/ubicaciones/departamentos");
            return;
        }
        request.setAttribute("departamento", departamento);
        mostrar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        long id = leerId(request.getParameter("id"));
        Departamento departamento = departamentoRepository.buscarPorId(id).orElse(null);
        if (departamento == null) {
            response.sendRedirect(request.getContextPath() + "/ubicaciones/departamentos");
            return;
        }
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        request.setAttribute("departamento", new Departamento(id, nombre, departamento.getEstado()));
        try {
            if (nombre.isBlank()) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                mostrar(request, response);
                return;
            }
            Optional<Departamento> conNombre = departamentoRepository.buscarPorNombre(nombre);
            if (conNombre.isPresent() && !conNombre.get().getId().equals(id)) {
                request.setAttribute("error", "El departamento ya existe");
                mostrar(request, response);
                return;
            }
            departamento.setNombre(nombre);
            departamentoRepository.guardar(departamento);
            response.sendRedirect(request.getContextPath() + "/ubicaciones/departamentos");
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        }
    }

    static long leerId(String valor) {
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/departamento-editar.jsp").forward(request, response);
    }
}
