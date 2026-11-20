import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CancelButtonComponent } from './cancel-button.component';

describe('CancelButtonComponent', () => {
  let component: CancelButtonComponent;
  let fixture: ComponentFixture<CancelButtonComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CancelButtonComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CancelButtonComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
