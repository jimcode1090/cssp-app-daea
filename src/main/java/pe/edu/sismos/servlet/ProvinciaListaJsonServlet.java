package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Provincia;
import pe.edu.sismos.repository.ProvinciaRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

// US-28: provincias activas de un departamento, en formato JSON, para la lista dependiente.
@WebServlet(name = "ProvinciaListaJsonServlet", urlPatterns = "/ubicaciones/provincias/lista")
public class ProvinciaListaJsonServlet extends HttpServlet {
    private final ProvinciaRepository provinciaRepository = new ProvinciaRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long departamentoId = DepartamentoEditarServlet.leerId(request.getParameter("departamentoId"));
        List<Provincia> provincias = provinciaRepository.listarActivasPorDepartamento(departamentoId);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < provincias.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(provincias.get(i).getId())
                    .append(",\"nombre\":\"").append(escapar(provincias.get(i).getNombre())).append("\"}");
        }
        json.append(']');
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json.toString());
    }

    static String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
