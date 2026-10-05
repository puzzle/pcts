import { Component, effect, input, signal } from '@angular/core';
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

const getCertificateOverviewTable = () => new GenericTableDataSource(getCertificateOverviewColumns())
  .withLimit(10)
  .withDetailViewLink();

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
  certificates = input.required<CertificateTypeModel[]>();

  table = getCertificateOverviewTable();

  searchControl = new FormControl('');
  searchTerm = signal('');

  constructor() {
    this.searchControl.valueChanges
      .pipe(debounceTime(300))
      .subscribe((value) => {
        this.searchTerm.set((value || '').toLowerCase());
      });

    effect(() => {
      const certs = this.certificates();
      const term = this.searchTerm();

      if (certs) {
        if (!term) {
          this.table.data = certs;
        } else {
          this.table.data = certs.filter((cert) => {
            const certDataString = ((cert.name || '')).toLowerCase();

            const searchTerms: string[] = term.split(' ').filter(Boolean);
            return searchTerms.every((t) => certDataString.includes(t));
          });
        }
      }
    });
  }
}
