package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.repository.DepartamentoRepository;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;

// US-26: Gestionar provincias (listado y formulario Agregar).
@WebServlet(name = "ProvinciaListarServlet", urlPatterns = "/ubicaciones/provincias")
public class ProvinciaListarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrar(request, response);
    }

    static void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("provincias", new ProvinciaRepository().listar());
        request.setAttribute("departamentos", new DepartamentoRepository().listarActivos());
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/provincias.jsp").forward(request, response);
    }
}
