package ch.puzzle.pctsmigration.service.pcts;


import org.openapitools.client.ApiException;
import org.openapitools.client.api.MembersApi;
import org.openapitools.client.model.MemberDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MembersApi membersApi;

    public MemberService(MembersApi membersApi) {
        this.membersApi = membersApi;
    }

    public List<MemberDto> getMembers() throws ApiException {
        return this.membersApi.getMember();
    }
}
