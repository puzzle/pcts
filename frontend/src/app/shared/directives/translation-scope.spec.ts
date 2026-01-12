import { TranslationScope } from './translation-scope';

describe('TranslationScope', () => {
  it('should create an instance', () => {
    const directive = new TranslationScope();
    expect(directive)
      .toBeTruthy();
  });
});
