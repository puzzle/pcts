import { DestroyRef, Injector } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { PctsModalBuilder } from './pcts-modal-builder';
import { ModelWithId } from './pcts-modal.service';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { I18N_PREFIX } from '../i18n-prefix.token';

describe('PctsModalBuilder', () => {
  let builder: PctsModalBuilder<ModelWithId>;
  let destroyRefMock: DestroyRef;
  let injectorMock: Injector;
  let openModalSpy: jest.Mock;
  let afterSubmittedSubjects: Subject<any>[];
  const modelWithId = { id: 1 };
  class MockModalComponent {}

  beforeEach(() => {
    destroyRefMock = {
      onDestroy: jest.fn()
    } as unknown as DestroyRef;

    injectorMock = Injector.create({ providers: [] });

    afterSubmittedSubjects = [];

    openModalSpy = jest.fn()
      .mockImplementation(() => {
        const subject = new Subject<any>();
        afterSubmittedSubjects.push(subject);
        return { afterSubmitted: subject.asObservable() };
      });

    builder = new PctsModalBuilder<ModelWithId>(destroyRefMock, openModalSpy, injectorMock);
  });

  it('should throw error when required values are missing', () => {
    expect(() => builder.build())
      .toThrow('Component and onSubmitMethod must be provided');
  });

  describe('Config Methods', () => {
    beforeEach(() => {
      const mockSubmitMethod = jest.fn()
        .mockReturnValue(of({}));
      builder.withComponent(MockModalComponent as any)
        .withOnSubmitMethod(mockSubmitMethod);
    });

    it('should set the correct submitMethods for edit ', () => {
      const opener = builder.withSubmitOptionsForEdit()
        .build();
      opener(modelWithId);

      const config = getConfig();

      expect(config.data.submitOptions)
        .toEqual([ModalSubmitMode.DELETE]);
    });

    it('should set the correct submitMethods for add', () => {
      const opener = builder.withSubmitOptionsForAdd()
        .build();
      opener();

      const config = getConfig();

      expect(config.data.submitOptions)
        .toEqual([ModalSubmitMode.COPY,
          ModalSubmitMode.ENTER_ANOTHER]);
    });

    it('should pass down the correct injector', () => {
      const prefix = 'prefix';

      const opener = builder.withI18nPrefix(prefix)
        .build();
      opener();

      const config = getConfig();
      const injectorPrefix = config.injector.get(I18N_PREFIX);

      expect(injectorPrefix)
        .toBe(prefix);
    });
  });

  describe('Submit Methods', () => {
    let submitSpy: jest.Mock;
    let successSpy: jest.Mock;
    let deleteSpy: jest.Mock;
    let opener: (model?: ModelWithId) => void;

    beforeEach(() => {
      submitSpy = jest.fn()
        .mockImplementation((model) => of(model));
      deleteSpy = jest.fn()
        .mockImplementation((id: number) => Observable<void>);
      successSpy = jest.fn();

      opener = builder
        .withComponent(MockModalComponent as any)
        .withOnSubmitMethod(submitSpy)
        .withOnSuccessMethod(successSpy)
        .withOnDeleteMethod(deleteSpy)
        .build();

      opener();
      expect(openModalSpy)
        .toHaveBeenCalledTimes(1);
    });

    it('should not reopen the modal on save', () => {
      submitModal(ModalSubmitMode.SAVE, modelWithId);

      expect(submitSpy)
        .toHaveBeenCalledWith(modelWithId);
      expect(successSpy)
        .toHaveBeenCalled();
      expect(deleteSpy)
        .not.toHaveBeenCalled();

      expect(openModalSpy)
        .toHaveBeenCalledTimes(1);
    });

    it('should reopen the modal on enter another with undefined model', () => {
      submitModal(ModalSubmitMode.ENTER_ANOTHER, modelWithId);

      expect(submitSpy)
        .toHaveBeenCalledWith(modelWithId);
      expect(successSpy)
        .toHaveBeenCalled();

      expect(openModalSpy)
        .toHaveBeenCalledTimes(2);
      expect(deleteSpy)
        .not.toHaveBeenCalled();

      const config = getConfig(1);
      expect(config.data.model)
        .toBeUndefined();
    });

    it('should reopen the modal on copy with previous model', () => {
      submitModal(ModalSubmitMode.COPY, modelWithId);

      expect(submitSpy)
        .toHaveBeenCalledWith(modelWithId);
      expect(successSpy)
        .toHaveBeenCalled();

      expect(openModalSpy)
        .toHaveBeenCalledTimes(2);
      expect(deleteSpy)
        .not.toHaveBeenCalled();

      const config = getConfig(1);
      expect(config.data.model)
        .toEqual(modelWithId);
    });

    it('should not reopen modal on delete and call delete method', () => {
      submitModal(ModalSubmitMode.DELETE, modelWithId);

      expect(successSpy)
        .not.toHaveBeenCalled();
      expect(openModalSpy)
        .toHaveBeenCalledTimes(1);
      expect(deleteSpy)
        .toHaveBeenCalled();
    });
  });

  const getConfig = (callIndex = 0) => {
    return openModalSpy.mock.calls[callIndex][1];
  };

  const submitModal = (modalSubmitMode: ModalSubmitMode, submittedData: ModelWithId) => {
    afterSubmittedSubjects[0].next({
      modalSubmitMode: modalSubmitMode,
      submittedModel: submittedData
    });
  };
});


