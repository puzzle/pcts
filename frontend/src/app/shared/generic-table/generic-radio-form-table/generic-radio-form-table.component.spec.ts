import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericRadioFormTableComponent } from './generic-radio-form-table.component';

describe('GenericRadioFormTableComponent', () => {
  let component: GenericRadioFormTableComponent;
  let fixture: ComponentFixture<GenericRadioFormTableComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericRadioFormTableComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericRadioFormTableComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
