package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Sismo;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SismoListarServlet", urlPatterns = "/sismos")
public class SismoListarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final SismoRepository sismoRepository = new SismoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Sismo> sismos = sismoRepository.listar();

        request.setAttribute("sismos", sismos);
        request.setAttribute("totalSismos", sismos.size());

        if ("1".equals(request.getParameter("eliminado"))) {
            request.setAttribute("eliminado", true);
            request.setAttribute("codigoEliminado", request.getParameter("codigo"));
        }

        request.getRequestDispatcher("/WEB-INF/views/sismos/lista.jsp")
                .forward(request, response);
    }
}
