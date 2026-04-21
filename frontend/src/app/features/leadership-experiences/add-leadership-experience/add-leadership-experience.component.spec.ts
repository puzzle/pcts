import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLeadershipExperienceComponent } from './add-leadership-experience.component';

describe('AddLeadershipExperienceComponent', () => {
  let component: AddLeadershipExperienceComponent;
  let fixture: ComponentFixture<AddLeadershipExperienceComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AddLeadershipExperienceComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddLeadershipExperienceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
