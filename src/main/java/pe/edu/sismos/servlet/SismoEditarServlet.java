package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.model.Estacion;
import pe.edu.sismos.model.Sismo;
import pe.edu.sismos.repository.EstacionRepository;
import pe.edu.sismos.repository.PersistenciaException;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

// US-21: Editar los datos de un sismo.
@WebServlet(name = "SismoEditarServlet", urlPatterns = "/sismos/editar")
public class SismoEditarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final SismoRepository sismoRepository = new SismoRepository();
    private final EstacionRepository estacionRepository = new EstacionRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        Optional<Sismo> encontrado = sismoRepository.buscarPorCodigo(codigo);

        if (encontrado.isEmpty()) {
            noEncontrado(request, response);
            return;
        }

        // US-21 (CA1): el formulario se carga con los datos actuales y el código en solo lectura.
        Sismo sismo = encontrado.get();
        request.setAttribute("modoEdicion", true);
        SismoNuevoServlet.conservarFormulario(request, sismo.getCodigo(), sismo.getFechaHora().toString(),
                String.valueOf(sismo.getMagnitud()), String.valueOf(sismo.getProfundidad()),
                String.valueOf(sismo.getLatitud()), String.valueOf(sismo.getLongitud()),
                sismo.getDistrito().getProvincia().getDepartamento().getId(), sismo.getDistrito().getProvincia().getId(),
                sismo.getDistrito().getId(), sismo.getEstacion().getId(), sismo.getReferencia(), sismo.getEstado());

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
        long departamentoId = DepartamentoEditarServlet.leerId(request.getParameter("departamentoId"));
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        long distritoId = DepartamentoEditarServlet.leerId(request.getParameter("distritoId"));
        long estacionId = DepartamentoEditarServlet.leerId(request.getParameter("estacionId"));
        String referencia = limpiar(request.getParameter("referencia"));
        String estado = limpiar(request.getParameter("estado"));

        request.setAttribute("modoEdicion", true);
        SismoNuevoServlet.conservarFormulario(request, codigo, fechaHoraTexto, magnitudTexto, profundidadTexto,
                latitudTexto, longitudTexto, departamentoId, provinciaId, distritoId, estacionId, referencia, estado);

        Sismo existente = sismoRepository.buscarPorCodigo(codigo).orElse(null);
        if (existente == null) {
            noEncontrado(request, response);
            return;
        }

        try {
            Distrito distrito = SismoNuevoServlet.resolverDistrito(departamentoId, provinciaId, distritoId);
            Estacion estacion = estacionRepository.buscarPorId(estacionId).orElse(null);

            // US-02 y US-41: las mismas validaciones del registro (también al editar).
            Sismo sismo = SismoNuevoServlet.construir(existente.getId(), codigo, fechaHoraTexto, magnitudTexto,
                    profundidadTexto, latitudTexto, longitudTexto, referencia, estado, distrito, estacion);
            sismoRepository.guardar(sismo);

            // Redirect inicia una nueva petición GET y evita repetir el POST al recargar.
            response.sendRedirect(request.getContextPath()
                    + "/sismos/detalle?codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8)
                    + "&editado=1");
        } catch (IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrarFormulario(request, response);
        } catch (PersistenciaException ex) {
            request.setAttribute("error", ex.getMessage());
            mostrarFormulario(request, response);
        }
    }

    private void noEncontrado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("mensajeError", "No existe un sismo con el código solicitado.");
        request.getRequestDispatcher("/WEB-INF/views/error/404.jsp")
                .forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SismoNuevoServlet.cargarListas(request, (long) request.getAttribute("departamentoIdIngresado"),
                (long) request.getAttribute("provinciaIdIngresada"));
        request.getRequestDispatcher("/WEB-INF/views/sismos/formulario.jsp")
                .forward(request, response);
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
