import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DOCUMENT } from '@angular/common';

import { provideTranslateService } from '@ngx-translate/core';
import { UserService } from './core/auth/user.service';

jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
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

    await TestBed.configureTestingModule({
      imports: [AppComponent, TranslateModule.forRoot()],
      providers: [provideTranslateService(),{
        provide: UserService,
        useValue: userServiceMock
      },
        provideRouter([])]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    document = TestBed.inject(DOCUMENT);
    translateService = TestBed.inject(TranslateService);

    router = TestBed.inject(Router);
    jest.spyOn(router, 'navigate')
      .mockImplementation(() => Promise.resolve(true));

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

    expect(router.navigate)
      .toHaveBeenCalledWith(['/member']);
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
