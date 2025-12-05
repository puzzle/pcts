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

    private List<DegreeCalculation> degreeCalculations;

    private List<ExperienceCalculation> experienceCalculations;

    private List<CertificateCalculation> certificateCalculations;

    public Calculation(Long id, Member member, Role role, CalculationState state, LocalDate publicationDate,
                       String publicizedBy, List<DegreeCalculation> degreeCalculations,
                       List<ExperienceCalculation> experienceCalculations,
                       List<CertificateCalculation> certificateCalculations) {
        this.id = id;
        this.member = member;
        this.role = role;
        this.state = state;
        this.publicationDate = publicationDate;
        this.publicizedBy = publicizedBy;
        this.degreeCalculations = degreeCalculations;
        this.experienceCalculations = experienceCalculations;
        this.certificateCalculations = certificateCalculations;
    }

    public Calculation() {
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        Calculation that = (Calculation) object;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMember(), that.getMember())
               && Objects.equals(getRole(), that.getRole()) && getState() == that.getState()
               && Objects.equals(getPublicationDate(), that.getPublicationDate())
               && Objects.equals(getPublicizedBy(), that.getPublicizedBy())
               && Objects.equals(degreeCalculations, that.degreeCalculations)
               && Objects.equals(experienceCalculations, that.experienceCalculations)
               && Objects.equals(certificateCalculations, that.certificateCalculations);
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
                      degreeCalculations,
                      experienceCalculations,
                      certificateCalculations);
    }

    @Override
    public String toString() {
        return "Calculation{" + "id=" + id + ", member=" + member + ", role=" + role + ", state=" + state
               + ", publicationDate=" + publicationDate + ", publicizedBy='" + publicizedBy + '\''
               + ", degreeCalculations=" + degreeCalculations + ", experienceCalculations=" + experienceCalculations
               + ", certificateCalculations=" + certificateCalculations + '}';
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

    public List<DegreeCalculation> getDegreeCalculations() {
        return degreeCalculations;
    }

    public void setDegreeCalculations(List<DegreeCalculation> degreeCalculations) {
        this.degreeCalculations = degreeCalculations;
    }

    public List<ExperienceCalculation> getExperienceCalculations() {
        return experienceCalculations;
    }

    public void setExperienceCalculations(List<ExperienceCalculation> experienceCalculations) {
        this.experienceCalculations = experienceCalculations;
    }

    public List<CertificateCalculation> getCertificateCalculations() {
        return certificateCalculations;
    }

    public void setCertificateCalculations(List<CertificateCalculation> certificateCalculations) {
        this.certificateCalculations = certificateCalculations;
    }
}
