import { TestBed } from '@angular/core/testing';
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { certificateTypeResolver } from './certificate-type.resolver';
import { CertificateTypeModel } from './certificate-type/certificate-type.model';
import { CertificateTypeService } from './certificate-type/certificate-type.service';
import { of } from 'rxjs';

describe('certificateResolver', () => {
  const executeResolver: ResolveFn<CertificateTypeModel[]> = (...resolverParameters) => TestBed.runInInjectionContext(() => certificateTypeResolver(...resolverParameters));
  const routeSnapshotMock: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot;
  const stateMock: RouterStateSnapshot = {} as RouterStateSnapshot;
  const certificateTypeServiceMock = {
    getAllCertificateTypes: jest.fn()
      .mockReturnValue(of([]))
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{ provide: CertificateTypeService,
        useValue: certificateTypeServiceMock }]
    });
  });

  it('should be created', () => {
    expect(executeResolver(routeSnapshotMock, stateMock))
      .toBeTruthy();
  });

  it('should call all certificate types', () => {
    executeResolver(routeSnapshotMock, stateMock);
    expect(certificateTypeServiceMock.getAllCertificateTypes)
      .toHaveBeenCalled();
  });
});
