import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseFormActionsComponent } from './base-form-actions.component';

describe('BaseFormActionsComponent', () => {
  let component: BaseFormActionsComponent;
  let fixture: ComponentFixture<BaseFormActionsComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [BaseFormActionsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(BaseFormActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
