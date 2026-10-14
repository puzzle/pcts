import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CertificateOverviewComponent } from './certificate-overview.component';
import { ActivatedRoute, Router, provideRouter } from '@angular/router';
import { CertificateTypeModel } from '../certificate-type/certificate-type.model';
import { certificateType1, certificateType2 } from '../../../shared/test/test-data';
import { CertificateFilter } from './certificateFilter';

describe('CertificateOverviewComponent', () => {
  let component: CertificateOverviewComponent;
  let fixture: ComponentFixture<CertificateOverviewComponent>;
  let router: Router;
  let route: ActivatedRoute;

  const mockCertificates: CertificateTypeModel[] = [certificateType1,
    certificateType1];

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CertificateOverviewComponent],
      providers: [provideRouter([])]
    })
      .compileComponents();

    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);

    jest.spyOn(router, 'navigate')
      .mockResolvedValue(true);

    fixture = TestBed.createComponent(CertificateOverviewComponent);
    component = fixture.componentInstance;
  });

  function setInputs(certificates: CertificateTypeModel[] = [certificateType1,
    certificateType2], filter: CertificateFilter = { textSearch: '' }) {
    fixture.componentRef.setInput('certificates', certificates);
    fixture.componentRef.setInput('filter', filter);
    fixture.detectChanges();
  }

  it('should create', () => {
    setInputs();
    expect(component)
      .toBeTruthy();
  });

  describe('Effects', () => {
    it('should update table data when certificates input changes', () => {
      setInputs(mockCertificates, { textSearch: '' });

      expect(component.table.data)
        .toEqual(mockCertificates);
    });

    it('should emit event on filterFormGroup when input updates current value', () => {
      setInputs([], { textSearch: 'aws' });

      const setValueSpy = jest.spyOn(component.filterFormGroup, 'setValue');

      fixture.componentRef.setInput('filter', { textSearch: 'aws' });
      fixture.detectChanges();

      expect(setValueSpy)
        .toHaveBeenCalled();
    });
  });

  describe('User Interaction (filterFormGroup & updateUrl)', () => {
    it('should update URL with query parameter when updateUrl is triggered', () => {
      setInputs([], { textSearch: '' });

      (component as any).updateUrl({ textSearch: 'azure' });

      expect(router.navigate)
        .toHaveBeenCalledWith([], {
          relativeTo: route,
          queryParams: { q: 'azure' },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
    });

    it('should pass empty textSearch to q parameter in URL when textSearch is empty', () => {
      setInputs([], { textSearch: 'aws' });

      (component as any).updateUrl({ textSearch: '' });

      expect(router.navigate)
        .toHaveBeenCalledWith([], {
          relativeTo: route,
          queryParams: { q: null },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
    });

    it('should handle partial form updates correctly in updateUrl', () => {
      setInputs([], { textSearch: '' });

      (component as any).updateUrl({ textSearch: 'kubernetes' });

      expect(router.navigate)
        .toHaveBeenCalledWith([], {
          relativeTo: route,
          queryParams: { q: 'kubernetes' },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
    });
  });
  describe('Table Custom Filter Predicate', () => {
    it('should correctly filter certificates in GenericTableDataSource', () => {
      setInputs(undefined, { textSearch: 'aws' });
      expect(component.table.filteredData)
        .toEqual([certificateType1]);

      setInputs(undefined, { textSearch: 'ruby' });
      expect(component.table.filteredData)
        .toEqual([certificateType2]);

      setInputs(undefined, { textSearch: '10' });
      expect(component.table.filteredData)
        .toEqual([certificateType1]);

      setInputs(undefined, { textSearch: '15' });
      expect(component.table.filteredData)
        .toEqual([certificateType2]);

      setInputs(undefined, { textSearch: 'gitlab' });
      expect(component.table.filteredData)
        .toEqual([certificateType1]);
    });
  });
});
