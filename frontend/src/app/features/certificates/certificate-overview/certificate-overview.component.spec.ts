import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CertificateOverviewComponent } from './certificate-overview.component';
import { provideRouter } from '@angular/router';

describe('CertificateOverviewComponent', () => {
  let component: CertificateOverviewComponent;
  let fixture: ComponentFixture<CertificateOverviewComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CertificateOverviewComponent],
      providers: [provideRouter([])]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CertificateOverviewComponent);
    fixture.componentRef.setInput('certificates', []);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
