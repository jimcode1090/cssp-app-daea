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
import java.util.Optional;

// US-22: Eliminar un sismo con confirmación previa.
// Igual que Editar, el profesor no llegó a mostrar esta pantalla en clase, pero está
// definida en la sección 5.7 y sigue el mismo patrón GET (confirmar) / POST (eliminar).
@WebServlet(name = "SismoEliminarServlet", urlPatterns = "/sismos/eliminar")
public class SismoEliminarServlet extends HttpServlet {

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

        // US-22 (CA1, CA2): pantalla de confirmación con los datos del sismo y la advertencia.
        request.setAttribute("sismo", encontrado.get());
        request.getRequestDispatcher("/WEB-INF/views/sismos/confirmar-eliminar.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        SismoRepository repositorio = repositorio(request);

        if (repositorio.buscarPorCodigo(codigo).isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("mensajeError", "No existe un sismo con el código solicitado.");
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp")
                    .forward(request, response);
            return;
        }

        // US-22 (CA3): se elimina y se redirige al listado con un mensaje de confirmación.
        repositorio.eliminar(codigo);

        response.sendRedirect(request.getContextPath()
                + "/sismos?eliminado=1&codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8));
    }

    private SismoRepository repositorio(HttpServletRequest request) {
        return (SismoRepository) request.getServletContext()
                .getAttribute(AplicacionListener.REPOSITORIO_SISMOS);
    }
}
