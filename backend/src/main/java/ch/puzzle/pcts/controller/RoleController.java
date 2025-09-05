package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.mapper.RoleMapper;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleMapper mapper;
    private final RoleBusinessService service;

    @Autowired
    public RoleController(RoleMapper mapper, RoleBusinessService service) {
        this.mapper = mapper;
        this.service = service;
    }
}
