package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.repository.DepartamentoRepository;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;

@WebServlet(name = "DepartamentoListarServlet", urlPatterns = "/ubicaciones/departamentos")
public class DepartamentoListarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrar(request, response);
    }

    static void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("departamentos", new DepartamentoRepository().listar());
        request.setAttribute("provinciasPorDepartamento", new ProvinciaRepository().contarActivasPorDepartamento());
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/departamentos.jsp").forward(request, response);
    }
}
