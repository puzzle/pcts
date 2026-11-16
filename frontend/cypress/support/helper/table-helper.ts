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

  setRowPercentage(row: JQuery<HTMLElement>, state: Relevancy, percentage: number) {
    if (state === Relevancy.NONE) {
      return this;
    }

    const stateIndexMap = {
      [Relevancy.STRONGLY]: 0,
      [Relevancy.NORMAL]: 1,
      [Relevancy.POORLY]: 2
    };

    cy.wrap(row)
      .next('.detail-row')
      .find('input[type="number"]')
      .eq(stateIndexMap[state])
      .clear()
      .type(percentage.toString());

    return this;
  }
}
