import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExperienceComponent } from './add-experience.component';

describe('AddExperience', () => {
  let component: AddExperienceComponent;
  let fixture: ComponentFixture<AddExperienceComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddExperienceComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddExperienceComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
