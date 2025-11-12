package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.membercertificate.MemberCertificateDto;
import ch.puzzle.pcts.dto.membercertificate.MemberCertificateInputDto;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberCertificateMapper {
    MemberMapper memberMapper;
    MemberBusinessService memberBusinessService;
    CertificateTypeMapper certificateTypeMapper;
    CertificateTypeBusinessService certificateTypeBusinessService;

    MemberCertificateMapper(MemberMapper memberMapper, CertificateTypeMapper certificateTypeMapper,
                            MemberBusinessService memberBusinessService,
                            CertificateTypeBusinessService certificateTypeBusinessService) {
        this.memberMapper = memberMapper;
        this.memberBusinessService = memberBusinessService;
        this.certificateTypeMapper = certificateTypeMapper;
        this.certificateTypeBusinessService = certificateTypeBusinessService;
    }

    public List<MemberCertificateDto> toDto(List<MemberCertificate> models) {
        return models.stream().map(this::toDto).toList();
    }

    public List<MemberCertificate> fromDto(List<MemberCertificateInputDto> dtos) {
        return dtos.stream().map(this::fromDto).toList();
    }

    public MemberCertificateDto toDto(MemberCertificate model) {
        return new MemberCertificateDto(model.getId(),
                                        memberMapper.toDto(model.getMember()),
                                        certificateTypeMapper.toDto(model.getCertificate_type()),
                                        model.getCompleted_at(),
                                        model.getValid_until(),
                                        model.getComment());

    }

    public MemberCertificate fromDto(MemberCertificateInputDto dto) {
        return MemberCertificate.Builder
                .builder()
                .withMember(memberBusinessService.getById(dto.memberId()))
                .withCertificate(certificateTypeBusinessService.getById(dto.certificate_typeId()))
                .withCompleted_at(dto.completed_at())
                .withValid_until(dto.valid_until())
                .withComment(dto.comment())
                .build();
    }
}
