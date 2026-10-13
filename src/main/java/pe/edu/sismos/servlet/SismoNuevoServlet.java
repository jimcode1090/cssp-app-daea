package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.model.Sismo;
import pe.edu.sismos.repository.DepartamentoRepository;
import pe.edu.sismos.repository.DistritoRepository;
import pe.edu.sismos.repository.EstacionRepository;
import pe.edu.sismos.repository.PersistenciaException;
import pe.edu.sismos.repository.ProvinciaRepository;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Set;

// US-01: Registrar un nuevo sismo.
@WebServlet(name = "SismoNuevoServlet", urlPatterns = "/sismos/nuevo")
public class SismoNuevoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Usado también por SismoEditarServlet (US-41): valores de estado permitidos.
    static final Set<String> ESTADOS_VALIDOS =
            Set.of("REGISTRADO", "EN_EVALUACION", "EN_SEGUIMIENTO", "CERRADO");

    private final SismoRepository sismoRepository = new SismoRepository();
    private final EstacionRepository estacionRepository = new EstacionRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String codigo = limpiar(request.getParameter("codigo"));
        String fechaHoraTexto = limpiar(request.getParameter("fechaHora"));
        String magnitudTexto = limpiar(request.getParameter("magnitud"));
        String profundidadTexto = limpiar(request.getParameter("profundidad"));
        String latitudTexto = limpiar(request.getParameter("latitud"));
        String longitudTexto = limpiar(request.getParameter("longitud"));
        long departamentoId = DepartamentoEditarServlet.leerId(request.getParameter("departamentoId"));
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        long distritoId = DepartamentoEditarServlet.leerId(request.getParameter("distritoId"));
        long estacionId = DepartamentoEditarServlet.leerId(request.getParameter("estacionId"));
        String referencia = limpiar(request.getParameter("referencia"));
        String estado = limpiar(request.getParameter("estado"));

        conservarFormulario(request, codigo, fechaHoraTexto, magnitudTexto, profundidadTexto,
                latitudTexto, longitudTexto, departamentoId, provinciaId, distritoId, estacionId, referencia, estado);

        try {
            Distrito distrito = resolverDistrito(departamentoId, provinciaId, distritoId);
            Estacion estacion = estacionRepository.buscarPorId(estacionId).orElse(null);

            // US-02 y US-41: se validan todos los campos antes de guardar.
            Sismo sismo = construir(null, codigo, fechaHoraTexto, magnitudTexto, profundidadTexto,
                    latitudTexto, longitudTexto, referencia, estado, distrito, estacion);

            // US-31: el código no puede repetirse.
            if (sismoRepository.existeCodigo(codigo)) {
                request.setAttribute("error", "Ya existe un sismo con el código " + codigo);
                mostrarFormulario(request, response);
                return;
            }

            sismoRepository.guardar(sismo);

            // Redirect inicia una nueva petición GET y evita repetir el POST al recargar.
            response.sendRedirect(request.getContextPath()
                    + "/sismos/detalle?codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8)
                    + "&creado=1");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrarFormulario(request, response);
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrarFormulario(request, response);
        }
    }

    // Usado también por SismoEditarServlet: valida los campos y arma el sismo, o lanza el mensaje de error.
    static Sismo construir(Long id, String codigo, String fechaHoraTexto, String magnitudTexto,
                           String profundidadTexto, String latitudTexto, String longitudTexto,
                           String referencia, String estado, Distrito distrito, Estacion estacion) {

        // US-02: ningún campo puede quedar vacío.
        if (codigo.isBlank() || fechaHoraTexto.isBlank() || magnitudTexto.isBlank()
                || profundidadTexto.isBlank() || latitudTexto.isBlank() || longitudTexto.isBlank()
                || referencia.isBlank() || estado.isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        // US-28: la ubicación debe estar completa y ser coherente (departamento, provincia y distrito activos).
        if (distrito == null) {
            throw new IllegalArgumentException("Seleccione una ubicación válida");
        }

        // US-17: la estación debe elegirse y estar activa.
        if (estacion == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        if (!"ACTIVO".equals(estacion.getEstado())) {
            throw new IllegalArgumentException("La estación seleccionada está inactiva");
        }

        // US-41: magnitud, profundidad y estado dentro de los valores permitidos.
        double magnitud;
        try {
            magnitud = Double.parseDouble(magnitudTexto);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La magnitud debe ser un número mayor que cero");
        }
        if (!(magnitud > 0)) {
            throw new IllegalArgumentException("La magnitud debe ser un número mayor que cero");
        }
        double profundidad;
        try {
            profundidad = Double.parseDouble(profundidadTexto);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La profundidad debe ser un número mayor o igual que cero");
        }
        if (!(profundidad >= 0)) {
            throw new IllegalArgumentException("La profundidad debe ser un número mayor o igual que cero");
        }
        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException("El estado seleccionado no es válido");
        }

        LocalDateTime fechaHora;
        double latitud;
        double longitud;
        try {
            fechaHora = LocalDateTime.parse(fechaHoraTexto);
            latitud = Double.parseDouble(latitudTexto);
            longitud = Double.parseDouble(longitudTexto);
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new IllegalArgumentException("Uno o más campos numéricos o de fecha tienen un formato incorrecto.");
        }

        // US-03 y US-04: coordenadas dentro de rango y fecha que no sea futura.
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90");
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180");
        }
        if (fechaHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del sismo no puede ser futura");
        }

        return new Sismo(id, codigo, fechaHora, magnitud, profundidad, latitud, longitud, referencia,
                estado, "ACTIVO", distrito, estacion);
    }

    // Usado también por SismoEditarServlet: el distrito solo es válido si pertenece a la provincia y al departamento elegidos.
    static Distrito resolverDistrito(long departamentoId, long provinciaId, long distritoId) {
        return new DistritoRepository().buscarPorId(distritoId)
                .filter(d -> "ACTIVO".equals(d.getEstado())
                        && d.getProvincia().getId() == provinciaId
                        && "ACTIVO".equals(d.getProvincia().getEstado())
                        && d.getProvincia().getDepartamento().getId() == departamentoId
                        && "ACTIVO".equals(d.getProvincia().getDepartamento().getEstado()))
                .orElse(null);
    }

    // Usado también por SismoEditarServlet: carga las listas de ubicación y las estaciones activas.
    static void cargarListas(HttpServletRequest request, long departamentoId, long provinciaId) {
        request.setAttribute("departamentos", new DepartamentoRepository().listarActivos());
        request.setAttribute("provincias", new ProvinciaRepository().listarActivasPorDepartamento(departamentoId));
        request.setAttribute("distritos", new DistritoRepository().listarActivosPorProvincia(provinciaId));
        request.setAttribute("estaciones", new EstacionRepository().listarActivas());
    }

    static void conservarFormulario(HttpServletRequest request, String codigo, String fechaHora,
                                    String magnitud, String profundidad, String latitud, String longitud,
                                    long departamentoId, long provinciaId, long distritoId, long estacionId,
                                    String referencia, String estado) {
        request.setAttribute("codigoIngresado", codigo);
        request.setAttribute("fechaHoraIngresada", fechaHora);
        request.setAttribute("magnitudIngresada", magnitud);
        request.setAttribute("profundidadIngresada", profundidad);
        request.setAttribute("latitudIngresada", latitud);
        request.setAttribute("longitudIngresada", longitud);
        request.setAttribute("departamentoIdIngresado", departamentoId);
        request.setAttribute("provinciaIdIngresada", provinciaId);
        request.setAttribute("distritoIdIngresado", distritoId);
        request.setAttribute("estacionIdIngresada", estacionId);
        request.setAttribute("referenciaIngresada", referencia);
        request.setAttribute("estadoIngresado", estado);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long departamentoId = request.getAttribute("departamentoIdIngresado") == null
                ? -1 : (long) request.getAttribute("departamentoIdIngresado");
        long provinciaId = request.getAttribute("provinciaIdIngresada") == null
                ? -1 : (long) request.getAttribute("provinciaIdIngresada");
        cargarListas(request, departamentoId, provinciaId);
        request.getRequestDispatcher("/WEB-INF/views/sismos/formulario.jsp")
                .forward(request, response);
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
