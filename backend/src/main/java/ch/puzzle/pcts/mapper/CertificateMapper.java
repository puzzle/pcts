package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.mode.certificate.Certificate;
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
                                  certificateTypeMapper.toDto(model.getCertificate_type()),
                                  model.getCompleted_at(),
                                  model.getValid_until(),
                                  model.getComment());

    }

    public Certificate fromDto(CertificateInputDto dto) {
        return Certificate.Builder
                .builder()
                .withMember(memberBusinessService.getById(dto.memberId()))
                .withCertificate(certificateTypeBusinessService.getById(dto.certificate_typeId()))
                .withCompleted_at(dto.completed_at())
                .withValid_until(dto.valid_until())
                .withComment(dto.comment())
                .build();
    }
}
