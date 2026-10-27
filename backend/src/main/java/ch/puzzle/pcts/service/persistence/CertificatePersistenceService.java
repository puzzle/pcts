package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CertificatePersistenceService extends PersistenceBase<Certificate, CertificateRepository> {

    public CertificatePersistenceService(CertificateRepository repository) {
        super(repository);
    }

    @Override
    public String entityName() {
        return CERTIFICATE;
    }

    public List<Certificate> findAllByMember(Member member) {
        return repository.findAllByMember(member);
    }
}
