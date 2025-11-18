import { Page } from './page';

class MemberDetailPage extends Page {
  visit(id: number) {
    cy.visit('/member/' + id);
  }

  editMemberButton() {
    return cy.getByTestId('edit-member-button');
  }

  memberDetailView() {
    return cy.getByTestId('member-detail-view');
  }
}

export default new MemberDetailPage();
