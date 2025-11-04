package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.repository.MemberCertificateRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberCertificatePersistenceService
        extends
            PersistenceBase<MemberCertificate, MemberCertificateRepository> {
    public MemberCertificatePersistenceService(MemberCertificateRepository repository) {
        super(repository);
    }
}
