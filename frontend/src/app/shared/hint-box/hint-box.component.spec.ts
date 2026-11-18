import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HintBoxComponent } from './hint-box.component';

describe('HintBoxComponent', () => {
  let component: HintBoxComponent;
  let fixture: ComponentFixture<HintBoxComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [HintBoxComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(HintBoxComponent);
    fixture.componentRef.setInput('text', 'Test message');
    fixture.componentRef.setInput('variant', 'hint');
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
