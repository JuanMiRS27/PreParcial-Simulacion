package com.example.preparcialarle.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false, unique = true)
    private Claim claim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus resultado;

    @Column(nullable = false)
    private Integer puntajeRiesgo;

    @Column(nullable = false, length = 1000)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fechaEvaluacion;

    @PrePersist
    public void prePersist() {
        if (fechaEvaluacion == null) fechaEvaluacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Claim getClaim() { return claim; }
    public void setClaim(Claim claim) { this.claim = claim; }
    public ClaimStatus getResultado() { return resultado; }
    public void setResultado(ClaimStatus resultado) { this.resultado = resultado; }
    public Integer getPuntajeRiesgo() { return puntajeRiesgo; }
    public void setPuntajeRiesgo(Integer puntajeRiesgo) { this.puntajeRiesgo = puntajeRiesgo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }
}
