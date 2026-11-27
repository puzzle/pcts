import { TableData } from './table-data';
import { Relevancy } from '../../../src/app/features/calculations/relevancy.enum';

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

  getTableRowBy(index: number, text: string) {
    return this.tableRows()
      .eq(index)
      .should('contain.text', text);
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

  toggleShowAll() {
    this.getShowAllButton()
      .click();
    return this;
  }

  selectRowRelevancy(row: JQuery<HTMLElement>, state: Relevancy) {
    if (state === Relevancy.NONE) {
      return;
    }

    const stateIndexMap = {
      [Relevancy.STRONGLY]: 0,
      [Relevancy.NORMAL]: 1,
      [Relevancy.POORLY]: 2
    };

    cy.wrap(row)
      .find('mat-radio-button')
      .eq(stateIndexMap[state])
      .find('input')
      .click({ force: true });
  }

  expectRadioRowsIs(row: JQuery<HTMLElement>, expected: string) {
    cy.wrap(row)
      .find('mat-radio-button input')
      .should('be.' + expected);
  }

  expectCheckboxRowIs(row: JQuery<HTMLElement>, expected: string) {
    cy.wrap(row)
      .find('mat-checkbox input, input[matInput]')
      .should('be.' + expected);
  }

  expandRow(row: JQuery<HTMLElement>) {
    cy.wrap(row)
      .findByTestId('expand-row')
      .click();

    return this;
  }

  setRowCheckboxRelevancy(row: JQuery<HTMLElement>, state: Relevancy, checkState = true) {
    if (state === Relevancy.NONE) {
      return this;
    }

    const stateIdMap = {
      [Relevancy.STRONGLY]: '#highly',
      [Relevancy.NORMAL]: '#limited',
      [Relevancy.POORLY]: '#little'
    };

    const checkboxInput = cy.wrap(row)
      .find(`mat-checkbox${stateIdMap[state]}`)
      .find('input');

    if (checkState) {
      checkboxInput.check({ force: true });
    } else {
      checkboxInput.uncheck({ force: true });
    }

    return this;
  }

  getPercentageInputField(row: JQuery<HTMLElement>, state: Relevancy) {
    if (state === Relevancy.NONE) {
      throw Error;
    }

    const stateIndexMap = {
      [Relevancy.STRONGLY]: 0,
      [Relevancy.NORMAL]: 1,
      [Relevancy.POORLY]: 2
    };

    return cy.wrap(row)
      .next('.detail-row')
      .find('input[type="number"]')
      .eq(stateIndexMap[state]);
  }

  setRowPercentage(row: JQuery<HTMLElement>, state: Relevancy, percentage: number) {
    this.getPercentageInputField(row, state)
      .clear()
      .type(percentage.toString());

    return this;
  }

  expectWarning(row: JQuery<HTMLElement>) {
    this.getWarningForRow(row)
      .should('be.visible');

    return this;
  }

  expectNoWarning(row: JQuery<HTMLElement>) {
    this.getWarningForRow(row)
      .should('not.exist');

    return this;
  }

  private getWarningForRow(row: JQuery<HTMLElement>) {
    return cy.wrap(row)
      .contains('mat-icon', 'warning');
  }

  expectTableNotFound() {
    cy.get('body')
      .findByTestId(this.tableTestId)
      .should('not.exist');
  }

  expectRadioTableRowsToBe(toggled: string) {
    this.tableRows()
      .each(($row) => {
        this.expectRadioRowsIs($row, toggled);
      });
  }

  expectCheckboxTableRowsToBe(toggled: string) {
    this.tableRows()
      .each(($row) => {
        this.expectCheckboxRowIs($row, toggled);
      });
  }
}
