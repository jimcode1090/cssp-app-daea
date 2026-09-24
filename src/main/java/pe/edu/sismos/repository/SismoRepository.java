package pe.edu.sismos.repository;

import pe.edu.sismos.model.Sismo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/** Repositorio temporal compartido. La persistencia se incorporará en una fase posterior. */
public class SismoRepository {

    private final List<Sismo> sismos = new CopyOnWriteArrayList<>();

    public SismoRepository() {
        sismos.add(new Sismo("SIS001", LocalDateTime.of(2026, 8, 18, 14, 32),
                5.6, 34, -16.4090, -71.5375, "Arequipa", "20 km al sur de la ciudad de Arequipa", "Registrado"));
        sismos.add(new Sismo("SIS002", LocalDateTime.of(2026, 9, 2, 3, 10),
                4.5, 45, -12.0464, -77.0428, "Lima", "Lima Metropolitana", "En evaluación"));
        sismos.add(new Sismo("SIS003", LocalDateTime.of(2026, 9, 10, 21, 47),
                4.8, 20, -13.5319, -71.9675, "Cusco", "Cusco ciudad", "Cerrado"));
        sismos.add(new Sismo("SIS004", LocalDateTime.of(2026, 9, 14, 8, 5),
                5.1, 60, -18.0146, -70.2536, "Tacna", "Tacna ciudad", "Registrado"));
        sismos.add(new Sismo("SIS005", LocalDateTime.of(2026, 9, 17, 16, 20),
                4.2, 12, -14.0678, -75.7286, "Ica", "Ica ciudad", "En seguimiento"));
    }

    public List<Sismo> listar() {
        return List.copyOf(sismos);
    }

    public Optional<Sismo> buscarPorCodigo(String codigo) {
        if (codigo == null) {
            return Optional.empty();
        }
        return sismos.stream()
                .filter(s -> s.codigo().equalsIgnoreCase(codigo.trim()))
                .findFirst();
    }

    public boolean existeCodigo(String codigo) {
        return buscarPorCodigo(codigo).isPresent();
    }

    public void agregar(Sismo sismo) {
        sismos.add(sismo);
    }

    public void actualizar(Sismo sismo) {
        buscarPorCodigo(sismo.codigo()).ifPresent(sismos::remove);
        sismos.add(sismo);
    }

    public void eliminar(String codigo) {
        buscarPorCodigo(codigo).ifPresent(sismos::remove);
    }
}
