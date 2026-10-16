import memberDetailPage from '../pages/memberDetailPage';
import * as users from '../fixtures/users.json';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    cy.loginAsUser(users.gl);
  });

  describe('member without optional data', () => {
    beforeEach(() => {
      // Equals to 'Sophie Keller'
      memberDetailPage.visit(3);
    });

    it('should display alternative message when dateOfHire, abbreviation and division is not set', () => {
      memberDetailPage.memberName()
        .should('include.text', 'Sophie Keller');

      memberDetailPage.memberDetailData()
        .should('include.text', 'Anstellungsdatum: -')
        .should('include.text', 'Kürzel: -')
        .should('include.text', 'Division: -');
    });
  });

  describe('member with all data', () => {
    beforeEach(() => {
      // Equals to 'Lena Müller'
      memberDetailPage.visit(1);
    });

    it('should display everything about the selected member', () => {
      memberDetailPage.memberName()
        .should('include.text', 'Lena Müller');

      memberDetailPage.memberDetailTitle()
        .should('include.text', 'Personalien');

      memberDetailPage.memberDetailData()
        .should('contain.text', 'Kürzel: LM')
        .should('contain.text', 'Geburtsdatum:10.08.1999')
        .should('contain.text', 'Anstellungsdatum: 15.07.2021')
        .should('contain.text', 'Anstellungsstatus:Member')
        .should('contain.text', 'Division: /zh');
    });

    it('should navigate to edit page', () => {
      memberDetailPage.editMemberButton()
        .click();

      cy.url()
        .should('include', '/member/1/edit');
    });

    it('should display cv tab', () => {
      memberDetailPage.memberCvTab()
        .should('include.text', 'CV')
        .should('include.text', 'Ausbildung')
        .should('include.text', 'Berufs- und Lebenserfahrung')
        .should('include.text', 'Zertifikate')
        .should('include.text', 'Führungserfahrung');
    });

    it('should display right role tabs', () => {
      memberDetailPage.memberCvTab()
        .should('include.text', 'CV')
        .should('include.text', 'Administrator')
        .should('include.text', 'Intern');
    });

    it('should display role and there points', () => {
      memberDetailPage.assertRoleWithPoints('Administrator', '7.50');
      memberDetailPage.assertRoleWithPoints('Intern', '4.55');
    });
  });
});
