package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.MEMBER;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberPersistenceService extends PersistenceBase<Member, MemberRepository> {
    public MemberPersistenceService(MemberRepository repository) {
        super(repository);
    }

    public Optional<Member> findByPreferredUsername(String preferredUsername) {
        return this.repository.findMemberByPreferredUsername(preferredUsername);
    }

    public Optional<Member> findByEmail(String email) {
        return this.repository.findMemberByEmailAndEmailIsNotNull(email);
    }

    public Optional<Member> findById(Long id) {
        return this.repository.findById(id);
    }

    public Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> {
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, Map.of());
            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });
    }

    @Override
    public String entityName() {
        return MEMBER;
    }
}
