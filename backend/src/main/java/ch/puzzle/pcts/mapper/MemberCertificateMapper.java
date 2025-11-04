package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.membercertificate.MemberCertificateDto;
import ch.puzzle.pcts.dto.membercertificate.MemberCertificateInputDto;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberCertificateMapper {
    MemberMapper memberMapper;
    MemberBusinessService memberBusinessService;
    CertificateMapper certificateMapper;
    CertificateBusinessService certificateBusinessService;

    MemberCertificateMapper(MemberMapper memberMapper, CertificateMapper certificateMapper,
                            MemberBusinessService memberBusinessService,
                            CertificateBusinessService certificateBusinessService) {
        this.memberMapper = memberMapper;
        this.memberBusinessService = memberBusinessService;
        this.certificateMapper = certificateMapper;
        this.certificateBusinessService = certificateBusinessService;
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
                                        certificateMapper.toDto(model.getCertificate()),
                                        model.getCompleted_at(),
                                        model.getValid_until(),
                                        model.getComment());

    }

    public MemberCertificate fromDto(MemberCertificateInputDto dto) {
        return MemberCertificate.Builder
                .builder()
                .withMember(memberBusinessService.getById(dto.memberId()))
                .withCertificate(certificateBusinessService.getById(dto.certificateId()))
                .withCompleted_at(dto.completed_at())
                .withValid_until(dto.valid_until())
                .withComment(dto.comment())
                .build();
    }
}
