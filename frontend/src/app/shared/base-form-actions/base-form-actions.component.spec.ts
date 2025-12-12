import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseFormActionsComponent } from './base-form-actions.component';
import { BaseFormComponent } from '../form/base-form.component';
import { FormControl, FormGroup } from '@angular/forms';
import { provideTranslateService } from '@ngx-translate/core';

describe('BaseFormActionsComponent', () => {
  let component: BaseFormActionsComponent;
  let fixture: ComponentFixture<BaseFormActionsComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [BaseFormActionsComponent],
      providers: [provideTranslateService()]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BaseFormActionsComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('formGroup', new FormGroup({}));
    fixture.componentRef.setInput('formName', 'testForm');
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });


  it('should emit canceled event when called', () => {
    const baseFixture = TestBed.createComponent(BaseFormComponent);
    const form = new FormGroup({
      name: new FormControl('John')
    });
    baseFixture.componentRef.setInput('formGroup', form);
    baseFixture.componentRef.setInput('formName', 'userForm');
    baseFixture.detectChanges();

    jest.spyOn(component.canceled, 'emit');
    component.canceled.emit();

    expect(component.canceled.emit)
      .toHaveBeenCalledTimes(1);
  });
});
