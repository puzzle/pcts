import { ShowIfAdminDirective } from './show-if-admin.directive';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../auth.service';
import { Component } from '@angular/core';
import { By } from '@angular/platform-browser';

@Component({
  standalone: true,
  imports: [ShowIfAdminDirective],
  template: '<div *appShowIfAdmin id="admin-content">Admin Only View</div>'
})
class ShowIfAdminDirectiveSpecComponent {}

describe('ShowIfAdminDirective (Jest)', () => {
  let fixture: ComponentFixture<ShowIfAdminDirectiveSpecComponent>;

  const mockUserService = {
    isAdmin: jest.fn()
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [ShowIfAdminDirectiveSpecComponent,
        ShowIfAdminDirective],
      providers: [{ provide: AuthService,
        useValue: mockUserService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShowIfAdminDirectiveSpecComponent);
  });

  it('should render content if user is admin', () => {
    mockUserService.isAdmin.mockReturnValue(true);

    fixture.detectChanges();

    const element = fixture.debugElement.query(By.css('#admin-content'));
    expect(element)
      .toBeTruthy();
    expect(element.nativeElement.textContent)
      .toContain('Admin Only View');
  });

  it('should NOT render content if user is not admin', () => {
    mockUserService.isAdmin.mockReturnValue(false);

    fixture.detectChanges();

    const element = fixture.debugElement.query(By.css('#admin-content'));
    expect(element)
      .toBeFalsy();
  });
});
