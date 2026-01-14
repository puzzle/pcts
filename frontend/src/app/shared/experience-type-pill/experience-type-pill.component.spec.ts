import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExperienceTypePillComponent } from './experience-type-pill.component';

describe('ExperienceTypePill', () => {
  let component: ExperienceTypePillComponent;
  let fixture: ComponentFixture<ExperienceTypePillComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ExperienceTypePillComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ExperienceTypePillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
