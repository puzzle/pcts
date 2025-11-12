import { ScopedTranslationPipe } from './scoped-translation-pipe';
import { TestBed } from '@angular/core/testing';

describe('ScopedTranslationPipe', () => {
  let pipe: ScopedTranslationPipe;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScopedTranslationPipe]
    });
    pipe = TestBed.inject(ScopedTranslationPipe);
  });
  it('create an instance', () => {
    expect(pipe)
      .toBeTruthy();
  });
});
