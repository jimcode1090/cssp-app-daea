package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.repository.DistritoRepository;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;

// US-27: Gestionar distritos (listado y formulario Agregar).
@WebServlet(name = "DistritoListarServlet", urlPatterns = "/ubicaciones/distritos")
public class DistritoListarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrar(request, response);
    }

    static void mostrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("distritos", new DistritoRepository().listar());
        request.setAttribute("provincias", new ProvinciaRepository().listarActivas());
        request.getRequestDispatcher("/WEB-INF/views/ubicaciones/distritos.jsp").forward(request, response);
    }
}
