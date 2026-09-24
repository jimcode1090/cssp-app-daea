package pe.edu.sismos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un sismo con sus atributos (sección 5.4 de los requisitos técnicos).
 * Es un record de Java: los datos son inmutables. Para editar un sismo se crea
 * un nuevo objeto con los datos corregidos y se reemplaza el anterior en el repositorio.
 */
public record Sismo(
        String codigo,
        LocalDateTime fechaHora,
        double magnitud,
        double profundidad,
        double latitud,
        double longitud,
        String departamento,
        String referencia,
        String estado
) {

    // Adicional a la sección 5.4: el profesor no trabajó fechas en su ejemplo (Producto no
    // tenía), así que no hay un patrón de clase visto para mostrarlas. Se resuelve con un
    // método del propio record (getFechaHoraTexto), sin sacar la lógica de formato a la JSP
    // ni usar scriptlets, que es la regla que sí dio el profesor.
    private static final DateTimeFormatter FORMATO_FECHA_VISIBLE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String getFechaHoraTexto() {
        return fechaHora.format(FORMATO_FECHA_VISIBLE);
    }
}
