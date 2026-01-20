import { Component, ViewChild, signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TypedTemplateDirective } from './typed-template.directive';

@Component({
  template: `
    <ng-template [appTypedTemplate]="testData()">
      User Content
    </ng-template>
  `,
  imports: [TypedTemplateDirective],
  standalone: true
})
class TestHostComponent {
  testData = signal('hello');

  @ViewChild(TypedTemplateDirective) directive!: TypedTemplateDirective<string>;
}

describe('TypedTemplateDirective', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let component: TestHostComponent;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [TypedTemplateDirective,
        TestHostComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    component = fixture.componentInstance;
  });

  it('should create the directive instance', () => {
    fixture.detectChanges();
    expect(component.directive)
      .toBeTruthy();
  });

  it('should accept the input value', () => {
    fixture.detectChanges();
    expect(component.directive.appTypedTemplate())
      .toBe('hello');
  });

  it('should handle updates to the input', () => {
    fixture.detectChanges();
    expect(component.directive.appTypedTemplate())
      .toBe('hello');

    component.testData.set('world');

    fixture.detectChanges();

    expect(component.directive.appTypedTemplate())
      .toBe('world');
  });

  describe('Static Type Guard (ngTemplateContextGuard)', () => {
    it('should always return true', () => {
      fixture.detectChanges();

      const context = { $implicit: 'some-value' };
      const result = TypedTemplateDirective.ngTemplateContextGuard(component.directive, context);

      expect(result)
        .toBe(true);
    });
  });
});
