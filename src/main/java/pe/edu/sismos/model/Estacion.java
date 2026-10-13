package pe.edu.sismos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estacion")
public class Estacion {
    private static final DateTimeFormatter FORMATO_FECHA_VISIBLE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;
    @Column(nullable = false, length = 120)
    private String nombre;
    @Column(nullable = false)
    private double latitud;
    @Column(nullable = false)
    private double longitud;
    @Column(name = "fecha_instalacion", nullable = false)
    private LocalDate fechaInstalacion;
    @Column(nullable = false, length = 10)
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;

    @OneToMany(mappedBy = "estacion")
    private List<Sismo> sismos = new ArrayList<>();

    public Estacion() {
    }

    public Estacion(Long id, String codigo, String nombre, Provincia provincia, double latitud, double longitud,
                    LocalDate fechaInstalacion, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaInstalacion = fechaInstalacion;
        this.estado = estado;
    }

    public String getFechaInstalacionTexto() {
        return fechaInstalacion.format(FORMATO_FECHA_VISIBLE);
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public LocalDate getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(LocalDate fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public List<Sismo> getSismos() {
        return sismos;
    }

    public void setSismos(List<Sismo> sismos) {
        this.sismos = sismos;
    }
}
