import { RowDetailTemplateDirective } from './rowDetailTemplate.directive';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Component, ViewChild } from '@angular/core';

@Component({
  template: `
    <ng-template appExpandableRow>
      <span>Some Content</span>
    </ng-template>
  `,
  imports: [RowDetailTemplateDirective],
  standalone: true
})
class TestHostComponent {
  @ViewChild(RowDetailTemplateDirective) directive!: RowDetailTemplateDirective;
}

describe('RowDetailTemplateDirective', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let component: TestHostComponent;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [RowDetailTemplateDirective,
        TestHostComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create an instance', () => {
    expect(component.directive)
      .toBeTruthy();
  });
});
