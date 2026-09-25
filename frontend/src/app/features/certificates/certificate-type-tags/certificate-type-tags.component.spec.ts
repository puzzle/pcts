import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CertificateTypeTagsComponent } from './certificate-type-tags.component';

describe('CertificateTypeTagsComponent', () => {
  let component: CertificateTypeTagsComponent;
  let fixture: ComponentFixture<CertificateTypeTagsComponent>;

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      imports: [CertificateTypeTagsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CertificateTypeTagsComponent);
    component = fixture.componentInstance;

    fixture.componentRef.setInput('tags', [
      'B',
      'A',
      'D',
      'C'
    ]);

    fixture.detectChanges();
  });

  it('should sort the tags', () => {
    expect(component.sortedTags)
      .toEqual([
        'A',
        'B',
        'C',
        'D'
      ]);
  });
});
