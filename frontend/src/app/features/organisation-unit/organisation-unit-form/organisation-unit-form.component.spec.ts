import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganisationUnitFormComponent } from './organisation-unit-form.component';

describe('OrganisationUnitFormComponent', () => {
  let component: OrganisationUnitFormComponent;
  let fixture: ComponentFixture<OrganisationUnitFormComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [OrganisationUnitFormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(OrganisationUnitFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
