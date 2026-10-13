package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.model.Provincia;
import pe.edu.sismos.repository.EstacionRepository;
import pe.edu.sismos.repository.PersistenciaException;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "EstacionEditarServlet", urlPatterns = "/estaciones/editar")
public class EstacionEditarServlet extends HttpServlet {
    private final EstacionRepository estacionRepository = new EstacionRepository();
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Estacion estacion = estacionRepository.buscarPorCodigo(DepartamentoNuevoServlet.limpiar(request.getParameter("codigo"))).orElse(null);
        if (estacion == null) {
            response.sendRedirect(request.getContextPath() + "/estaciones");
            return;
        }
        EstacionNuevoServlet.conservarFormulario(request, estacion.getCodigo(), estacion.getNombre(),
                estacion.getProvincia().getId(), String.valueOf(estacion.getLatitud()),
                String.valueOf(estacion.getLongitud()), estacion.getFechaInstalacion().toString(), estacion.getEstado());
        mostrar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String codigo = DepartamentoNuevoServlet.limpiar(request.getParameter("codigo"));
        Estacion existente = estacionRepository.buscarPorCodigo(codigo).orElse(null);
        if (existente == null) {
            response.sendRedirect(request.getContextPath() + "/estaciones");
            return;
        }
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        String latitud = DepartamentoNuevoServlet.limpiar(request.getParameter("latitud"));
        String longitud = DepartamentoNuevoServlet.limpiar(request.getParameter("longitud"));
        String fechaInstalacion = DepartamentoNuevoServlet.limpiar(request.getParameter("fechaInstalacion"));
        String estado = DepartamentoNuevoServlet.limpiar(request.getParameter("estado"));
        EstacionNuevoServlet.conservarFormulario(request, codigo, nombre, provinciaId, latitud, longitud, fechaInstalacion, estado);
        try {
            Provincia provincia = provinciaRepository.buscarPorId(provinciaId)
                    .filter(p -> "ACTIVO".equals(p.getEstado()))
                    .orElse(null);
            Estacion estacion = EstacionNuevoServlet.construir(existente.getId(), codigo, nombre, provincia, latitud, longitud, fechaInstalacion, estado);
            estacionRepository.guardar(estacion);
            response.sendRedirect(request.getContextPath() + "/estaciones?resultado=actualizada&codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        }
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("modoEdicion", true);
        request.setAttribute("provincias", provinciaRepository.listarActivas());
        request.getRequestDispatcher("/WEB-INF/views/estaciones/formulario.jsp").forward(request, response);
    }
}
