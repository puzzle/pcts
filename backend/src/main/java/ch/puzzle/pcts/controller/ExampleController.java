package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.CreateExampleDto;
import ch.puzzle.pcts.dto.ExampleDto;
import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.model.Example;
import ch.puzzle.pcts.service.business.ExampleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/example")
public class ExampleController {
    private final ExampleMapper mapper;
    private final ExampleService service;

    @Autowired
    public ExampleController(ExampleMapper mapper, ExampleService service){
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ExampleDto>> getExample(){
        return ResponseEntity.ok(mapper.toDto(service.getAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<ExampleDto> getExampleById(@PathVariable long id){
        Example example = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(example));
    }

    @PostMapping
    public ResponseEntity<ExampleDto> createNew(@Valid @RequestBody CreateExampleDto dto){
        Example newExample = service.create(dto);
        return ResponseEntity.ok(mapper.toDto(newExample));
    }
}
