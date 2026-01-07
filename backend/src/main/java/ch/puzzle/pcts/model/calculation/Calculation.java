package ch.puzzle.pcts.model.calculation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Calculation implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private CalculationState state;

    private LocalDate publicationDate;

    private String publicizedBy;

    @OneToMany(mappedBy = "calculation", fetch = FetchType.LAZY)
    private List<DegreeCalculation> degrees;

    @OneToMany(mappedBy = "calculation", fetch = FetchType.LAZY)
    private List<ExperienceCalculation> experiences;

    @OneToMany(mappedBy = "calculation", fetch = FetchType.LAZY)
    private List<CertificateCalculation> certificates;

    @Transient
    private BigDecimal points;

    public Calculation(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.role = builder.role;
        this.state = builder.state;
        this.publicationDate = builder.publicationDate;
        this.publicizedBy = trim(builder.publicizedBy);
        this.degrees = builder.degrees;
        this.experiences = builder.experiences;
        this.certificates = builder.certificates;
        this.points = builder.points;
    }

    public Calculation() {
    }

    @Override
    public String toString() {
        return "Calculation{" + "id=" + id + ", member=" + member + ", role=" + role + ", state=" + state
               + ", publicationDate=" + publicationDate + ", publicizedBy='" + publicizedBy + '\'' + ", degrees="
               + degrees + ", experiences=" + experiences + ", certificates=" + certificates + ", points=" + points
               + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Calculation that))
            return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMember(), that.getMember())
               && Objects.equals(getRole(), that.getRole()) && getState() == that.getState()
               && Objects.equals(getPublicationDate(), that.getPublicationDate())
               && Objects.equals(getPublicizedBy(), that.getPublicizedBy())
               && Objects.equals(getDegrees(), that.getDegrees())
               && Objects.equals(getExperiences(), that.getExperiences())
               && Objects.equals(getCertificates(), that.getCertificates())
               && Objects.equals(getPoints(), that.getPoints());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getMember(),
                      getRole(),
                      getState(),
                      getPublicationDate(),
                      getPublicizedBy(),
                      getDegrees(),
                      getExperiences(),
                      getCertificates(),
                      getPoints());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public CalculationState getState() {
        return state;
    }

    public void setState(CalculationState state) {
        this.state = state;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublicizedBy() {
        return trim(publicizedBy);
    }

    public void setPublicizedBy(String publicizedBy) {
        this.publicizedBy = trim(publicizedBy);
    }

    public List<DegreeCalculation> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<DegreeCalculation> degrees) {
        this.degrees = degrees;
    }

    public List<ExperienceCalculation> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceCalculation> experiences) {
        this.experiences = experiences;
    }

    public List<CertificateCalculation> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateCalculation> certificates) {
        this.certificates = certificates;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    public static final class Builder {
        private Long id;
        private Member member;
        private Role role;
        private CalculationState state;
        private LocalDate publicationDate;
        private String publicizedBy;
        private List<DegreeCalculation> degrees;
        private List<ExperienceCalculation> experiences;
        private List<CertificateCalculation> certificates;
        private BigDecimal points;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withMember(Member member) {
            this.member = member;
            return this;
        }

        public Builder withRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder withState(CalculationState state) {
            this.state = state;
            return this;
        }

        public Builder withPublicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder withPublicizedBy(String publicizedBy) {
            this.publicizedBy = trim(publicizedBy);
            return this;
        }

        public Builder withDegrees(List<DegreeCalculation> degrees) {
            this.degrees = degrees;
            return this;
        }

        public Builder withExperiences(List<ExperienceCalculation> experiences) {
            this.experiences = experiences;
            return this;
        }

        public Builder withCertificates(List<CertificateCalculation> certificates) {
            this.certificates = certificates;
            return this;
        }

        public Builder withPoints(BigDecimal points) {
            this.points = points;
            return this;
        }

        public Calculation build() {
            return new Calculation(this);
        }

    }
}
