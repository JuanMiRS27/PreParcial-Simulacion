package com.example.preparcialarle.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimType tipoSiniestro;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorEstimado;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private LocalDate fechaSiniestro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToOne(mappedBy = "claim", cascade = CascadeType.ALL)
    private Evaluation evaluation;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) estado = ClaimStatus.PENDIENTE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClaimType getTipoSiniestro() { return tipoSiniestro; }
    public void setTipoSiniestro(ClaimType tipoSiniestro) { this.tipoSiniestro = tipoSiniestro; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getValorEstimado() { return valorEstimado; }
    public void setValorEstimado(BigDecimal valorEstimado) { this.valorEstimado = valorEstimado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public LocalDate getFechaSiniestro() { return fechaSiniestro; }
    public void setFechaSiniestro(LocalDate fechaSiniestro) { this.fechaSiniestro = fechaSiniestro; }
    public ClaimStatus getEstado() { return estado; }
    public void setEstado(ClaimStatus estado) { this.estado = estado; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Evaluation getEvaluation() { return evaluation; }
    public void setEvaluation(Evaluation evaluation) { this.evaluation = evaluation; }
}
