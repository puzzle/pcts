package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {
    MemberMapper memberMapper;
    MemberBusinessService memberBusinessService;
    CertificateTypeMapper certificateTypeMapper;
    CertificateTypeBusinessService certificateTypeBusinessService;

    CertificateMapper(MemberMapper memberMapper, CertificateTypeMapper certificateTypeMapper,
                      MemberBusinessService memberBusinessService,
                      CertificateTypeBusinessService certificateTypeBusinessService) {
        this.memberMapper = memberMapper;
        this.memberBusinessService = memberBusinessService;
        this.certificateTypeMapper = certificateTypeMapper;
        this.certificateTypeBusinessService = certificateTypeBusinessService;
    }

    public List<CertificateDto> toDto(List<Certificate> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<Certificate> fromDto(List<CertificateInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public CertificateDto toDto(Certificate model) {
        return new CertificateDto(model.getId(),
                                  memberMapper.toDto(model.getMember()),
                                  certificateTypeMapper.toDto(model.getCertificateType()),
                                  model.getCompletedAt(),
                                  model.getValidUntil(),
                                  model.getComment());

    }

    public Certificate fromDto(CertificateInputDto dto) {
        return Certificate.Builder
                .builder()
                .withMember(memberBusinessService.getReferenceById(dto.memberId()))
                .withCertificateType(certificateTypeBusinessService.getReferenceById(dto.certificateTypeId()))
                .withCompletedAt(dto.completedAt())
                .withValidUntil(dto.validUntil())
                .withComment(dto.comment())
                .build();
    }
}
