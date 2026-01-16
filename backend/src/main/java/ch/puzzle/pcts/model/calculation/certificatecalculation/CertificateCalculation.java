package ch.puzzle.pcts.model.calculation.certificatecalculation;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChild;
import ch.puzzle.pcts.model.certificate.Certificate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class CertificateCalculation implements CalculationChild, Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;

    public CertificateCalculation(Long id, Calculation calculation, Certificate certificate) {
        this.id = id;
        this.calculation = calculation;
        this.certificate = certificate;
    }

    public CertificateCalculation() {
    }

    @Override
    public String toString() {
        return "CertificateCalculation{" + "id=" + id + ", calculation="
               + (getCalculation() != null ? getCalculation().getId() : null) + ", certificate=" + getCertificate()
               + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CertificateCalculation that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId())
               && Objects
                       .equals(this.getCalculation() != null ? this.getCalculation().getId() : null,
                               that.getCalculation() != null ? that.getCalculation().getId() : null)
               && Objects.equals(getCertificate(), that.getCertificate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCalculation() != null ? getCalculation().getId() : null, getCertificate());
    }

    public boolean isLeadershipExperience() {
        return this.getCertificate().getCertificateType().getCertificateKind().isLeadershipExperienceType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }
}
