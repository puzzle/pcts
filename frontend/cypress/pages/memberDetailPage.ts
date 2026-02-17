import { Page } from './page';

class MemberDetailPage extends Page {
  visit(id: number) {
    cy.visit(`/member/${id}`);
    return this;
  }

  switchTab(tabLabel: string) {
    cy.getByTestId('cv-tab')
      .find('.mdc-tab')
      .contains(tabLabel)
      .click();

    return this;
  }

  openModalButton(mode: string, modelName: string) {
    return cy.getByTestId(`${mode}-${modelName}-button`);
  }


  editMemberButton() {
    return cy.getByTestId('edit-member-button');
  }

  memberDetailData() {
    return cy.getByTestId('member-detail-data');
  }

  memberName() {
    return cy.getByTestId('member-name');
  }

  memberDetailTitle() {
    return cy.getByTestId('member-detail-title');
  }

  memberCvTab() {
    return cy.getByTestId('cv-tab');
  }

  rolePointBox(roleName: string) {
    return cy.getByTestId(`role-points-${roleName}`);
  }

  assertRoleWithPoints(role: string, points: string) {
    this.rolePointBox(role)
      .should('include.text', role)
      .and('include.text', points);
  }
}

export default new MemberDetailPage();
