import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExperience } from './add-experience.component';

describe('AddExperience', () => {
  let component: AddExperience;
  let fixture: ComponentFixture<AddExperience>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddExperience]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddExperience);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
