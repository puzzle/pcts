import { Component, computed, effect, inject } from '@angular/core';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { TypedTemplateDirective } from '../../../shared/generic-table/type-template/typed-template.directive';
import { RowDetailTemplateDirective } from '../../../shared/generic-table/rowDetailTemplate.directive';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { ColumnTemplateDirective } from '../../../shared/generic-table/column-template/column-template.directive';
import { ActivatedRoute } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CertificateDetailViewComponent } from './certificate-detail-view/certificate-detail-view.component';
import { CertificateTypeModel } from '../certificate-type/certificate-type.model';

@Component({
  imports: [
    GenericTableComponent,
    TypedTemplateDirective,
    RowDetailTemplateDirective,
    ColumnTemplateDirective,
    ScopedTranslationPipe,
    CertificateDetailViewComponent
  ],
  selector: 'app-certificate.component',
  styleUrl: './certificate-overview.component.scss',
  templateUrl: './certificate-overview.component.html'
})
export class CertificateOverviewComponent {
  private readonly route = inject(ActivatedRoute);

  private readonly data = toSignal(this.route.data);

  table = getCertificateOverviewTable();

  certificates = computed(() => this.data()?.['certificates'] as CertificateTypeModel[]);


  constructor() {
    effect(() => {
      const certs = this.certificates();
      if (certs) {
        this.table.data = certs;
      }
    });
  }
}

const getCertificateOverviewTable = () => new GenericTableDataSource(getCertificateOverviewColumns())
  .withLimit(10)
  .withDetailViewLink();

const getCertificateOverviewColumns = (): GenCol<CertificateTypeModel>[] => [
  GenCol.fromAttr('name'),
  GenCol.fromAttr('publisher'),
  GenCol.fromAttr('points'),
  GenCol.fromAttr('tags')
];
