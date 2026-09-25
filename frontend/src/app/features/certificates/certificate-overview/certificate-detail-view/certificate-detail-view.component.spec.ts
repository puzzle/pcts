import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CertificateDetailViewComponent } from './certificate-detail-view.component';

describe('CertificateDetailViewComponent', () => {
  let component: CertificateDetailViewComponent;
  let fixture: ComponentFixture<CertificateDetailViewComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CertificateDetailViewComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CertificateDetailViewComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
