import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalculationFormComponent } from './calculation-form.component';

describe('CalculationFormComponent', () => {
  let component: CalculationFormComponent;
  let fixture: ComponentFixture<CalculationFormComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CalculationFormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CalculationFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
