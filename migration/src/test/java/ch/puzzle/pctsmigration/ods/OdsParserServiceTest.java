package ch.puzzle.pctsmigration.ods;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.exception.MigrationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class OdsParserServiceTest {

    private OdsParserService odsParserService;
    private static List<String> tableNames;

    @BeforeEach
    void setUp() {
        odsParserService = new OdsParserService();
        tableNames = List.of("Zertifikat", "Zertifikate");
    }

    @Test
    void testParseToPromptText_EmptyFileThrowsException() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);

        MigrationException exception = assertThrows(MigrationException.class,
                                                    () -> odsParserService.parseToPromptText(mockFile, tableNames));

        assertTrue(exception.getError().message().contains("Uploaded file is empty"));
    }

    @Test
    void testParseToPromptText_IOExceptionThrowsMigrationException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getInputStream()).thenThrow(new IOException("Stream error"));

        MigrationException exception = assertThrows(MigrationException.class,
                                                    () -> odsParserService.parseToPromptText(mockFile, tableNames));

        assertTrue(exception.getError().message().contains("Failed to parse ODS file: Stream error"));
    }

    @Test
    void testParseToPromptText_NoValidSheetsThrowsException() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        OdfSpreadsheetDocument mockDoc = mock(OdfSpreadsheetDocument.class);
        OdfTable uppercaseTable = mock(OdfTable.class);
        when(uppercaseTable.getTableName()).thenReturn("NoValidSheets");
        when(mockDoc.getSpreadsheetTables()).thenReturn(Collections.singletonList(uppercaseTable));

        try (MockedStatic<OdfSpreadsheetDocument> mockedStatic = mockStatic(OdfSpreadsheetDocument.class)) {
            mockedStatic.when(() -> OdfSpreadsheetDocument.loadDocument(any(InputStream.class))).thenReturn(mockDoc);

            MigrationException exception = assertThrows(MigrationException.class,
                                                        () -> odsParserService.parseToPromptText(mockFile, tableNames));

            assertTrue(exception.getError().message().contains("No valid sheets found"));
        }
    }

    @Test
    void testParseToPromptText_ValidDataParsesToMarkdown() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        OdfSpreadsheetDocument mockDoc = mock(OdfSpreadsheetDocument.class);

        OdfTable mockTable = mock(OdfTable.class);
        when(mockTable.getTableName()).thenReturn("Zertifikat");
        when(mockTable.getRowCount()).thenReturn(2);
        when(mockTable.getColumnCount()).thenReturn(2);
        when(mockDoc.getSpreadsheetTables()).thenReturn(List.of(mockTable));

        mockRow(mockTable, 0, "Header1", "Header2|");
        mockRow(mockTable, 1, "Value1", "Value2");

        try (MockedStatic<OdfSpreadsheetDocument> mockedStatic = mockStatic(OdfSpreadsheetDocument.class)) {
            mockedStatic.when(() -> OdfSpreadsheetDocument.loadDocument(any(InputStream.class))).thenReturn(mockDoc);

            String markdown = odsParserService.parseToPromptText(mockFile, tableNames);

            assertNotNull(markdown);
            assertTrue(markdown.contains("## Sheet: Zertifikat"));
            assertTrue(markdown.contains("| Header1 | Header2\\| |"));
            assertTrue(markdown.contains("| --- | --- |"));
            assertTrue(markdown.contains("| Value1 | Value2 |"));
        }
    }

    @Test
    void testParseToPromptText_RemovesTrailingEmptyCells() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        OdfSpreadsheetDocument mockDoc = mock(OdfSpreadsheetDocument.class);
        OdfTable mockTable = mock(OdfTable.class);
        when(mockTable.getTableName()).thenReturn("Zertifikat");
        when(mockTable.getRowCount()).thenReturn(1);
        when(mockTable.getColumnCount()).thenReturn(3);
        when(mockDoc.getSpreadsheetTables()).thenReturn(List.of(mockTable));

        mockRow(mockTable, 0, "Data1", "Data2", "");

        try (MockedStatic<OdfSpreadsheetDocument> mockedStatic = mockStatic(OdfSpreadsheetDocument.class)) {
            mockedStatic.when(() -> OdfSpreadsheetDocument.loadDocument(any(InputStream.class))).thenReturn(mockDoc);

            String markdown = odsParserService.parseToPromptText(mockFile, tableNames);

            assertTrue(markdown.contains("| Data1 | Data2 |"));
            assertFalse(markdown.contains("--- | --- | --- |"));
            assertTrue(markdown.contains("--- | --- |"));
        }
    }

    private void mockRow(OdfTable mockTable, int rowIndex, String... cellValues) {
        OdfTableRow mockRow = mock(OdfTableRow.class);
        when(mockTable.getRowByIndex(rowIndex)).thenReturn(mockRow);

        for (int c = 0; c < cellValues.length; c++) {
            OdfTableCell mockCell = mock(OdfTableCell.class);
            when(mockRow.getCellByIndex(c)).thenReturn(mockCell);
            when(mockCell.getDisplayText()).thenReturn(cellValues[c]);
        }
    }
}