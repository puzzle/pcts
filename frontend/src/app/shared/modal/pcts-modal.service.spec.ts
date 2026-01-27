import { TestBed } from '@angular/core/testing';
import { PctsModalService } from './pcts-modal.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Injector } from '@angular/core';
import { Subject, firstValueFrom, lastValueFrom } from 'rxjs';
import { FormGroup, FormControl } from '@angular/forms';
import { defaultSize } from './base-modal.component';
import { enrichMatDialogRef, StrictlyTypedDialog } from './strictly-typed-dialog.helper';

interface TestData {
  id: number;
}

interface TestResult {
  success: boolean;
}

class TestModalComponent extends StrictlyTypedDialog<TestData, TestResult> {
  protected formGroup = new FormGroup({
    test: new FormControl('')
  });
}

describe('PctsModalService', () => {
  let service: PctsModalService;

  let matDialogSpy: { open: jest.Mock };
  let matDialogRefSpy: { afterClosed: jest.Mock;
    close: jest.Mock; };
  let afterClosedSubject: Subject<TestResult | undefined>;

  beforeEach(() => {
    afterClosedSubject = new Subject<TestResult | undefined>();

    matDialogRefSpy = {
      afterClosed: jest.fn()
        .mockReturnValue(afterClosedSubject.asObservable()),
      close: jest.fn()
    };

    matDialogSpy = {
      open: jest.fn()
        .mockReturnValue(matDialogRefSpy)
    };

    TestBed.configureTestingModule({
      providers: [PctsModalService,
        { provide: MatDialog,
          useValue: matDialogSpy }]
    });

    service = TestBed.inject(PctsModalService);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  describe('openModal', () => {
    it('should call MatDialog.open with component and merged config', () => {
      const testData: TestData = { id: 123 };
      const config = { data: testData,
        disableClose: true };

      service.openModal(TestModalComponent, config);

      expect(matDialogSpy.open)
        .toHaveBeenCalledTimes(1);

      expect(matDialogSpy.open)
        .toHaveBeenCalledWith(TestModalComponent, expect.objectContaining({
          ...defaultSize,
          data: testData,
          disableClose: true
        }));
    });
  });

  describe('openModalAtCurrentHierarchy', () => {
    it('should inject the current Injector into the dialog config', () => {
      const testData: TestData = { id: 456 };
      const injector = TestBed.inject(Injector);

      service.openModalAtCurrentHierarchy(TestModalComponent, { data: testData });

      const actualConfig = matDialogSpy.open.mock.calls[0][1] as MatDialogConfig;

      expect(actualConfig.injector)
        .toBe(injector);
      expect(actualConfig.data)
        .toEqual(testData);
    });
  });

  describe('Service Integration with enrichMatDialogRef', () => {
    it('should return a ref that emits via afterSubmitted when dialog returns a valid result', async() => {
      const testData: TestData = { id: 1 };
      const expectedResult: TestResult = { success: true };

      const ref = service.openModal(TestModalComponent, { data: testData });
      const promise = firstValueFrom(ref.afterSubmitted);

      afterClosedSubject.next(expectedResult);

      await expect(promise).resolves.toEqual(expectedResult);
    });
  });
});

describe('enrichMatDialogRef', () => {
  let mockRef: any;
  let afterClosedSubject: Subject<any>;

  beforeEach(() => {
    afterClosedSubject = new Subject();
    mockRef = {
      afterClosed: jest.fn()
        .mockReturnValue(afterClosedSubject)
    };
  });

  it('should attach the afterSubmitted property to the ref', () => {
    const enriched = enrichMatDialogRef(mockRef);
    expect(enriched)
      .toHaveProperty('afterSubmitted');
  });

  it('should filter out undefined values from afterClosed', () => {
    const enriched = enrichMatDialogRef(mockRef);
    const spy = jest.fn();

    enriched.afterSubmitted.subscribe(spy);

    afterClosedSubject.next(undefined);

    expect(spy).not.toHaveBeenCalled();
  });

  it('should Replay the last value to late subscribers', async() => {
    const enriched = enrichMatDialogRef(mockRef);
    const expected = { foo: 'bar' };

    afterClosedSubject.next(expected);

    const promise = firstValueFrom(enriched.afterSubmitted);

    await expect(promise).resolves.toEqual(expected);
  });

  it('should propagate errors from afterClosed', async() => {
    const enriched = enrichMatDialogRef(mockRef);
    const errorObj = new Error('Dialog crashed');

    const promise = firstValueFrom(enriched.afterSubmitted);

    afterClosedSubject.error(errorObj);

    await expect(promise).rejects.toBe(errorObj);
  });

  it('should complete afterSubmitted when afterClosed completes', async() => {
    const enriched = enrichMatDialogRef(mockRef);

    afterClosedSubject.next({ data: 'ok' });
    afterClosedSubject.complete();

    await expect(lastValueFrom(enriched.afterSubmitted)).resolves.toEqual({ data: 'ok' });
  });
});
