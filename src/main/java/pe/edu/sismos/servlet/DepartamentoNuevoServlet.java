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

// US-19: Agregar un departamento.
@WebServlet(name = "DepartamentoNuevoServlet", urlPatterns = "/ubicaciones/departamentos/nuevo")
public class DepartamentoNuevoServlet extends HttpServlet {
    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String nombre = limpiar(request.getParameter("nombre"));
        request.setAttribute("nombreIngresado", nombre);
        try {
            if (nombre.isBlank()) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                DepartamentoListarServlet.mostrar(request, response);
                return;
            }
            if (departamentoRepository.buscarPorNombre(nombre).isPresent()) {
                request.setAttribute("error", "El departamento ya existe");
                DepartamentoListarServlet.mostrar(request, response);
                return;
            }
            departamentoRepository.guardar(new Departamento(null, nombre, "ACTIVO"));
            response.sendRedirect(request.getContextPath() + "/ubicaciones/departamentos");
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            DepartamentoListarServlet.mostrar(request, response);
        }
    }

    static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
