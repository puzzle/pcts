import eslint from '@eslint/js'
import tsEslint from 'typescript-eslint'
import unusedImports from 'eslint-plugin-unused-imports'
import stylistic from '@stylistic/eslint-plugin'
import html from '@html-eslint/eslint-plugin'
import angular from 'angular-eslint'
import angularTemplateParser from '@angular-eslint/template-parser'
import angularTemplate from '@angular-eslint/eslint-plugin-template'
import checkFile from 'eslint-plugin-check-file'
export default tsEslint.config(
  {
    files: ['src/app/shared/types/**/*'],
    rules: {
      'check-file/filename-naming-convention': [
        'error',
        {
          '**/*.{js,ts}': 'KEBAB_CASE',
        },
      ],
    },
  },
  {
    ignores: [
      'cypress/downloads/**/*',
      'dist/**',
      '.angular/**',
      'node_modules/**',
      'src/app/app.component.html',
    ], // #TODO: Match cypress path to project #12- E2E setup
  },
  {
    files: ['src/**/*.ts', 'cypress/**/*.ts'], // #TODO: Match cypress path to project #12- E2E setup
    extends: [
      eslint.configs.recommended,
      ...tsEslint.configs.recommended,
      ...tsEslint.configs.stylistic,
      ...angular.configs.tsRecommended,
    ],
    processor: angular.processInlineTemplates,
    languageOptions: {
      parserOptions: {
        project: [
          './tsconfig.json',
          './tsconfig.spec.json',
          './tsconfig.app.json',
          './cypress/tsconfig.json',
        ],
      },
      globals: {
        //Cypress things not recognized by eslint
        cy: 'readonly',
        Cypress: 'readonly',
        it: 'readonly',
        describe: 'readonly',
        expect: 'readonly',
        beforeEach: 'readonly',
        before: 'readonly',
        //Dom things not recognized by eslint
        localStorage: 'readonly',
        console: 'readonly',
        window: 'readonly',
        document: 'readonly',
        //Event not recognized by eslint
        MouseEvent: 'readonly',
        KeyboardEvent: 'readonly',
        Event: 'readonly',
        //HTML Elements not recognized by eslint
        HTMLDivElement: 'readonly',
        HTMLInputElement: 'readonly',
        HTMLSpanElement: 'readonly',
        HTMLElement: 'readonly',
        HTMLTitleElement: 'readonly',
        HTMLHtmlElement: 'readonly',
        //Others not recognized by eslint
        ResizeObserver: 'readonly',
        ResizeObserverEntry: 'readonly',
        setTimeout: 'readonly',
        JQuery: 'readonly',
        Document: 'readonly',
        URL: 'readonly',
      },
    },
    rules: {
      ...stylistic.configs.all.rules,
      //eslint rules
      'unused-imports/no-unused-imports': 'error',
      'no-undef': 'error',
      curly: 'error',
      'prefer-rest-params': 'error',
      'space-before-function-paren': ['error', 'never'],

      //Typescript eslint rules
      '@typescript-eslint/ban-ts-comment': 'error',
      '@typescript-eslint/no-unused-expressions': [
        'error',
        {
          allowTernary: true,
        },
      ],

      '@typescript-eslint/no-unused-vars': [
        'error',
        {
          args: 'none',
        },
      ],
      '@typescript-eslint/no-explicit-any': 'off',
      '@typescript-eslint/no-namespace': [
        'error',
        {
          allowDeclarations: true,
        },
      ],
      '@typescript-eslint/no-empty-function': [
        'error',
        { allow: ['arrowFunctions', 'constructors'] },
      ],
      //Turned off to allow ! in the code
      '@typescript-eslint/no-non-null-assertion': 'off',
      '@typescript-eslint/no-non-null-asserted-optional-chain': 'off',

      '@typescript-eslint/no-confusing-non-null-assertion': 'error',

      //Stylistic eslint rules
      '@stylistic/no-extra-parens': 'error',
      '@stylistic/function-call-argument-newline': ['error', 'never'],
      '@stylistic/quotes': ['error', 'single'],
      '@stylistic/padded-blocks': ['error', 'never'],
      '@stylistic/dot-location': ['error', 'property'],
      '@stylistic/newline-per-chained-call': [
        'error',
        { ignoreChainWithDepth: 1 },
      ],
      '@stylistic/indent': ['error', 2],
      '@stylistic/quote-props': ['error', 'as-needed'],
      '@stylistic/object-property-newline': ['error'],
      '@stylistic/multiline-ternary': ['off'],
      '@stylistic/object-curly-spacing': ['error', 'always'],
      '@stylistic/array-bracket-newline': ['error', { minItems: 4 }],
      '@stylistic/semi-style': ['error'],
      '@stylistic/function-paren-newline': ['error', { minItems: 4 }],
      '@stylistic/space-before-function-paren': ['error', 'never'],
      // Disabled because it's an unnecessary rule in our case
      '@stylistic/lines-around-comment': 'off',
      //Angular eslint rules
      '@angular-eslint/no-empty-lifecycle-method': 'error',
      '@angular-eslint/component-class-suffix': 'error',
      '@angular-eslint/directive-selector': [
        'error',
        {
          type: 'attribute',
          // reenable this after fixing all the directives
          // prefix: 'app',
          style: 'camelCase',
        },
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'app',
          style: 'kebab-case',
        },
      ],
      '@angular-eslint/prefer-standalone': 'off',
      '@typescript-eslint/naming-convention': [
        'error',
        {
          selector: ['class', 'interface'],
          format: ['PascalCase'],
        },
        {
          selector: 'variable',
          modifiers: [],
          format: ['camelCase', 'UPPER_CASE'],
        },
        {
          selector: 'enum',
          format: ['PascalCase'],
        },
        {
          selector: 'enumMember',
          format: ['UPPER_CASE'],
        },
        {
          selector: ['method', 'function'],
          format: ['camelCase'],
        },
        {
          selector: 'typeParameter',
          format: ['PascalCase'],
        },
      ],
    },
  },

  {
    files: ['src/**/*.spec.ts', 'cypress/**/*.spec.ts'], // #TODO: Match cypress path to project #12- E2E setup

    extends: [...tsEslint.configs.recommended],
    rules: {
      //Rules removed for Test files because they are unnecessary for tests
      '@typescript-eslint/no-explicit-any': 'off',
      'prefer-rest-params': 'off',
      '@typescript-eslint/no-empty-function': 'off',
      '@typescript-eslint/ban-ts-comment': 'off',
      '@typescript-eslint/no-non-null-assertion': 'off',
    },
  },

  {
    files: ['**/*.component.html'],
    ...angular.configs.recommended,
    languageOptions: {
      parser: angularTemplateParser,
    },
    rules: {
      '@angular-eslint/template/alt-text': 'error',
      '@angular-eslint/template/valid-aria': 'error',
      '@angular-eslint/template/elements-content': 'error',
      '@angular-eslint/template/button-has-type': 'warn',
      '@angular-eslint/template/label-has-associated-control': 'error',
      '@angular-eslint/template/click-events-have-key-events': 'error',
      '@angular-eslint/template/interactive-supports-focus': 'error',
      '@angular-eslint/template/no-positive-tabindex': 'error',
      '@angular-eslint/template/use-track-by-function': 'warn',
      '@angular-eslint/template/no-negated-async': 'error',
      '@angular-eslint/template/no-inline-styles': 'error',
      '@angular-eslint/template/prefer-self-closing-tags': 'error',

      '@html-eslint/element-newline': 'off',
    },
  },
  {
    plugins: {
      'unused-imports': unusedImports,
      '@stylistic': stylistic,
      '@html-eslint': html,
      'check-file': checkFile,
      '@angular-eslint/template': angularTemplate,
    },
  }
)
