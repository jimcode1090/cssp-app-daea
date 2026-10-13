package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Sismo;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet(name = "SismoEliminarServlet", urlPatterns = "/sismos/eliminar")
public class SismoEliminarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final SismoRepository sismoRepository = new SismoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        Optional<Sismo> encontrado = sismoRepository.buscarPorCodigo(codigo);

        if (encontrado.isEmpty()) {
            noEncontrado(request, response);
            return;
        }

        request.setAttribute("sismo", encontrado.get());
        request.getRequestDispatcher("/WEB-INF/views/sismos/confirmar-eliminar.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");

        if (codigo == null || !sismoRepository.eliminar(codigo)) {
            noEncontrado(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath()
                + "/sismos?eliminado=1&codigo=" + URLEncoder.encode(codigo, StandardCharsets.UTF_8));
    }

    private void noEncontrado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute("mensajeError", "No existe un sismo con el código solicitado.");
        request.getRequestDispatcher("/WEB-INF/views/error/404.jsp")
                .forward(request, response);
    }
}
