import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BaseModalComponent } from './base-modal.component';


describe('BaseModal', () => {
  let component: BaseModalComponent;
  let fixture: ComponentFixture<BaseModalComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [BaseModalComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BaseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
