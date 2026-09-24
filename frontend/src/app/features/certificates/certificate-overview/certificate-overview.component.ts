import { Component, computed, effect, inject } from '@angular/core';
import { GenericTableComponent } from '../../../shared/generic-table/generic-table.component';
import { TypedTemplateDirective } from '../../../shared/generic-table/type-template/typed-template.directive';
import { RowDetailTemplateDirective } from '../../../shared/generic-table/rowDetailTemplate.directive';
import { GenCol, GenericTableDataSource } from '../../../shared/generic-table/generic-table-data-source';
import { CertificateOverviewModel } from './certificateOverviewModel';
import { CertificateTypeModel } from '../certificate-type/certificate-type.model';
import { ColumnTemplateDirective } from '../../../shared/generic-table/column-template/column-template.directive';
import { ExperienceTypePillComponent } from '../../../shared/experience-type-pill/experience-type-pill.component';
import { ActivatedRoute } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { TranslatePipe } from '@ngx-translate/core';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { CertificateDetailViewComponent } from './certificate-detail-view/certificate-detail-view.component';

@Component({
  imports: [
    GenericTableComponent,
    TypedTemplateDirective,
    RowDetailTemplateDirective,
    ColumnTemplateDirective,
    ExperienceTypePillComponent,
    TranslatePipe,
    ScopedTranslationPipe,
    CertificateDetailViewComponent
  ],
  selector: 'app-certificate.component',
  styleUrl: './certificate-overview.component.scss',
  templateUrl: './certificate-overview.component.html'
})
export class CertificateOverviewComponent {
  private readonly route = inject(ActivatedRoute);

  private data = toSignal(this.route.data);

  table = getCertificateOverviewTable();

  certificates = computed(() => this.data()?.['certificateOverview'] as CertificateOverviewModel[]);


  constructor() {
    effect(() => {
      const certs = this.certificates();
      if (certs) {
        this.table.data = certs;
      }
    });
  }

  mapToOverviewModel(certificate: CertificateTypeModel): CertificateOverviewModel {
    return {
      id: certificate.id,
      points: certificate.points,
      name: certificate.name,
      publisher: certificate.publisher,
      tags: certificate.tags
    };
  }
}

const getCertificateOverviewTable = () => new GenericTableDataSource(getCertificateOverviewColumns())
  .withLimit(10)
  .withDetailViewLink();

const getCertificateOverviewColumns = (): GenCol<CertificateOverviewModel>[] => [
  GenCol.fromAttr('name'),
  GenCol.fromAttr('publisher'),
  GenCol.fromAttr('points'),
  GenCol.fromAttr('tags')
];
