package ch.puzzle.pcts.util;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewCvDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewMemberDto;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.certificatetype.Tag;
import java.util.List;

public class TestDataDTOs {

    private TestDataDTOs() {
    }

    public static final RoleDto ROLE_2_DTO = new RoleDto(ROLE_2.getId(), ROLE_2.getName(), ROLE_2.getIsManagement());

    public static final RoleDto ROLE_2_INPUT = new RoleDto(null, ROLE_2.getName(), ROLE_2.getIsManagement());

    public static final OrganisationUnitDto ORG_UNIT_1_DTO = new OrganisationUnitDto(ORG_UNIT_1_ID,
                                                                                     ORG_UNIT_1.getName());

    public static final OrganisationUnitDto ORG_UNIT_2_DTO = new OrganisationUnitDto(ORG_UNIT_2_ID,
                                                                                     ORG_UNIT_2.getName());

    public static final OrganisationUnitDto ORG_UNIT_1_INPUT = new OrganisationUnitDto(null, ORG_UNIT_1.getName());

    public static final MemberDto MEMBER_1_DTO = new MemberDto(MEMBER_1_ID,
                                                               MEMBER_1.getFirstName(),
                                                               MEMBER_1.getLastName(),
                                                               MEMBER_1.getEmploymentState(),
                                                               MEMBER_1.getAbbreviation(),
                                                               MEMBER_1.getDateOfHire(),
                                                               MEMBER_1.getBirthDate(),
                                                               ORG_UNIT_1_DTO);

    public static final MemberDto MEMBER_2_DTO = new MemberDto(MEMBER_2_ID,
                                                               MEMBER_2.getFirstName(),
                                                               MEMBER_2.getLastName(),
                                                               MEMBER_2.getEmploymentState(),
                                                               MEMBER_2.getAbbreviation(),
                                                               MEMBER_2.getDateOfHire(),
                                                               MEMBER_2.getBirthDate(),
                                                               ORG_UNIT_2_DTO);

    public static final MemberInputDto MEMBER_1_INPUT = new MemberInputDto(MEMBER_1.getFirstName(),
                                                                           MEMBER_1.getLastName(),
                                                                           MEMBER_1.getEmploymentState(),
                                                                           MEMBER_1.getAbbreviation(),
                                                                           MEMBER_1.getDateOfHire(),
                                                                           MEMBER_1.getBirthDate(),
                                                                           MEMBER_1.getOrganisationUnit().getId());

    public static final MemberInputDto MEMBER_2_INPUT = new MemberInputDto(MEMBER_2.getFirstName(),
                                                                           MEMBER_2.getLastName(),
                                                                           MEMBER_2.getEmploymentState(),
                                                                           MEMBER_2.getAbbreviation(),
                                                                           MEMBER_2.getDateOfHire(),
                                                                           MEMBER_2.getBirthDate(),
                                                                           MEMBER_2.getOrganisationUnit().getId());

    public static final CertificateTypeDto CERT_TYPE_1_DTO = new CertificateTypeDto(CERT_TYPE_1_ID,
                                                                                    CERT_TYPE_1.getName(),
                                                                                    CERT_TYPE_1.getPoints(),
                                                                                    CERT_TYPE_1.getComment(),
                                                                                    CERT_TYPE_1
                                                                                            .getTags()
                                                                                            .stream()
                                                                                            .map(Tag::getName)
                                                                                            .toList());

    public static final CertificateTypeDto CERT_TYPE_5_DTO = new CertificateTypeDto(CERT_TYPE_5_ID,
                                                                                    CERT_TYPE_5.getName(),
                                                                                    CERT_TYPE_5.getPoints(),
                                                                                    CERT_TYPE_5.getComment(),
                                                                                    CERT_TYPE_5
                                                                                            .getTags()
                                                                                            .stream()
                                                                                            .map(Tag::getName)
                                                                                            .toList());

    public static final CertificateTypeDto CERT_TYPE_6_DTO = new CertificateTypeDto(CERT_TYPE_6_ID,
                                                                                    CERT_TYPE_6.getName(),
                                                                                    CERT_TYPE_6.getPoints(),
                                                                                    CERT_TYPE_6.getComment(),
                                                                                    CERT_TYPE_6
                                                                                            .getTags()
                                                                                            .stream()
                                                                                            .map(Tag::getName)
                                                                                            .toList());

    public static final CertificateTypeDto CERT_TYPE_5_Input = new CertificateTypeDto(null,
                                                                                      CERT_TYPE_5.getName(),
                                                                                      CERT_TYPE_5.getPoints(),
                                                                                      CERT_TYPE_5.getComment(),
                                                                                      CERT_TYPE_5
                                                                                              .getTags()
                                                                                              .stream()
                                                                                              .map(Tag::getName)
                                                                                              .toList());

    public final static LeadershipExperienceTypeDto LEADERSHIP_TYPE_1_DTO = new LeadershipExperienceTypeDto(LEADERSHIP_TYPE_1_ID,
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getName(),
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getPoints(),
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getComment(),
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getCertificateKind());

    public static final LeadershipExperienceTypeDto LEADERSHIP_TYPE_4_DTO = new LeadershipExperienceTypeDto(LEADERSHIP_TYPE_4_ID,
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getName(),
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getPoints(),
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getComment(),
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getCertificateKind());

    public static final LeadershipExperienceTypeDto LEADERSHIP_TYPE_5_DTO = new LeadershipExperienceTypeDto(LEADERSHIP_TYPE_5_ID,
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getName(),
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getPoints(),
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getComment(),
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getCertificateKind());

    public final static LeadershipExperienceTypeDto LEADERSHIP_TYPE_1_INPUT = new LeadershipExperienceTypeDto(null,
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getName(),
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getPoints(),
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getComment(),
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getCertificateKind());

    public static final CertificateDto CERTIFICATE_1_DTO = new CertificateDto(CERTIFICATE_1_ID,
                                                                              MEMBER_1_DTO,
                                                                              CERT_TYPE_1_DTO,
                                                                              CERTIFICATE_1.getCompletedAt(),
                                                                              CERTIFICATE_1.getValidUntil(),
                                                                              CERTIFICATE_1.getComment());

    public static final CertificateInputDto CERTIFICATE_1_INPUT = new CertificateInputDto(CERTIFICATE_1
            .getMember()
            .getId(),
                                                                                          CERTIFICATE_1
                                                                                                  .getCertificateType()
                                                                                                  .getId(),
                                                                                          CERTIFICATE_1
                                                                                                  .getCompletedAt(),
                                                                                          CERTIFICATE_1.getValidUntil(),
                                                                                          CERTIFICATE_1.getComment());

    public static final CertificateInputDto CERTIFICATE_2_INPUT = new CertificateInputDto(CERTIFICATE_2
            .getMember()
            .getId(),
                                                                                          CERTIFICATE_2
                                                                                                  .getCertificateType()
                                                                                                  .getId(),
                                                                                          CERTIFICATE_2
                                                                                                  .getCompletedAt(),
                                                                                          CERTIFICATE_2.getValidUntil(),
                                                                                          CERTIFICATE_2.getComment());

    public static final LeadershipExperienceDto LEADERSHIP_CERT_1_DTO = new LeadershipExperienceDto(LEADERSHIP_CERT_1_ID,
                                                                                                    MEMBER_1_DTO,
                                                                                                    LEADERSHIP_TYPE_1_DTO,
                                                                                                    LEADERSHIP_CERT_1
                                                                                                            .getComment());

    public final static LeadershipExperienceInputDto LEADERSHIP_CERT_1_INPUT = new LeadershipExperienceInputDto(LEADERSHIP_CERT_1
            .getMember()
            .getId(), LEADERSHIP_CERT_1.getCertificateType().getId(), LEADERSHIP_CERT_1.getComment());

    public final static LeadershipExperienceInputDto LEADERSHIP_CERT_2_INPUT = new LeadershipExperienceInputDto(LEADERSHIP_CERT_2
            .getMember()
            .getId(), LEADERSHIP_CERT_2.getCertificateType().getId(), LEADERSHIP_CERT_2.getComment());

    public static final DegreeTypeDto DEGREE_TYPE_1_DTO = new DegreeTypeDto(DEGREE_TYPE_1_ID,
                                                                            DEGREE_TYPE_1.getName(),
                                                                            DEGREE_TYPE_1.getHighlyRelevantPoints(),
                                                                            DEGREE_TYPE_1.getLimitedRelevantPoints(),
                                                                            DEGREE_TYPE_1.getLittleRelevantPoints());

    public static final DegreeTypeDto DEGREE_TYPE_1_INPUT = new DegreeTypeDto(null,
                                                                              DEGREE_TYPE_1.getName(),
                                                                              DEGREE_TYPE_1.getHighlyRelevantPoints(),
                                                                              DEGREE_TYPE_1.getLimitedRelevantPoints(),
                                                                              DEGREE_TYPE_1.getLittleRelevantPoints());

    public static final DegreeDto DEGREE_1_DTO = new DegreeDto(DEGREE_1.getId(),
                                                               MEMBER_1_DTO,
                                                               DEGREE_1.getName(),
                                                               DEGREE_1.getInstitution(),
                                                               DEGREE_1.getCompleted(),
                                                               DEGREE_TYPE_1_DTO,
                                                               DEGREE_1.getStartDate(),
                                                               DEGREE_1.getEndDate(),
                                                               DEGREE_1.getComment());

    public static final DegreeInputDto DEGREE_1_INPUT = new DegreeInputDto(DEGREE_1.getMember().getId(),
                                                                           DEGREE_1.getName(),
                                                                           DEGREE_1.getInstitution(),
                                                                           DEGREE_1.getCompleted(),
                                                                           DEGREE_1.getDegreeType().getId(),
                                                                           DEGREE_1.getStartDate(),
                                                                           DEGREE_1.getEndDate(),
                                                                           DEGREE_1.getComment());

    public static final DegreeInputDto DEGREE_2_INPUT = new DegreeInputDto(DEGREE_2.getMember().getId(),
                                                                           DEGREE_2.getName(),
                                                                           DEGREE_2.getInstitution(),
                                                                           DEGREE_2.getCompleted(),
                                                                           DEGREE_2.getDegreeType().getId(),
                                                                           DEGREE_2.getStartDate(),
                                                                           DEGREE_2.getEndDate(),
                                                                           DEGREE_2.getComment());

    public static final ExperienceTypeDto EXP_TYPE_1_DTO = new ExperienceTypeDto(EXP_TYPE_1_ID,
                                                                                 EXP_TYPE_1.getName(),
                                                                                 EXP_TYPE_1.getHighlyRelevantPoints(),
                                                                                 EXP_TYPE_1.getLimitedRelevantPoints(),
                                                                                 EXP_TYPE_1.getLittleRelevantPoints());

    public static final ExperienceTypeDto EXP_TYPE_1_INPUT = new ExperienceTypeDto(null,
                                                                                   EXP_TYPE_1.getName(),
                                                                                   EXP_TYPE_1.getHighlyRelevantPoints(),
                                                                                   EXP_TYPE_1
                                                                                           .getLimitedRelevantPoints(),
                                                                                   EXP_TYPE_1
                                                                                           .getLittleRelevantPoints());

    public static final ExperienceDto EXPERIENCE_1_DTO = new ExperienceDto(EXPERIENCE_1_ID,
                                                                           MEMBER_1_DTO,
                                                                           EXPERIENCE_1.getName(),
                                                                           EXPERIENCE_1.getEmployer(),
                                                                           EXPERIENCE_1.getPercent(),
                                                                           EXP_TYPE_1_DTO,
                                                                           EXPERIENCE_1.getComment(),
                                                                           EXPERIENCE_1.getStartDate(),
                                                                           EXPERIENCE_1.getEndDate());

    public static final ExperienceInputDto EXPERIENCE_1_INPUT = new ExperienceInputDto(EXPERIENCE_1.getMember().getId(),
                                                                                       EXPERIENCE_1.getName(),
                                                                                       EXPERIENCE_1.getEmployer(),
                                                                                       EXPERIENCE_1.getPercent(),
                                                                                       EXPERIENCE_1.getType().getId(),
                                                                                       EXPERIENCE_1.getComment(),
                                                                                       EXPERIENCE_1.getStartDate(),
                                                                                       EXPERIENCE_1.getEndDate());

    public static final ExperienceInputDto EXPERIENCE_2_INPUT = new ExperienceInputDto(EXPERIENCE_2.getMember().getId(),
                                                                                       EXPERIENCE_2.getName(),
                                                                                       EXPERIENCE_2.getEmployer(),
                                                                                       EXPERIENCE_2.getPercent(),
                                                                                       EXPERIENCE_2.getType().getId(),
                                                                                       EXPERIENCE_2.getComment(),
                                                                                       EXPERIENCE_2.getStartDate(),
                                                                                       EXPERIENCE_2.getEndDate());

    public static final MemberOverviewDto MEMBER_1_OVERVIEW_DTO = new MemberOverviewDto(new MemberOverviewMemberDto(MEMBER_1
            .getId(),
                                                                                                                    MEMBER_1
                                                                                                                            .getFirstName(),
                                                                                                                    MEMBER_1
                                                                                                                            .getLastName(),
                                                                                                                    MEMBER_1
                                                                                                                            .getEmploymentState(),
                                                                                                                    MEMBER_1
                                                                                                                            .getAbbreviation(),
                                                                                                                    MEMBER_1
                                                                                                                            .getDateOfHire(),
                                                                                                                    MEMBER_1
                                                                                                                            .getBirthDate(),
                                                                                                                    MEMBER_1
                                                                                                                            .getOrganisationUnit()
                                                                                                                            .getName()),
                                                                                        new MemberOverviewCvDto(List
                                                                                                .of(new MemberOverviewDegreeDto(DEGREE_1
                                                                                                        .getId(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getDegreeType()
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getStartDate(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewExperienceDto(EXPERIENCE_1
                                                                                                                                .getId(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getEmployer(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getType()
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getComment(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getStartDate(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getEndDate()),
                                                                                                                            new MemberOverviewExperienceDto(EXPERIENCE_2
                                                                                                                                    .getId(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getEmployer(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getType()
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getComment(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getStartDate(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewCertificateDto(CERTIFICATE_1
                                                                                                                                .getId(),
                                                                                                                                                             CERTIFICATE_1
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_1
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_1
                                                                                                                                                                     .getComment()),
                                                                                                                            new MemberOverviewCertificateDto(CERTIFICATE_4
                                                                                                                                    .getId(),
                                                                                                                                                             CERTIFICATE_4
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_4
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_4
                                                                                                                                                                     .getComment())),
                                                                                                                List
                                                                                                                        .of()));

    public static final MemberOverviewDto MEMBER_2_OVERVIEW_DTO = new MemberOverviewDto(new MemberOverviewMemberDto(MEMBER_2
            .getId(),
                                                                                                                    MEMBER_2
                                                                                                                            .getFirstName(),
                                                                                                                    MEMBER_2
                                                                                                                            .getLastName(),
                                                                                                                    MEMBER_2
                                                                                                                            .getEmploymentState(),
                                                                                                                    MEMBER_2
                                                                                                                            .getAbbreviation(),
                                                                                                                    MEMBER_2
                                                                                                                            .getDateOfHire(),
                                                                                                                    MEMBER_2
                                                                                                                            .getBirthDate(),
                                                                                                                    MEMBER_2
                                                                                                                            .getOrganisationUnit()
                                                                                                                            .getName()),
                                                                                        new MemberOverviewCvDto(List
                                                                                                .of(new MemberOverviewDegreeDto(DEGREE_2
                                                                                                        .getId(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getDegreeType()
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getStartDate(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewExperienceDto(EXPERIENCE_3
                                                                                                                                .getId(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getEmployer(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getType()
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getComment(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getStartDate(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewCertificateDto(CERTIFICATE_2
                                                                                                                                .getId(),
                                                                                                                                                             CERTIFICATE_2
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_2
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_2
                                                                                                                                                                     .getComment()),
                                                                                                                            new MemberOverviewCertificateDto(CERTIFICATE_3
                                                                                                                                    .getId(),
                                                                                                                                                             CERTIFICATE_3
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_3
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_3
                                                                                                                                                                     .getComment())),
                                                                                                                List
                                                                                                                        .of()));

    public static final MemberOverviewDto MEMBER_EMPTY_CV_DTO = new MemberOverviewDto(new MemberOverviewMemberDto(MEMBER_1
            .getId(),
                                                                                                                  MEMBER_1
                                                                                                                          .getFirstName(),
                                                                                                                  MEMBER_1
                                                                                                                          .getLastName(),
                                                                                                                  MEMBER_1
                                                                                                                          .getEmploymentState(),
                                                                                                                  MEMBER_1
                                                                                                                          .getAbbreviation(),
                                                                                                                  MEMBER_1
                                                                                                                          .getDateOfHire(),
                                                                                                                  MEMBER_1
                                                                                                                          .getBirthDate(),
                                                                                                                  MEMBER_1
                                                                                                                          .getOrganisationUnit()
                                                                                                                          .getName()),
                                                                                      new MemberOverviewCvDto(List.of(),
                                                                                                              List.of(),
                                                                                                              List.of(),
                                                                                                              List
                                                                                                                      .of()));

    public static final CalculationDto CALCULATION_DTO_1 = new CalculationDto(CALCULATION_1_ID,
                                                                              MEMBER_1_DTO,
                                                                              ROLE_2_DTO,
                                                                              CALCULATION_1.getState(),
                                                                              CALCULATION_1.getPublicationDate(),
                                                                              CALCULATION_1.getPublicizedBy());

    public static final CalculationInputDto CALCULATION_INPUT_DTO_1 = new CalculationInputDto(CALCULATION_1
            .getMember()
            .getId(), CALCULATION_1.getRole().getId(), CALCULATION_1.getState());

    public static final CalculationInputDto CALCULATION_INPUT_DTO_2 = new CalculationInputDto(CALCULATION_2
            .getMember()
            .getId(), CALCULATION_2.getRole().getId(), CALCULATION_2.getState());
}
