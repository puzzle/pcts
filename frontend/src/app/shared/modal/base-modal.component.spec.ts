import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BaseModalComponent } from './base-modal.component';
import { FormControl, FormGroup } from '@angular/forms';


describe('BaseModal', () => {
  let component: BaseModalComponent;
  let fixture: ComponentFixture<BaseModalComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [BaseModalComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BaseModalComponent);
    const form = new FormGroup({
      name: new FormControl('John')
    });
    fixture.componentRef.setInput('formGroup', form);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
