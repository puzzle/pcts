package ch.puzzle.pctsmigration.api;


import org.openapitools.client.ApiException;
import org.openapitools.client.api.MembersApi;
import org.openapitools.client.model.MemberDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MemberService {
    private final MembersApi membersApi;

    public MemberService(MembersApi membersApi) {
        this.membersApi = membersApi;
    }

    public List<MemberDto> getMembers() throws ApiException {
        return this.membersApi.getMember();
    }

    public Long getMemberIdBy(String abbreviation) {
        try {
            List<MemberDto> members = getMembers();

            for (MemberDto member : members) {
                if (Objects.equals(member.getAbbreviation(), abbreviation)) {
                    return member.getId();
                }
            }
        } catch (ApiException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
