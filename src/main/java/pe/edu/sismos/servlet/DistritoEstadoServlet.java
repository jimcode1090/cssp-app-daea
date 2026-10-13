package pe.edu.sismos.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pe.edu.sismos.model.Distrito;
import pe.edu.sismos.repository.DistritoRepository;
import pe.edu.sismos.repository.SismoRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "DistritoEstadoServlet", urlPatterns = "/ubicaciones/distritos/estado")
public class DistritoEstadoServlet extends HttpServlet {
    private final DistritoRepository distritoRepository = new DistritoRepository();
    private final SismoRepository sismoRepository = new SismoRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Distrito distrito = distritoRepository.buscarPorId(DepartamentoEditarServlet.leerId(request.getParameter("id"))).orElse(null);
        String resultado = "";
        if (distrito != null) {
            if ("ACTIVO".equals(distrito.getEstado())) {
                if (sismoRepository.contarActivosDeDistrito(distrito.getId()) > 0) {
                    request.setAttribute("error", "El distrito está asociado a sismos registrados");
                    DistritoListarServlet.mostrar(request, response);
                    return;
                }
                distrito.setEstado("INACTIVO");
            } else {
                distrito.setEstado("ACTIVO");
            }
            distritoRepository.guardar(distrito);
            resultado = "?resultado=" + ("INACTIVO".equals(distrito.getEstado()) ? "desactivado" : "activado") + "&nombre=" + URLEncoder.encode(distrito.getNombre(), StandardCharsets.UTF_8);
        }
        response.sendRedirect(request.getContextPath() + "/ubicaciones/distritos" + resultado);
    }
}
