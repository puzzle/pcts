import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GenericCheckboxFormTableComponent } from './generic-checkbox-form-table.component';

describe('GenericCheckboxFormTableComponent', () => {
  let component: GenericCheckboxFormTableComponent;
  let fixture: ComponentFixture<GenericCheckboxFormTableComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [GenericCheckboxFormTableComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(GenericCheckboxFormTableComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
