package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.List;
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
            return sheetSplitter.split(sheets, config.splitMarkers()).stream().map(markdownFormatter::format).toList();
        } catch (MigrationException e) {
            throw e;
        } catch (Exception e) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST,
                                                   "Failed to parse ODS file: " + e.getMessage()));
        }
    }
}