import { Page } from './page';

class MemberDetailPage extends Page {
  visit(id: number) {
    cy.visit('/member/' + id);
  }

  editMemberButton() {
    return cy.getByTestId('edit-member-button');
  }
}

export default new MemberDetailPage();
