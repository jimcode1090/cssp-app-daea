package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.repository.EstacionRepository;

import java.io.IOException;

@WebServlet(name = "EstacionListarServlet", urlPatterns = "/estaciones")
public class EstacionListarServlet extends HttpServlet {
    private final EstacionRepository estacionRepository = new EstacionRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("estaciones", estacionRepository.listar());
        request.getRequestDispatcher("/WEB-INF/views/estaciones/lista.jsp").forward(request, response);
    }
}
