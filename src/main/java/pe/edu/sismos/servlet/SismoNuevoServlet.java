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
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

// US-01: Registrar un nuevo sismo.
// US-02 (Ticket 7): ningún campo puede quedar vacío; se agrega la validación y la
// "zona de mensajes" en formulario.jsp. La validación de código duplicado (US-31, Ticket 8)
// y de rangos (US-41, Ticket 9) se agregan en las ramas siguientes.
@WebServlet(name = "SismoNuevoServlet", urlPatterns = "/sismos/nuevo")
public class SismoNuevoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
        String departamento = limpiar(request.getParameter("departamento"));
        String referencia = limpiar(request.getParameter("referencia"));
        String estado = limpiar(request.getParameter("estado"));

        conservarFormulario(request, codigo, fechaHoraTexto, magnitudTexto, profundidadTexto,
                latitudTexto, longitudTexto, departamento, referencia, estado);

        // US-02: ningún campo puede quedar vacío.
        if (codigo.isBlank() || fechaHoraTexto.isBlank() || magnitudTexto.isBlank()
                || profundidadTexto.isBlank() || latitudTexto.isBlank() || longitudTexto.isBlank()
                || departamento.isBlank() || referencia.isBlank() || estado.isBlank()) {
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

            Sismo sismo = new Sismo(codigo, fechaHora, magnitud, profundidad, latitud, longitud,
                    departamento, referencia, estado);
            repositorio(request).agregar(sismo);

            // Redirect inicia una nueva petición GET y evita repetir el POST al recargar.
            response.sendRedirect(request.getContextPath() + "/sismos");
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
