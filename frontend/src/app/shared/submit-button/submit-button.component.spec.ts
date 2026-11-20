import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmitButtonComponent } from './submit-button.component';
import { By } from '@angular/platform-browser';

describe('SubmitButtonComponent', () => {
  let component: SubmitButtonComponent;
  let fixture: ComponentFixture<SubmitButtonComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [SubmitButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(SubmitButtonComponent);
    fixture.componentRef.setInput('disabled', false);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should emit selected value when the button is clicked', () => {
    const emitSpy = jest.spyOn(component.saveClicked, 'emit');

    const button = fixture.debugElement.query(By.css('[data-testid="calculation-save-button"]'));
    expect(button)
      .toBeTruthy();
    button.nativeElement.click();
    fixture.detectChanges();

    expect(emitSpy)
      .toHaveBeenCalled();
  });

  it('should be disabled when the button is disabled by input', () => {
    fixture.componentRef.setInput('disabled', true);
    const button = fixture.debugElement.query(By.css('[data-testid="calculation-save-button"]'));

    expect(button)
      .toBeTruthy();
    expect(fixture.componentInstance.disabled())
      .toBeTruthy();
  });

  it('should be display correct label', () => {
    fixture.componentRef.setInput('text', 'Test text');
    const button = fixture.debugElement.query(By.css('[data-testid="calculation-save-button"]'));

    expect(button)
      .toBeTruthy();
    expect(fixture.componentInstance.text())
      .toEqual('Test text');
  });
});
