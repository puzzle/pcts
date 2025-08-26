package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.business.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/examples")
public class ExampleController {
    private final ExampleMapper mapper;
    private final ExampleService service;

    @Autowired
    public ExampleController(ExampleMapper mapper, ExampleService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExampleDto>> getExample() {
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @Operation(summary = "Get example by ID", description = "Find the example by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the example", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ExampleDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Can't find example", content = @Content) })
    @GetMapping("{id}")
    public ResponseEntity<ExampleDto> getExampleById(@PathVariable long id) {
        Example example = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(example));
    }

    @PostMapping
    public ResponseEntity<ExampleDto> createNew(@Valid @RequestBody ExampleDto dto) {
        Example newExample = service.create(mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(newExample));
    }
}
