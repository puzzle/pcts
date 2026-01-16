import { Component, inject } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslationScopeDirective } from './translation-scope.directive';
import { I18N_PREFIX } from '../i18n-prefix.token';

@Component({
  selector: 'app-mock-child',
  standalone: true,
  template: '<p>My Prefix is: {{ prefix }}</p>'
})
class MockChildComponent {
  prefix = inject(I18N_PREFIX, { optional: true }) ?? 'NOT_FOUND';
}

@Component({
  standalone: true,
  imports: [TranslationScopeDirective,
    MockChildComponent],
  template: `
    <app-mock-child *appTranslationScope="'EXPERIENCE'" />
  `
})
class TestHostComponent {}

describe('TranslationScopeDirective', () => {
  let fixture: ComponentFixture<TestHostComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    fixture.detectChanges();
  });

  it('should create the embedded view', () => {
    const child = fixture.nativeElement.querySelector('app-mock-child');
    expect(child)
      .toBeTruthy();
  });

  it('should provide the I18N_PREFIX to the child component via the custom injector', () => {
    const child = fixture.nativeElement.querySelector('app-mock-child');

    expect(child.textContent)
      .toContain('My Prefix is: GLOBAL.DEFAULT.PREFIX.EXPERIENCE');
  });

  it('should pass the correct value when the input string changes', () => {
    expect(fixture.componentInstance)
      .toBeTruthy();
  });
});
