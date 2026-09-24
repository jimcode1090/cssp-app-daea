package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.listener.AplicacionListener;
import pe.edu.sismos.model.Sismo;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

// US-21: Editar los datos de un sismo.
// El profesor no mostró en clase esta pantalla (su ejemplo llegaba hasta Detalle), pero
// la sección 5.7 de los requisitos técnicos ya la define, así que sigue el mismo patrón
// (Servlet controla / JSP presenta / repositorio en memoria) de SismoNuevoServlet.
@WebServlet(name = "SismoEditarServlet", urlPatterns = "/sismos/editar")
public class SismoEditarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        SismoRepository repositorio = repositorio(request);
        Optional<Sismo> encontrado = repositorio.buscarPorCodigo(codigo);

        if (encontrado.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("mensajeError", "No existe un sismo con el código solicitado.");
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp")
                    .forward(request, response);
            return;
        }

        // US-21 (CA1): el formulario se carga con los datos actuales y el código en solo lectura.
        Sismo sismo = encontrado.get();
        request.setAttribute("modoEdicion", true);
        request.setAttribute("codigoIngresado", sismo.codigo());
        request.setAttribute("fechaHoraIngresada", sismo.fechaHora().toString());
        request.setAttribute("magnitudIngresada", String.valueOf(sismo.magnitud()));
        request.setAttribute("profundidadIngresada", String.valueOf(sismo.profundidad()));
        request.setAttribute("latitudIngresada", String.valueOf(sismo.latitud()));
        request.setAttribute("longitudIngresada", String.valueOf(sismo.longitud()));
        request.setAttribute("departamentoIngresado", sismo.departamento());
        request.setAttribute("referenciaIngresada", sismo.referencia());
        request.setAttribute("estadoIngresado", sismo.estado());

        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // El código llega oculto desde el formulario y no se vuelve a pedir: no se modifica al editar.
        String codigo = limpiar(request.getParameter("codigo"));
        String fechaHoraTexto = limpiar(request.getParameter("fechaHora"));
        String magnitudTexto = limpiar(request.getParameter("magnitud"));
        String profundidadTexto = limpiar(request.getParameter("profundidad"));
        String latitudTexto = limpiar(request.getParameter("latitud"));
        String longitudTexto = limpiar(request.getParameter("longitud"));
        String departamento = limpiar(request.getParameter("departamento"));
        String referencia = limpiar(request.getParameter("referencia"));
        String estado = limpiar(request.getParameter("estado"));

        request.setAttribute("modoEdicion", true);
        conservarFormulario(request, codigo, fechaHoraTexto, magnitudTexto, profundidadTexto,
                latitudTexto, longitudTexto, departamento, referencia, estado);

        SismoRepository repositorio = repositorio(request);
        if (repositorio.buscarPorCodigo(codigo).isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("mensajeError", "No existe un sismo con el código solicitado.");
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp")
                    .forward(request, response);
            return;
        }

        // US-02: ningún campo puede quedar vacío (aplica también al editar).
        if (fechaHoraTexto.isBlank() || magnitudTexto.isBlank() || profundidadTexto.isBlank()
                || latitudTexto.isBlank() || longitudTexto.isBlank() || departamento.isBlank()
                || referencia.isBlank() || estado.isBlank()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            mostrarFormulario(request, response);
            return;
        }

        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraTexto);
            double magnitud = Double.parseDouble(magnitudTexto);
            double profundidad = Double.parseDouble(profundidadTexto);
            double latitud = Double.parseDouble(latitudTexto);
            double longitud = Double.parseDouble(longitudTexto);

            // US-41: magnitud, profundidad y estado dentro de los valores permitidos (también al editar).
            if (magnitud <= 0) {
                request.setAttribute("error", "La magnitud debe ser un número mayor que cero");
                mostrarFormulario(request, response);
                return;
            }
            if (profundidad < 0) {
                request.setAttribute("error", "La profundidad debe ser un número mayor o igual que cero");
                mostrarFormulario(request, response);
                return;
            }
            if (!SismoNuevoServlet.ESTADOS_VALIDOS.contains(estado)) {
                request.setAttribute("error", "El estado seleccionado no es válido");
                mostrarFormulario(request, response);
                return;
            }

            // No implementado en esta entrega (Fase 2 del backlog, fuera del alcance de la PA1):
            // if (latitud < -90 || latitud > 90) { ... }              // US-03
            // if (longitud < -180 || longitud > 180) { ... }          // US-03
            // if (fechaHora.isAfter(LocalDateTime.now())) { ... }     // US-04

            Sismo sismo = new Sismo(codigo, fechaHora, magnitud, profundidad, latitud, longitud,
                    departamento, referencia, estado);
            repositorio.actualizar(sismo);

            // Redirect inicia una nueva petición GET y evita repetir el POST al recargar.
            response.sendRedirect(request.getContextPath()
                    + "/sismos/detalle?codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8)
                    + "&editado=1");
        } catch (NumberFormatException | DateTimeParseException ex) {
            request.setAttribute("error", "Uno o más campos numéricos o de fecha tienen un formato incorrecto.");
            mostrarFormulario(request, response);
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/sismos/formulario.jsp")
                .forward(request, response);
    }

    private void conservarFormulario(HttpServletRequest request, String codigo, String fechaHora,
                                      String magnitud, String profundidad, String latitud, String longitud,
                                      String departamento, String referencia, String estado) {
        request.setAttribute("codigoIngresado", codigo);
        request.setAttribute("fechaHoraIngresada", fechaHora);
        request.setAttribute("magnitudIngresada", magnitud);
        request.setAttribute("profundidadIngresada", profundidad);
        request.setAttribute("latitudIngresada", latitud);
        request.setAttribute("longitudIngresada", longitud);
        request.setAttribute("departamentoIngresado", departamento);
        request.setAttribute("referenciaIngresada", referencia);
        request.setAttribute("estadoIngresado", estado);
    }

    private SismoRepository repositorio(HttpServletRequest request) {
        return (SismoRepository) request.getServletContext()
                .getAttribute(AplicacionListener.REPOSITORIO_SISMOS);
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
