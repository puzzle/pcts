import * as users from '../fixtures/users.json';
import OverviewPage from '../pages/overviewPage';
import MemberDetailPage from '../pages/memberDetailPage';
import FormPage from '../pages/formPage';

import CvMemberPage from '../pages/memberDetailPage';

const user = users.member;

describe('Non-Admin (Member) Permissions', () => {
  beforeEach(() => {
    cy.loginAsUser(user);
  });

  [{
    name: 'from overview',
    action: () => OverviewPage.visit()
  },
  {
    name: 'from other member page',
    action: () => MemberDetailPage.visit(users.gl.databaseId)
  },
  {
    name: 'from add page directly',
    action: () => FormPage.visitAdd('member')
  }].forEach(({ name, action }) => {
    it(`should redirect to detail view ${name}`, () => {
      action();

      cy.url()
        .should('include', `/member/${user.databaseId}`);
    });
  });

  it('should not show the "Add Member" button on overview', () => {
    MemberDetailPage.visit(user.databaseId);

    OverviewPage.createMemberButton()
      .should('not.exist');
  });

  it('should not show "Edit" button on member detail page', () => {
    MemberDetailPage.visit(user.databaseId);

    MemberDetailPage.memberDetailTitle()
      .should('be.visible')
      .should('include.text', 'Personalien');

    MemberDetailPage.editMemberButton()
      .should('not.exist');

    MemberDetailPage.memberCvTab();
  });

  const tableData: string[] = [
    'degree',
    'experience',
    'certificate',
    'leadership-experience'
  ];

  tableData.forEach((tableName) => {
    it(`should not show "Add" button on member detail page for table ${tableName}`, () => {
      MemberDetailPage.visit(user.databaseId);
      CvMemberPage.memberCvTab()
        .getByTestId('add-member-button')
        .should('not.exist');
    });
  });
});
