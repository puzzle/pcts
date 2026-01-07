import { Injector } from '@angular/core';
import { ScopedTranslationCoreService } from './services/scoped-translation-core.service';
import { I18N_PREFIX } from './i18n-prefix.token';
import { provideI18nPrefix, ScopedTranslationService } from './i18n-prefix.provider';

const mockCoreService = {
  instant: jest.fn()
};

describe('I18n Scoped Translation', () => {
  let rootInjector: Injector;

  function createScopedInjector(suffix: string, parent?: Injector, separator?: string): Injector {
    const providers = [{ provide: ScopedTranslationCoreService,
      useValue: mockCoreService },
    ...provideI18nPrefix(suffix, separator)];

    return Injector.create({
      parent: parent,
      providers: providers
    });
  }

  beforeEach(() => {
    jest.clearAllMocks();

    rootInjector = Injector.create({
      providers: [{ provide: I18N_PREFIX,
        useValue: 'root' }]
    });
  });

  describe('provideI18nPrefix Factory', () => {
    it('should return the suffix as the prefix when there is NO parent injector', () => {
      const injector = createScopedInjector('featureA');
      expect(injector.get(I18N_PREFIX))
        .toBe('featureA');
    });

    it('should append the suffix to the parent prefix', () => {
      const injector = createScopedInjector('child', rootInjector);
      expect(injector.get(I18N_PREFIX))
        .toBe('root.child');
    });

    it('should handle deep nesting', () => {
      const level1Injector = createScopedInjector('level1', rootInjector);
      const level2Injector = createScopedInjector('level2', level1Injector);

      expect(level2Injector.get(I18N_PREFIX))
        .toBe('root.level1.level2');
    });

    it('should use a custom separator if provided', () => {
      const injector = createScopedInjector('subsection', rootInjector, '-');
      expect(injector.get(I18N_PREFIX))
        .toBe('root-subsection');
    });

    it('should return the parent prefix if the new suffix is empty', () => {
      const injector = createScopedInjector('', rootInjector);
      expect(injector.get(I18N_PREFIX))
        .toBe('root');
    });
  });

  describe('ScopedTranslationService Class', () => {
    it('should correctly integrate prefix with core service', () => {
      const injector = createScopedInjector('child', rootInjector);
      const service = injector.get(ScopedTranslationService);

      mockCoreService.instant.mockReturnValue('Result');

      const result = service.instant('KEY', { id: 1 });

      expect(service.prefix)
        .toBe('root.child');
      expect(mockCoreService.instant)
        .toHaveBeenCalledWith('KEY', { id: 1 }, 'root.child');
      expect(result)
        .toBe('Result');
    });
  });
});
