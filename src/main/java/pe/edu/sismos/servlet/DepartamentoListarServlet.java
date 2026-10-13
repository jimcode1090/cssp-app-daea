package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.repository.DepartamentoRepository;

import java.io.IOException;

// US-19: Gestionar departamentos (listado y formulario Agregar).
@WebServlet(name = "DepartamentoListarServlet", urlPatterns = "/ubicaciones/departamentos")
public class DepartamentoListarServlet extends HttpServlet {
    private final DepartamentoRepository departamentoRepository = new DepartamentoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("departamentos", departamentoRepository.listar());
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/departamentos.jsp").forward(request, response);
    }
}
