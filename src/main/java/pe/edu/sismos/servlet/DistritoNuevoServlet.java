package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.model.Provincia;
import pe.edu.sismos.repository.DistritoRepository;
import pe.edu.sismos.repository.PersistenciaException;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "DistritoNuevoServlet", urlPatterns = "/ubicaciones/distritos/nuevo")
public class DistritoNuevoServlet extends HttpServlet {
    private final DistritoRepository distritoRepository = new DistritoRepository();
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        request.setAttribute("nombreIngresado", nombre);
        request.setAttribute("provinciaIdIngresada", provinciaId);
        try {
            Provincia provincia = provinciaRepository.buscarPorId(provinciaId)
                    .filter(p -> "ACTIVO".equals(p.getEstado()))
                    .orElse(null);
            if (nombre.isBlank() || provincia == null) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                DistritoListarServlet.mostrar(request, response);
                return;
            }
            if (distritoRepository.buscarPorNombreYProvincia(nombre, provinciaId).isPresent()) {
                request.setAttribute("error", "El distrito ya existe en esta provincia");
                DistritoListarServlet.mostrar(request, response);
                return;
            }
            distritoRepository.guardar(new Distrito(null, nombre, provincia, "ACTIVO"));
            response.sendRedirect(request.getContextPath() + "/ubicaciones/distritos?resultado=registrado&nombre=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8));
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            DistritoListarServlet.mostrar(request, response);
        }
    }
}
