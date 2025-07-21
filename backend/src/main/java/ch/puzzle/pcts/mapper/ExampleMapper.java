package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.model.example.Example;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExampleMapper {

    public List<ExampleDto> toDto(List<Example> models){
        return models.stream().map(this::toDto).toList();
    }

    public List<Example> fromDto(List<ExampleDto> dtos){
        return  dtos.stream().map(this::fromDto).toList();
    }

    public ExampleDto toDto(Example model){
        return new ExampleDto(model.getId(), model.getText());
    }

    public Example fromDto(ExampleDto dto){
        return new Example(dto.id(), dto.text());
    }
}
