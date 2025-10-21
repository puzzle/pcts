import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InputFieldComponent } from './input-field.component';
import { MatFormField } from '@angular/material/form-field';
import { provideTranslateService } from '@ngx-translate/core';

describe('InputFieldComponent', () => {
  let component: InputFieldComponent;
  let fixture: ComponentFixture<InputFieldComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [InputFieldComponent],
      providers: [provideTranslateService()]
    })
      .overrideComponent(InputFieldComponent, {
        set: { template: '<div></div>' }
      })
      .compileComponents();

    fixture = TestBed.createComponent(InputFieldComponent);
    component = fixture.componentInstance;

    (component as any).matFormField = () => ({} as MatFormField);
    (component as any).matFormFieldControl = () => ({});

    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should have a MatFormField viewChild defined', () => {
    const mockFormField = {} as MatFormField;
    (component as any).matFormField = () => mockFormField;
    expect(component.matFormField())
      .toBe(mockFormField);
  });

  it('should have a MatFormFieldControl contentChild defined', () => {
    const mockControl = { value: 'test' };
    (component as any).matFormFieldControl = () => mockControl;
    expect(component.matFormFieldControl().value)
      .toBe('test');
  });

  it('should connect form field control in effect', () => {
    const mockFormField = { _control: null } as unknown as MatFormField;
    const mockControl = { id: 'ctrl1' } as any;

    (component as any).matFormField = () => mockFormField;
    (component as any).matFormFieldControl = () => mockControl;

    mockFormField._control = mockControl;
    expect(mockFormField._control)
      .toBe(mockControl);
  });
});
