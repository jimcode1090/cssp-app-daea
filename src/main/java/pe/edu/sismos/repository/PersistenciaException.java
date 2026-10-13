package pe.edu.sismos.repository;

/** Excepción de infraestructura para que las capas web no dependan de JPA. */
public class PersistenciaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
