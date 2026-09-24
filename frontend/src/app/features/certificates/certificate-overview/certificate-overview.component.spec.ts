import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CertificateOverviewComponent } from './certificate-overview.component';

describe('CertificateComponent', () => {
  let component: CertificateOverviewComponent;
  let fixture: ComponentFixture<CertificateOverviewComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CertificateOverviewComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CertificateOverviewComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
