import { Component, effect, inject, input } from '@angular/core';
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
import { CertificateFilter } from './certificate-detail-view/certificateFilter';

const getCertificateOverviewTable = () => new GenericTableDataSource(getCertificateOverviewColumns())
  .withDetailViewLink()
  .withLimit(3)
  .withCustomFilterPredicate((cert: CertificateTypeModel, filter: string) => {
    const filterValues = JSON.parse(filter);
    const textSearch = (filterValues.textSearch || '').toLowerCase();

    return filterMultipleFields(cert, textSearch, ['name',
      'publisher',
      'points']);
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
    CertificateTypeTagsComponent
  ],
  selector: 'app-certificate.component',
  templateUrl: './certificate-overview.component.html'
})
export class CertificateOverviewComponent {
  private readonly router = inject(Router);

  private readonly route = inject(ActivatedRoute);

  certificates = input.required<CertificateTypeModel[]>();

  filter = input.required<CertificateFilter>();

  table = getCertificateOverviewTable();

  filterFormGroup = new FormGroup({
    textSearch: new FormControl<string>('')
  });

  constructor() {
    effect(() => {
      this.table.data = this.certificates();
    });

    effect(() => {
      const currentSearch = this.filter() ?? '';

      if (this.filterFormGroup.getRawValue() !== currentSearch) {
        /*
         * We use `emitEvent: false` here because otherwise `valueChanges` would be executed again even though it has already run before.
         * If you look at the entire workflow, it would go through the process twice.
         */
        this.filterFormGroup.setValue(currentSearch, { emitEvent: false });
      }

      this.applyFilter(currentSearch);
    });

    this.filterFormGroup.valueChanges
      .pipe(debounceTime(300))
      .subscribe((textSearch) => {
        this.updateUrl(textSearch);
      });
  }

  private applyFilter(filter: CertificateFilter): void {
    this.table.filter = JSON.stringify(filter);
  }

  private updateUrl(queries: Partial<CertificateFilter>): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        q: queries.textSearch
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
}
