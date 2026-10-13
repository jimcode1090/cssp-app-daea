package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.repository.DistritoRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(name = "DistritoListaJsonServlet", urlPatterns = "/ubicaciones/distritos/lista")
public class DistritoListaJsonServlet extends HttpServlet {
    private final DistritoRepository distritoRepository = new DistritoRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long provinciaId = DepartamentoEditarServlet.leerId(request.getParameter("provinciaId"));
        List<Distrito> distritos = distritoRepository.listarActivosPorProvincia(provinciaId);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < distritos.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(distritos.get(i).getId())
                    .append(",\"nombre\":\"").append(ProvinciaListaJsonServlet.escapar(distritos.get(i).getNombre())).append("\"}");
        }
        json.append(']');
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json.toString());
    }
}
