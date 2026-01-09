import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericCvContentComponent } from './generic-cv-content.component';

describe('GenericCvContentComponent', () => {
  let component: GenericCvContentComponent;
  let fixture: ComponentFixture<GenericCvContentComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericCvContentComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericCvContentComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
