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
import java.util.Optional;

@WebServlet(name = "DistritoEditarServlet", urlPatterns = "/ubicaciones/distritos/editar")
public class DistritoEditarServlet extends HttpServlet {
    private final DistritoRepository distritoRepository = new DistritoRepository();
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Distrito distrito = distritoRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        if (distrito == null) {
            response.sendRedirect(request.getContextPath() + "/ubicaciones/distritos");
            return;
        }
        request.setAttribute("distrito", distrito);
        mostrar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        long id = DepartamentoEditarServlet.leerId(request.getParameter("id"));
        Distrito distrito = distritoRepository.buscarPorId(id).orElse(null);
        if (distrito == null) {
            response.sendRedirect(request.getContextPath() + "/ubicaciones/distritos");
            return;
        }
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        try {
            Provincia provincia = provinciaRepository.buscarPorId(provinciaId)
                    .filter(p -> "ACTIVO".equals(p.getEstado()))
                    .orElse(null);
            request.setAttribute("distrito", new Distrito(id, nombre, provincia != null ? provincia : distrito.getProvincia(), distrito.getEstado()));
            if (nombre.isBlank() || provincia == null) {
                request.setAttribute("error", "Todos los campos son obligatorios.");
                mostrar(request, response);
                return;
            }
            Optional<Distrito> conNombre = distritoRepository.buscarPorNombreYProvincia(nombre, provinciaId);
            if (conNombre.isPresent() && !conNombre.get().getId().equals(id)) {
                request.setAttribute("error", "El distrito ya existe en esta provincia");
                mostrar(request, response);
                return;
            }
            distrito.setNombre(nombre);
            distrito.setProvincia(provincia);
            distritoRepository.guardar(distrito);
            response.sendRedirect(request.getContextPath() + "/ubicaciones/distritos?resultado=actualizado&nombre=" + URLEncoder.encode(nombre, StandardCharsets.UTF_8));
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        }
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("provincias", provinciaRepository.listarActivas());
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/distrito-editar.jsp").forward(request, response);
    }
}
