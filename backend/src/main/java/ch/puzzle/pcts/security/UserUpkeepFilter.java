package ch.puzzle.pcts.security;

import ch.puzzle.pcts.service.JwtService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserUpkeepFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(UserUpkeepFilter.class);

    private final JwtService jwtService;
    private final MemberBusinessService memberBusinessService;

    public UserUpkeepFilter(JwtService jwtService, MemberBusinessService memberBusinessService) {
        this.jwtService = jwtService;
        this.memberBusinessService = memberBusinessService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Reached UserUpkeepFilter from route {}", request.getRequestURL().toString()); // TODO: change me to
                                                                                                // debug

        if (!jwtService.isLoggedIn()) {
            filterChain.doFilter(request, response);
            return;
        }

        var m = memberBusinessService.findOrCreateLoggedInMember();

        filterChain.doFilter(request, response);
    }

}
