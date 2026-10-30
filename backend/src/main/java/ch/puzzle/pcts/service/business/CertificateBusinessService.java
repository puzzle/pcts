package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.CertificateValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificateBusinessService extends BusinessBase<Certificate> {
    private final CertificatePersistenceService certificatePersistenceService;

    public CertificateBusinessService(CertificateValidationService validationService,
                                      CertificatePersistenceService persistenceService) {
        super(validationService, persistenceService);

        certificatePersistenceService = persistenceService;
    }

    public List<Certificate> getAll() {
        return persistenceService.getAll();
    }

    public List<Certificate> findAllByMember(Member member) {
        return certificatePersistenceService.findAllByMember(member);
    }

}
