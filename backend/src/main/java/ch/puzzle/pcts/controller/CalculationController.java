package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.mapper.CalculationMapper;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.business.CalculationBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/calculations")
@Tag(name = "calculations", description = "Manage the calculations which are associated with a member")
public class CalculationController {
    private final CalculationBusinessService businessService;
    private final CalculationMapper mapper;

    public CalculationController(CalculationBusinessService businessService, CalculationMapper mapper) {
        this.businessService = businessService;
        this.mapper = mapper;
    }

    @Operation(summary = "Get a calculation by ID")
    @ApiResponse(responseCode = "200", description = "A single calculation.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CalculationDto.class)) })
    @GetMapping("{calculationId}")
    public ResponseEntity<CalculationDto> getCalculation(@Parameter(description = "ID of the calculation to retrieve.", required = true)
    @PathVariable Long calculationId) {
        Calculation calculation = businessService.getById(calculationId);
        return ResponseEntity.ok(mapper.toDto(calculation));
    }

    @Operation(summary = "Create a new calculation")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The calculation object to be created.", required = true)
    @ApiResponse(responseCode = "201", description = "Calculation created successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CalculationDto.class)) })
    @PostMapping
    public ResponseEntity<CalculationDto> createCalculation(@RequestBody CalculationInputDto dto) {
        Calculation newCalculation = businessService.create(mapper.fromDto(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(newCalculation));
    }

    @Operation(summary = "Update a calculation")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The updated calculation data.", required = true)
    @ApiResponse(responseCode = "200", description = "Calculation updated successfully.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = CalculationInputDto.class)) })
    @PutMapping("{calculationId}")
    public ResponseEntity<CalculationDto> updateCalculation(@Parameter(description = "ID of the calculation to update.", required = true)
    @PathVariable Long calculationId, @RequestBody CalculationInputDto dto) {
        Calculation updatedCalculation = businessService.update(calculationId, mapper.fromDto(dto));
        return ResponseEntity.ok(mapper.toDto(updatedCalculation));
    }
}
