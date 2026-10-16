import { ComponentFixture, TestBed } from '@angular/core/testing';
import CertificateOverviewComponent from './certificate-overview.component';
import { ActivatedRoute, Router, provideRouter } from '@angular/router';
import { CertificateTypeModel } from '../certificate-type/certificate-type.model';
import { allTags, certificateType1, certificateType2, certificateType3 } from '../../../shared/test/test-data';
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
    certificateType2,
    certificateType3], filter: CertificateFilter = { textSearch: '',
    tags: [] }) {
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
      setInputs(mockCertificates, { textSearch: '',
        tags: [] });

      expect(component.table.data)
        .toEqual(mockCertificates);
    });

    it('should not emit event on filterFormGroup when filter is equals to input', () => {
      setInputs([], { textSearch: 'aws',
        tags: [] });

      const setValueSpy = jest.spyOn(component.filterFormGroup, 'setValue');

      fixture.componentRef.setInput('filter', { textSearch: 'aws' });
      fixture.detectChanges();

      expect(setValueSpy)
        .not.toHaveBeenCalled();
    });

    it('should emit event on filterFormGroup when filter is not equals to input', () => {
      setInputs([], { textSearch: 'a' });

      const setValueSpy = jest.spyOn(component.filterFormGroup, 'setValue');

      fixture.componentRef.setInput('filter', { textSearch: 'aws' });
      fixture.detectChanges();

      expect(setValueSpy)
        .toHaveBeenCalled();
    });
  });

  describe('User Interaction (filterFormGroup & updateUrl)', () => {
    it('should update URL with query parameter when updateUrl is triggered', () => {
      setInputs([], { textSearch: '',
        tags: [] });

      (component as any).updateUrl({ textSearch: 'azure' });

      expect(router.navigate)
        .toHaveBeenCalledWith([], {
          relativeTo: route,
          queryParams: { textSearch: 'azure' },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
    });

    it('should pass empty textSearch to q parameter in URL when textSearch is empty', () => {
      setInputs([], { textSearch: 'aws',
        tags: [] });

      (component as any).updateUrl({ textSearch: '' });

      expect(router.navigate)
        .toHaveBeenCalledWith([], {
          relativeTo: route,
          queryParams: { textSearch: null },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
    });

    it('should handle partial form updates correctly in updateUrl', () => {
      setInputs([], { textSearch: '',
        tags: [] });

      (component as any).updateUrl({ textSearch: 'kubernetes',
        tags: ['kubernetes',
          'CI/CD'] });

      expect(router.navigate)
        .toHaveBeenCalledWith([], {
          relativeTo: route,
          queryParams: { textSearch: 'kubernetes',
            tag: ['kubernetes',
              'CI/CD'] },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
    });

    it('should restore "aws" into the search form when provided via input (simulating ?textSearch=aws)', () => {
      setInputs([], { textSearch: 'aws' });

      expect(component.filterFormGroup.value.textSearch)
        .toBe('aws');
    });
  });
  describe('Table Custom Filter Predicate', () => {
    it('should correctly filter certificates in GenericTableDataSource', () => {
      setInputs(undefined, { textSearch: 'aws',
        tags: [] });
      expect(component.table.filteredData)
        .toEqual([certificateType1]);

      setInputs(undefined, { textSearch: 'ruby',
        tags: [] });
      expect(component.table.filteredData)
        .toEqual([certificateType2]);

      setInputs(undefined, { textSearch: '10',
        tags: [] });
      expect(component.table.filteredData)
        .toEqual([certificateType1]);

      setInputs(undefined, { textSearch: '15',
        tags: [] });
      expect(component.table.filteredData)
        .toEqual([certificateType2]);

      setInputs(undefined, { textSearch: 'gitlab',
        tags: [] });
      expect(component.table.filteredData)
        .toEqual([certificateType1,
          certificateType3]);

      setInputs(undefined, { textSearch: '',
        tags: ['GitLab',
          'AWS'] });
      expect(component.table.filteredData)
        .toEqual([certificateType1,
          certificateType3]);

      setInputs(undefined, { textSearch: '',
        tags: ['GitLab'] });
      expect(component.table.filteredData)
        .toEqual([certificateType1,
          certificateType3]);
    });
  });

  describe('Tag Filter', () => {
    it('should get all unique tags sorted alphabetically', () => {
      const allTagsSorted = allTags.sort();

      setInputs(undefined, { textSearch: '',
        tags: [] });
      expect(component.tags())
        .toEqual(allTagsSorted);
    });

    it('should remove tag on method call', () => {
      const tag = allTags[0];

      setInputs(undefined, { textSearch: '',
        tags: [] });
      component.filterFormGroup.controls.tags.setValue([tag]);

      expect(component.filterFormGroup.controls.tags.value)
        .toEqual([tag]);

      component['removeTag'](tag);

      expect(component.filterFormGroup.controls.tags.value)
        .toEqual([]);
    });
  });
});
