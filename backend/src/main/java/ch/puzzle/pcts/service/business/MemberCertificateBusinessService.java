package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.service.persistence.MemberCertificatePersistenceService;
import ch.puzzle.pcts.service.validation.MemberCertificateValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberCertificateBusinessService {
    private final MemberCertificateValidationService validationService;
    private final MemberCertificatePersistenceService persistenceService;

    public MemberCertificateBusinessService(MemberCertificateValidationService validationService,
                                            MemberCertificatePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<MemberCertificate> getAll() {
        return persistenceService.getAll();
    }

    public MemberCertificate getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Member certificate with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public MemberCertificate create(MemberCertificate memberCertificate) {
        validationService.validateOnCreate(memberCertificate);
        return persistenceService.save(memberCertificate);
    }

    public MemberCertificate update(Long id, MemberCertificate memberCertificate) {
        validationService.validateOnUpdate(id, memberCertificate);
        memberCertificate.setId(id);
        return persistenceService.save(memberCertificate);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
