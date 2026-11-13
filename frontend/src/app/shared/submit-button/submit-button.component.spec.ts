import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmitButtonComponent } from './submit-button.component';

describe('SubmitButtonComponent', () => {
  let component: SubmitButtonComponent;
  let fixture: ComponentFixture<SubmitButtonComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [SubmitButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SubmitButtonComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
