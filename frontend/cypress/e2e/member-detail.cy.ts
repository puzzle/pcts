import memberDetailPage from '../pages/memberDetailPage';

describe('MemberOverviewComponent', () => {
  beforeEach(() => {
    // Equals to 'Lena Müller'
    memberDetailPage.visit(1);
  });

  it('should display everything about the selected member', () => {
    memberDetailPage.memberDetailView()
      .should('include.text', 'Lena Müller')
      .should('include.text', 'Personalien')
      .should('include.text', 'Kürzel: LM')
      .should('include.text', 'Geburtsdatum:10.08.1999')
      .should('include.text', 'Anstellungsdatum: 15.07.2021')
      .should('include.text', 'Anstellungsstatus:Member')
      .should('include.text', 'Division:/zh');
  });

  it('should navigate to edit page', () => {
    memberDetailPage.editMemberButton()
      .click();

    cy.url()
      .should('include', '/member/1/edit');
  });

  it('should display alternative message when dateOfHire and abbreviation is not set', () => {
    // Equals to 'Sophie Keller'
    memberDetailPage.visit(3);

    memberDetailPage.memberDetailView()
      .should('include.text', 'Sophie Keller')
      .should('include.text', 'Anstellungsdatum: -')
      .should('include.text', 'Kürzel: -');
  });
});
