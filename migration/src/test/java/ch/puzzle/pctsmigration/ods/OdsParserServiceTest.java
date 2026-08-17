// package ch.puzzle.pctsmigration.ods;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
// import java.io.ByteArrayOutputStream;
// import java.io.IOException;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
// import org.odftoolkit.odfdom.doc.table.OdfTable;
// import org.springframework.mock.web.MockMultipartFile;
//
// class OdsParserServiceTest {
//
// private OdsParserService odsParserService;
//
// @BeforeEach
// void setUp() {
// odsParserService = new OdsParserService();
// }
//
// @Test
// @DisplayName("Should throw IllegalArgumentException when the uploaded file is
// empty")
// void parse_whenFileIsEmpty_throwsIllegalArgumentException() {
// MockMultipartFile emptyFile = new MockMultipartFile("file",
// "empty.ods",
// "application/vnd.oasis.opendocument.spreadsheet",
// new byte[0]);
//
// assertThatThrownBy(() -> odsParserService.parse(emptyFile))
// .isInstanceOf(IllegalArgumentException.class)
// .hasMessage("Uploaded file is empty");
// }
//
// @Test
// @DisplayName("Should throw IOException when file content is not a valid ODS
// document")
// void parse_whenFileIsInvalid_throwsIOException() {
// MockMultipartFile invalidFile = new MockMultipartFile("file",
// "corrupt.ods",
// "application/vnd.oasis.opendocument.spreadsheet",
// "not-an-ods-file".getBytes());
//
// assertThatThrownBy(() -> odsParserService.parse(invalidFile))
// .isInstanceOf(IOException.class)
// .hasMessageContaining("Failed to parse ODS file");
// }
//
// @Test
// @DisplayName("Should parse valid ODS sheet and trim trailing empty cells and
// empty rows")
// void parse_whenValidOds_extractsRowsAndTrimsTrailingEmptyCells() throws
// Exception {
// byte[] odsBytes = createSampleOdsBytes(new String[][]{ { "Header 1", "Header
// 2", "", "" },
// { "Value 1", "Value 2", "Value 3", "" }, { "", "", "", "" } // Empty row
// should be discarded
// });
//
// MockMultipartFile file = new MockMultipartFile("file",
// "test.ods",
// "application/vnd.oasis.opendocument.spreadsheet",
// odsBytes);
//
// OdsParseResult result = odsParserService.parse(file);
//
// assertThat(result.sheets()).hasSize(1);
// OdsParseResult.Sheet sheet = result.sheets().getFirst();
// assertThat(sheet.name()).isEqualTo("TestSheet");
//
// assertThat(sheet.rows()).hasSize(2);
//
// assertThat(sheet.rows().get(0)).containsExactly("Header 1", "Header 2");
// assertThat(sheet.rows().get(1)).containsExactly("Value 1", "Value 2", "Value
// 3");
// }
//
// @Test
// @DisplayName("Should format sheets with data as a Markdown table")
// void toPromptText_withValidRows_generatesMarkdownTable() {
// OdsParseResult.Sheet sheet = new OdsParseResult.Sheet("Users",
// List
// .of(List.of("ID", "Name", "Role"),
// List.of("1", "Alice", "Admin"),
// List.of("2", "Bob", "User")));
// OdsParseResult result = new OdsParseResult(List.of(sheet));
//
// String markdown = odsParserService.toPromptText(result);
//
// String expected = """
// ## Sheet: Users
//
// | ID | Name | Role |\s
// | --- | --- | --- |\s
// | 1 | Alice | Admin |\s
// | 2 | Bob | User |\s
//
// """;
//
// assertThat(markdown).isEqualToNormalizingNewlines(expected);
// }
//
// @Test
// @DisplayName("Should output placeholder text when sheet has no rows")
// void toPromptText_whenSheetIsEmpty_generatesEmptySheetMessage() {
// OdsParseResult.Sheet sheet = new OdsParseResult.Sheet("EmptySheet",
// List.of());
// OdsParseResult result = new OdsParseResult(List.of(sheet));
//
// String markdown = odsParserService.toPromptText(result);
//
// assertThat(markdown).contains("## Sheet: EmptySheet").contains("_(empty
// sheet)_");
// }
//
// @Test
// @DisplayName("Should escape pipe characters present inside cell text")
// void toPromptText_whenCellsContainPipes_escapesPipeCharacters() {
// OdsParseResult.Sheet sheet = new OdsParseResult.Sheet("Config",
// List
// .of(List.of("Key", "Pattern"),
// List.of("regex", "foo|bar|baz")));
// OdsParseResult result = new OdsParseResult(List.of(sheet));
//
// String markdown = odsParserService.toPromptText(result);
//
// assertThat(markdown).contains("| regex | foo\\|bar\\|baz | ");
// }
//
// @Test
// @DisplayName("Should format multiple sheets sequentially")
// void toPromptText_withMultipleSheets_formatsAllSheets() {
// OdsParseResult.Sheet sheet1 = new OdsParseResult.Sheet("Sheet1",
// List.of(List.of("A")));
// OdsParseResult.Sheet sheet2 = new OdsParseResult.Sheet("Sheet2", List.of());
// OdsParseResult result = new OdsParseResult(List.of(sheet1, sheet2));
//
// String markdown = odsParserService.toPromptText(result);
//
// assertThat(markdown).contains("## Sheet: Sheet1").contains("## Sheet:
// Sheet2").contains("_(empty sheet)_");
// }
//
// private byte[] createSampleOdsBytes(String[][] data) throws Exception {
// try (OdfSpreadsheetDocument doc =
// OdfSpreadsheetDocument.newSpreadsheetDocument();
// ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//
// OdfTable table = doc.getTableList().getFirst();
// table.setTableName("TestSheet");
//
// for (int r = 0; r < data.length; r++) {
// for (int c = 0; c < data[r].length; c++) {
// table.getCellByPosition(c, r).setStringValue(data[r][c]);
// }
// }
//
// doc.save(out);
// return out.toByteArray();
// }
// }
// }