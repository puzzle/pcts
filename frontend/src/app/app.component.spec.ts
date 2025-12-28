import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DOCUMENT } from '@angular/common';

import { Router } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
import { UserService } from './core/auth/user.service';

jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let routerMock: Partial<Router>;
  let component: AppComponent;
  let router: Router;
  let document: Document;
  let translateService: TranslateService;
  let userServiceMock: Partial<UserService>;

  beforeEach(async() => {
    userServiceMock = {
      getName: jest.fn()
        .mockReturnValue('Test User'),
      logout: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
        .mockResolvedValue(true)
    };

    await TestBed.configureTestingModule({
      imports: [AppComponent, TranslateModule.forRoot()],
      providers: [provideTranslateService(),
        provideRouter([]),
        {
          provide: Router,
          useValue: routerMock
        },
        {
          provide: UserService,
          useValue: userServiceMock
        }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    document = TestBed.inject(DOCUMENT);
    translateService = TestBed.inject(TranslateService);

    fixture.detectChanges();
  });

  afterEach(() => {
    document.documentElement.removeAttribute('lang');
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

    expect(userServiceMock.logout)
      .toHaveBeenCalled();
  });

  it('should set default lang attribute', () => {
    fixture.detectChanges();

    expect(document.documentElement.getAttribute('lang'))
      .toBe('en');
  });

  it('should update the lang attribute', () => {
    fixture.detectChanges();

    translateService.use('de');
    fixture.detectChanges();

    expect(document.documentElement.getAttribute('lang'))
      .toBe('de');
  });
});
