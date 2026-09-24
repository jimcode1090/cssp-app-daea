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
import java.util.List;

// US-11: Ver el listado de sismos.
@WebServlet(name = "SismoListarServlet", urlPatterns = "/sismos")
public class SismoListarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SismoRepository repositorio = repositorio(request);
        List<Sismo> sismos = repositorio.listar();

        // El Servlet controla: recibe, procesa y prepara el modelo para la vista.
        request.setAttribute("sismos", sismos);
        request.setAttribute("totalSismos", sismos.size());

        // US-22 (CA3): mensaje de confirmación tras eliminar, llega por query string
        // desde SismoEliminarServlet (Post/Redirect/Get).
        if ("1".equals(request.getParameter("eliminado"))) {
            request.setAttribute("eliminado", true);
            request.setAttribute("codigoEliminado", request.getParameter("codigo"));
        }

        // Forward conserva la misma request y sus atributos.
        request.getRequestDispatcher("/WEB-INF/views/sismos/lista.jsp")
                .forward(request, response);
    }

    private SismoRepository repositorio(HttpServletRequest request) {
        return (SismoRepository) request.getServletContext()
                .getAttribute(AplicacionListener.REPOSITORIO_SISMOS);
    }
}
