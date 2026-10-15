import { Component, computed, effect, inject, input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { debounceTime } from 'rxjs/operators';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { TypedTemplateDirective } from '../../../shared/generic-table/type-template/typed-template.directive';
import { RowDetailTemplateDirective } from '../../../shared/generic-table/rowDetailTemplate.directive';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { ColumnTemplateDirective } from '../../../shared/generic-table/column-template/column-template.directive';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CertificateDetailViewComponent } from './certificate-detail-view/certificate-detail-view.component';
import { CertificateTypeModel } from '../certificate-type/certificate-type.model';
import { CertificateTypeTagsComponent } from '../certificate-type-tags/certificate-type-tags.component';
import { ActivatedRoute, Router } from '@angular/router';
import { filterMultipleFields } from '../../../shared/utils/typeFilter';
import { CertificateFilter } from './certificateFilter';
import { InfoIconComponent } from '../../../shared/icon-tooltip/info-icon/info-icon.component';
import { MatSelect, MatSelectTrigger } from '@angular/material/select';
import { MatOption } from '@angular/material/autocomplete';
import { MatChipGrid, MatChipRemove, MatChipRow } from '@angular/material/chips';

const getCertificateOverviewTable = () => new GenericTableDataSource<CertificateTypeModel, CertificateFilter>(getCertificateOverviewColumns())
  .withDetailViewLink()
  .addFilterPredicate((cert: CertificateTypeModel, filter: CertificateFilter) => {
    const textSearch = filter.textSearch;
    return filterMultipleFields(cert, textSearch, ['name',
      'publisher',
      'points']);
  })
  .addFilterPredicate((cert: CertificateTypeModel, filter: CertificateFilter) => {
    if (filter.tags?.length === 0) {
      return true;
    }

    return filter.tags?.some((tag) => cert.tags.includes(tag)) ?? false;
  });

const getCertificateOverviewColumns = (): GenCol<CertificateTypeModel>[] => [
  GenCol.fromAttr('name'),
  GenCol.fromAttr('publisher'),
  GenCol.fromAttr('points'),
  GenCol.fromAttr('tags')
];

@Component({
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIcon,
    GenericTableComponent,
    TypedTemplateDirective,
    RowDetailTemplateDirective,
    ColumnTemplateDirective,
    ScopedTranslationPipe,
    CertificateDetailViewComponent,
    CertificateTypeTagsComponent,
    MatSelect,
    MatSelectTrigger,
    MatOption,
    MatChipGrid,
    MatChipRow,
    MatChipRemove,
    MatSelect,
    InfoIconComponent
  ],
  selector: 'app-certificate',
  templateUrl: './certificate-overview.component.html'
})
class CertificateOverviewComponent {
  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  certificates = input.required<CertificateTypeModel[]>();

  filter = input.required<CertificateFilter>();

  table = getCertificateOverviewTable();

  filterFormGroup = new FormGroup({
    textSearch: new FormControl<string>(''),
    tags: new FormControl<string[] | null>([])
  });

  tags = computed(() => {
    const allUniqueTags = this.getAllUniqueTags(this.certificates());

    return this.sortArrayAlphabetically(allUniqueTags);
  });

  constructor() {
    effect(() => {
      this.table.data = this.certificates();
    });

    effect(() => {
      const currentSearch = this.filter();

      // Tags that are in the url, but they don't exist in the table should be ignored
      currentSearch.tags = currentSearch.tags?.filter((tag) => this.tags()
        .includes(tag)) ?? null;

      if (this.filterFormGroup.controls.textSearch.value !== currentSearch.textSearch) {
        /*
         * We use `emitEvent: false` here because otherwise `valueChanges` would be executed again even though it has already run before.
         * This is for when you click on a link to the certificate overview but with query parameters—in that case,
         * the URL is already set correctly, so we prevent the updateUrl from being executed again for no reason.
         *
         */
        this.filterFormGroup.setValue(currentSearch, { emitEvent: false });
      }

      this.applyFilter(currentSearch);
    });

    this.filterFormGroup.controls.textSearch.valueChanges
      .pipe(debounceTime(300))
      .subscribe(() => {
        this.updateUrl(this.filterFormGroup.getRawValue());
      });

    this.filterFormGroup.controls.tags.valueChanges
      .subscribe(() => {
        this.updateUrl(this.filterFormGroup.getRawValue());
      });
  }

  private applyFilter(filter: CertificateFilter): void {
    this.table.setFilter(filter);
  }

  protected removeTag(tag: string): void {
    const selectedTags = this.filterFormGroup.controls.tags.value;

    this.filterFormGroup.controls.tags.setValue(selectedTags?.filter((selectedTag) => selectedTag !== tag) || []);
  }

  private updateUrl(filter: Partial<CertificateFilter>): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        textSearch: filter.textSearch?.trim() || null,
        tag: filter.tags
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }

  private sortArrayAlphabetically(array: string[]) {
    return array.sort((a, b) => a.localeCompare(b));
  }

  private getAllUniqueTags(certificates: CertificateTypeModel[]) {
    const allTags = certificates
      .flatMap((cert) => cert.tags);

    return [...new Set(allTags)];
  }
}

export default CertificateOverviewComponent;
