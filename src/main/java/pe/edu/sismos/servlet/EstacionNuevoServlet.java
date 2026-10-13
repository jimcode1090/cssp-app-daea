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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

@WebServlet(name = "EstacionNuevoServlet", urlPatterns = "/estaciones/nuevo")
public class EstacionNuevoServlet extends HttpServlet {
    static final Set<String> ESTADOS_VALIDOS = Set.of("ACTIVO", "INACTIVO");
    private final EstacionRepository estacionRepository = new EstacionRepository();
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrar(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String codigo = DepartamentoNuevoServlet.limpiar(request.getParameter("codigo"));
        String nombre = DepartamentoNuevoServlet.limpiar(request.getParameter("nombre"));
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        String latitud = DepartamentoNuevoServlet.limpiar(request.getParameter("latitud"));
        String longitud = DepartamentoNuevoServlet.limpiar(request.getParameter("longitud"));
        String fechaInstalacion = DepartamentoNuevoServlet.limpiar(request.getParameter("fechaInstalacion"));
        String estado = DepartamentoNuevoServlet.limpiar(request.getParameter("estado"));
        conservarFormulario(request, codigo, nombre, provinciaId, latitud, longitud, fechaInstalacion, estado);
        try {
            Provincia provincia = provinciaRepository.buscarPorId(provinciaId)
                    .filter(p -> "ACTIVO".equals(p.getEstado()))
                    .orElse(null);
            if (codigo.isBlank()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }
            if (estacionRepository.buscarPorCodigo(codigo).isPresent()) {
                request.setAttribute("error", "Ya existe una estación con el código " + codigo);
                mostrar(request, response);
                return;
            }
            Estacion estacion = construir(null, codigo, nombre, provincia, latitud, longitud, fechaInstalacion, estado);
            estacionRepository.guardar(estacion);
            response.sendRedirect(request.getContextPath() + "/estaciones?resultado=registrada&codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrar(request, response);
        }
    }

    static Estacion construir(Long id, String codigo, String nombre, Provincia provincia, String latitudTexto,
                              String longitudTexto, String fechaTexto, String estado) {
        if (nombre.isBlank() || provincia == null || latitudTexto.isBlank() || longitudTexto.isBlank()
                || fechaTexto.isBlank() || estado.isBlank() || !ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        double latitud;
        double longitud;
        LocalDate fechaInstalacion;
        try {
            latitud = Double.parseDouble(latitudTexto);
            longitud = Double.parseDouble(longitudTexto);
            fechaInstalacion = LocalDate.parse(fechaTexto);
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new IllegalArgumentException("Revisa los números y la fecha: alguno no tiene un formato válido.");
        }
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
        }
        return new Estacion(id, codigo, nombre, provincia, latitud, longitud, fechaInstalacion, estado);
    }

    static void conservarFormulario(HttpServletRequest request, String codigo, String nombre, long provinciaId,
                                    String latitud, String longitud, String fechaInstalacion, String estado) {
        request.setAttribute("codigoIngresado", codigo);
        request.setAttribute("nombreIngresado", nombre);
        request.setAttribute("provinciaIdIngresada", provinciaId);
        request.setAttribute("latitudIngresada", latitud);
        request.setAttribute("longitudIngresada", longitud);
        request.setAttribute("fechaInstalacionIngresada", fechaInstalacion);
        request.setAttribute("estadoIngresado", estado);
    }

    private void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("provincias", provinciaRepository.listarActivas());
        request.getRequestDispatcher("/WEB-INF/views/estaciones/formulario.jsp").forward(request, response);
    }
}
