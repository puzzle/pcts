import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DOCUMENT } from '@angular/common';

import { provideTranslateService } from '@ngx-translate/core';
import { UserService } from './core/auth/user.service';

jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;
  let document: Document;
  let translateService: TranslateService;

  beforeEach(async() => {
    const userServiceMock = {
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
    app = fixture.componentInstance;
    document = TestBed.inject(DOCUMENT);
    translateService = TestBed.inject(TranslateService);
  });

  afterEach(() => {
    document.documentElement.removeAttribute('lang');
  });

  it('should create the app', () => {
    expect(app)
      .toBeTruthy();
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
