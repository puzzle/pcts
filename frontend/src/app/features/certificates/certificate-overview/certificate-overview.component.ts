import { Component, effect, inject, input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
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

const getCertificateOverviewTable = () => new GenericTableDataSource(getCertificateOverviewColumns())
  .withDetailViewLink()
  .withLimit(3)
  .withCustomFilterPredicate((cert: CertificateTypeModel, filter: string) => {
    const filterValues = JSON.parse(filter);
    const searchTxt = (filterValues.text || '').toLowerCase();

    return filterMultipleFields(cert, searchTxt, ['name',
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

  searchTerm = input.required<string | null>();

  table = getCertificateOverviewTable();

  searchControl = new FormControl('');

  constructor() {
    effect(() => {
      const certs = this.certificates();
      if (certs) {
        this.table.data = certs;
        this.applyFilterString();
      }
    });

    effect(() => {
      this.searchControl.setValue(this.searchTerm());
    });

    this.searchControl.valueChanges
      .pipe(debounceTime(300))
      .subscribe(() => {
        this.applyFilterString();
        this.updateUrl();
      });
  }

  private applyFilterString(): void {
    this.table.filter = JSON.stringify({
      text: this.searchControl.value ?? ''
    });
  }

  private updateUrl(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        q: this.searchControl.value ? this.searchControl.value : null
      },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
}
