import { Page } from './page';

class MemberDetailPage extends Page {
  visit(id: number) {
    cy.visit('/member/' + id);
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
}

export default new MemberDetailPage();
