import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { AuthService } from './core/auth/auth.service';
import { signal } from '@angular/core';

jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let routerMock: Partial<Router>;
  let authServiceMock: Partial<AuthService>;

  beforeEach(async() => {
    authServiceMock = {
      name: signal('Test User'),
      logout: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
        .mockResolvedValue(true)
    };

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideTranslateService(),
        {
          provide: Router,
          useValue: routerMock
        },
        {
          provide: AuthService,
          useValue: authServiceMock
        }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should navigate to /member when visitRoot() is called', () => {
    component.visitRoot();

    expect(routerMock.navigate)
      .toHaveBeenCalledWith(['']);
  });

  it('should call logout service when handleLogout() is called', () => {
    component.handleLogout();

    expect(authServiceMock.logout)
      .toHaveBeenCalled();
  });
});
