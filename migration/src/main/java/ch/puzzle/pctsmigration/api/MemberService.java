package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.List;
import java.util.Objects;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MembersApi;
import org.openapitools.client.model.MemberDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MembersApi membersApi;

    public MemberService(MembersApi membersApi) {
        this.membersApi = membersApi;
    }

    public List<MemberDto> getMembers() {
        try {
            return this.membersApi.getMember();
        } catch (ApiException e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), e.getMessage()));
        }
    }

    public Long getMemberIdBy(String abbreviation) {
        List<MemberDto> members = getMembers();

        for (MemberDto member : members) {
            if (Objects.equals(member.getAbbreviation(), abbreviation)) {
                return member.getId();
            }
        }
        throw new MigrationException(new Error(HttpStatusCode.valueOf(404),
                                               "Member with abbreviation " + abbreviation + " not found"));
    }
}
