package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OdsParserService {

    private final OdsSheetExtractor sheetExtractor;
    private final SheetSplitter sheetSplitter;
    private final MarkdownSheetFormatter markdownFormatter;

    public OdsParserService(OdsSheetExtractor sheetExtractor, SheetSplitter sheetSplitter,
                            MarkdownSheetFormatter markdownFormatter) {
        this.sheetExtractor = sheetExtractor;
        this.sheetSplitter = sheetSplitter;
        this.markdownFormatter = markdownFormatter;
    }

    public List<String> parseToPromptText(MultipartFile file, OdsParseConfig config) {
        if (file == null || file.isEmpty()) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST, "Uploaded file is empty"));
        }

        try (OdfSpreadsheetDocument doc = OdfSpreadsheetDocument.loadDocument(file.getInputStream())) {
            List<Sheet> sheets = sheetExtractor.extractSheets(doc, config);
            List<List<Sheet>> parts = partitionSheets(sheets, config);

            return parts.stream().map(this::trimSheets).map(markdownFormatter::format).toList();
        } catch (Exception e) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST,
                                                   "Failed to parse ODS file: " + e.getMessage()));
        }
    }

    private List<List<Sheet>> partitionSheets(List<Sheet> sheets, OdsParseConfig config) {
        if (!config.isSplittingEnabled()) {
            return List.of(sheets);
        }
        return sheetSplitter.split(mapSheetsToSplitMarkers(sheets, config));
    }

    private Map<Sheet, List<String>> mapSheetsToSplitMarkers(List<Sheet> sheets, OdsParseConfig config) {
        Map<Sheet, List<String>> sheetsWithMarkers = new LinkedHashMap<>(sheets.size());
        for (Sheet sheet : sheets) {
            sheetsWithMarkers.put(sheet, config.splitMarkersFor(sheet.name()));
        }
        return sheetsWithMarkers;
    }

    private List<Sheet> trimSheets(List<Sheet> sheets) {
        return sheets.stream().map(Sheet::trimTrailingEmptyColumns).toList();
    }
}