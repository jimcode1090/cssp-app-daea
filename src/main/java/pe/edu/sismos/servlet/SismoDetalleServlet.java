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
import java.util.Optional;

// US-12: Ver el detalle completo de un sismo.
@WebServlet(name = "SismoDetalleServlet", urlPatterns = "/sismos/detalle")
public class SismoDetalleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        SismoRepository repositorio = (SismoRepository) getServletContext()
                .getAttribute(AplicacionListener.REPOSITORIO_SISMOS);
        Optional<Sismo> encontrado = repositorio.buscarPorCodigo(codigo);

        // US-25: si el código no existe, se responde con 404.
        if (encontrado.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("mensajeError", "No existe un sismo con el código solicitado.");
            request.getRequestDispatcher("/WEB-INF/views/error/404.jsp")
                    .forward(request, response);
            return;
        }

        request.setAttribute("sismo", encontrado.get());
        request.setAttribute("sismoCreado", "1".equals(request.getParameter("creado")));
        request.setAttribute("sismoActualizado", "1".equals(request.getParameter("editado")));
        request.getRequestDispatcher("/WEB-INF/views/sismos/detalle.jsp")
                .forward(request, response);
    }
}
