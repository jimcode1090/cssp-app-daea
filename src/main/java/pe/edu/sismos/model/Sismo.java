package pe.edu.sismos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "sismo")
public class Sismo {

    private static final DateTimeFormatter FORMATO_FECHA_VISIBLE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    @Column(nullable = false)
    private double magnitud;
    @Column(nullable = false)
    private double profundidad;
    @Column(nullable = false)
    private double latitud;
    @Column(nullable = false)
    private double longitud;
    @Column(nullable = false, length = 200)
    private String referencia;
    @Column(nullable = false, length = 20)
    private String estado;
    @Column(name = "estado_registro", nullable = false, length = 10)
    private String estadoRegistro;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "distrito_id", nullable = false)
    private Distrito distrito;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "estacion_id", nullable = false)
    private Estacion estacion;

    public Sismo() {
    }

    public Sismo(Long id, String codigo, LocalDateTime fechaHora, double magnitud, double profundidad,
                 double latitud, double longitud, String referencia, String estado, String estadoRegistro,
                 Distrito distrito, Estacion estacion) {
        this.id = id;
        this.codigo = codigo;
        this.fechaHora = fechaHora;
        this.magnitud = magnitud;
        this.profundidad = profundidad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.referencia = referencia;
        this.estado = estado;
        this.estadoRegistro = estadoRegistro;
        this.distrito = distrito;
        this.estacion = estacion;
    }

    public String getFechaHoraTexto() {
        return fechaHora.format(FORMATO_FECHA_VISIBLE);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(double magnitud) {
        this.magnitud = magnitud;
    }

    public double getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(double profundidad) {
        this.profundidad = profundidad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }
}
