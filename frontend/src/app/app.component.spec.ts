import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { provideRouter } from '@angular/router';
import { provideTranslateService } from '@ngx-translate/core';
jest.mock('@puzzleitc/puzzle-shell', () => jest.fn());

describe('AppComponent', () => {
  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [provideTranslateService(),
        provideRouter([])]
    })
      .compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app)
      .toBeTruthy();
  });
});
