import { TableData } from './table-data';

export class TableHelper {
  private readonly tableTestId: string;

  private constructor(tableTestId: string) {
    this.tableTestId = tableTestId;
  }

  static withTableTestId(tableTestId: string) {
    return new TableHelper(tableTestId);
  }

  getTableHTMLElement() {
    return cy.getByTestId(this.tableTestId);
  }

  tableRows() {
    return this.getTableHTMLElement()
      .findByTestId('generic-table-row');
  }

  getShowAllButton() {
    return this.getTableHTMLElement()
      .findByTestId('limit-list-button');
  }

  expectTableContains(tableData: TableData) {
    tableData.rows.forEach((row, rowIndex) => {
      row.cells.forEach((cell, cellIndex) => {
        this.getTableHTMLElement()
          .findByTestId('generic-table-row')
          .eq(rowIndex)
          .findByTestId('generic-table-cell')
          .eq(cellIndex)
          .should('include.text', cell);
      });
    });
    return this;
  }

  expectEmptyTable(emptyText = 'Keine Einträge') {
    this.getTableHTMLElement()
      .should('include.text', emptyText);
    return this;
  }

  expectLengthOfTable(length: number) {
    this.tableRows()
      .should('have.length', length);
    return this;
  }

  expectTableToBeExtendable() {
    this.getShowAllButton()
      .should('include.text', 'Alles anzeigen');
    return this;
  }

  expectTableToBeFoldable() {
    this.getShowAllButton()
      .should('include.text', 'Weniger anzeigen');
    return this;
  }

  toggleTable() {
    this.getShowAllButton()
      .click();
    return this;
  }
}
