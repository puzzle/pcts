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

  it('should accept the experienceType input', () => {
    fixture.componentRef.setInput('experienceType', 'Praktikum');
    fixture.detectChanges();
    expect(component.experienceType())
      .toBe('Praktikum');
  });

  describe('getExperienceBadgeClass', () => {
    it('should return "badge-internship" for "Praktikum"', () => {
      const result = component.getExperienceBadgeClass('Praktikum');
      expect(result)
        .toBe('badge-internship');
    });

    it('should return "badge-work-experience" for "Berufserfahrung"', () => {
      const result = component.getExperienceBadgeClass('Berufserfahrung');
      expect(result)
        .toBe('badge-work-experience');
    });

    it('should return "badge-work-part-time" for "Nebenerwerb"', () => {
      const result = component.getExperienceBadgeClass('Nebenerwerb');
      expect(result)
        .toBe('badge-work-part-time');
    });

    it('should return "badge-default" for unknown types', () => {
      const result = component.getExperienceBadgeClass('Unknown Type');
      expect(result)
        .toBe('badge-default');
    });

    it('should return "badge-default" for empty strings', () => {
      const result = component.getExperienceBadgeClass('');
      expect(result)
        .toBe('badge-default');
    });
  });
});
