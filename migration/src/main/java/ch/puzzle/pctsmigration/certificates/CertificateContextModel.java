package ch.puzzle.pctsmigration.certificates;

import org.openapitools.client.model.CertificateTypeDto;

import java.time.LocalDate;
import java.util.List;

public record CertificateContextModel(List<CertificateTypeDto> types, LocalDate currentDate) {}
