package ch.puzzle.pctsmigration.api;

import java.util.List;
import java.util.Objects;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MembersApi;
import org.openapitools.client.model.MemberDto;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MembersApi membersApi;

    public MemberService(MembersApi membersApi) {
        this.membersApi = membersApi;
    }

    public List<MemberDto> getMembers() throws ApiException {
        return this.membersApi.getMember();
    }

    public Long getMemberIdBy(String abbreviation) throws ApiException {
        List<MemberDto> members = getMembers();

        for (MemberDto member : members) {
            if (Objects.equals(member.getAbbreviation(), abbreviation)) {
                return member.getId();
            }
        }
        return null;
    }
}
