package ch.puzzle.pcts.model.calculation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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

    private Calculation(Builder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.role = builder.role;
        this.state = builder.state;
        this.publicationDate = builder.publicationDate;
        this.publicizedBy = trim(builder.publicizedBy);
    }

    public Calculation() {
    }

    @Override
    public String toString() {
        return "Calculation{" + "id=" + id + ", member=" + member + ", role=" + role + ", state=" + state
               + ", publicationDate=" + publicationDate + ", publicizedBy='" + publicizedBy + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Calculation that)) {
            return false;
        }
        return Objects.equals(getId(), that.getId()) && Objects.equals(getMember(), that.getMember())
               && Objects.equals(getRole(), that.getRole()) && getState() == that.getState()
               && Objects.equals(getPublicationDate(), that.getPublicationDate())
               && Objects.equals(getPublicizedBy(), that.getPublicizedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMember(), getRole(), getState(), getPublicationDate(), getPublicizedBy());
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

    public static final class Builder {
        private Long id;
        private Member member;
        private Role role;
        private CalculationState state;
        private LocalDate publicationDate;
        private String publicizedBy;

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

        public Calculation build() {
            return new Calculation(this);
        }
    }
}
