import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ExperienceTypePillComponent } from './experience-type-pill.component';

describe('ExperienceTypePillComponent', () => {
  let component: ExperienceTypePillComponent;
  let fixture: ComponentFixture<ExperienceTypePillComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ExperienceTypePillComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExperienceTypePillComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('experienceType', 'default');
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  const experienceInputs = [
    'default',
    'Praktikum',
    'Berufserfahrung',
    'Nebenerwerb',
    'Unknown Type',
    ''
  ];
  experienceInputs.forEach((input) => {
    it(`should accept experienceType input "${input}"`, () => {
      fixture.componentRef.setInput('experienceType', input);
      fixture.detectChanges();

      expect(component.experienceType())
        .toBe(input);
    });
  });

  const badgeTestCases: { input: string;
    expected: string; }[] = [
    { input: 'Praktikum',
      expected: 'badge-internship' },
    { input: 'Berufserfahrung',
      expected: 'badge-work-experience' },
    { input: 'Nebenerwerb',
      expected: 'badge-work-part-time' },
    { input: 'Unknown Type',
      expected: 'badge-default' },
    { input: '',
      expected: 'badge-default' }
  ];

  badgeTestCases.forEach(({ input, expected }) => {
    it(`getExperienceBadgeClass("${input}") should return "${expected}"`, () => {
      fixture.componentRef.setInput('experienceType', input);
      const result = component.experienceBadgeClass();
      expect(result)
        .toBe(expected);
    });
  });
});
