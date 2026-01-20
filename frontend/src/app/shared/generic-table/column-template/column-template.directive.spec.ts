import { Component, TemplateRef, ViewChild } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ColumnTemplateDirective } from './column-template.directive';

@Component({
  template: `
    <ng-template appColumnTemplate="statusColumn">
      <span>Some Content</span>
    </ng-template>
  `,
  imports: [ColumnTemplateDirective],
  standalone: true
})
class TestHostComponent {
  @ViewChild(ColumnTemplateDirective) directive!: ColumnTemplateDirective;
}

describe('ColumnTemplateDirective', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let component: TestHostComponent;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ColumnTemplateDirective,
        TestHostComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create the directive', () => {
    expect(component.directive)
      .toBeTruthy();
  });

  it('should capture the columnName input via the selector alias', () => {
    expect(component.directive.columnName())
      .toBe('statusColumn');
  });

  it('should inject the TemplateRef', () => {
    expect(component.directive.template)
      .toBeDefined();
    expect(component.directive.template instanceof TemplateRef)
      .toBe(true);
  });
});
