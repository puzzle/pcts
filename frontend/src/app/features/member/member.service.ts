import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { HttpClient } from '@angular/common/http';
import { map, Observable, of } from 'rxjs';
import { MemberDto } from './dto/member.dto';
import { DateTime } from 'luxon';
import { MemberOverviewModel } from './member-overview.model';
import { EmploymentState } from '../../shared/enum/employment-state.enum';
import { CertificateKind } from '../certificates/certificate-type/certificate-kind.enum';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/members';

  getAllMembers(): Observable<MemberModel[]> {
    return this.httpClient.get<MemberModel[]>(this.API_URL)
      .pipe(map((dtos) => dtos.map((dto) => this.mapDates(dto))));
  }

  getMemberById(id: number): Observable<MemberModel> {
    return this.httpClient.get<MemberModel>(`${this.API_URL}/${id}`)
      .pipe(map((dto) => this.mapDates(dto)));
  }

  addMember(member: MemberModel): Observable<MemberModel> {
    return this.httpClient.post<MemberModel>(this.API_URL, this.toDto(member))
      .pipe(map((dto) => this.mapDates(dto)));
  }

  updateMember(id: number, member: MemberModel): Observable<MemberModel> {
    return this.httpClient.put<MemberModel>(`${this.API_URL}/${id}`, this.toDto(member))
      .pipe(map((dto) => this.mapDates(dto)));
  }

  private mapDates(dto: MemberModel): MemberModel {
    return {
      ...dto,
      birthDate: dto.birthDate ? new Date(dto.birthDate) : null!,
      dateOfHire: dto.dateOfHire ? new Date(dto.dateOfHire) : null!
    };
  }

  toDto(model: MemberModel): MemberDto {
    return {
      firstName: model.firstName,
      lastName: model.lastName,
      birthDate: DateTime.fromJSDate(model.birthDate)
        .toISODate(),
      abbreviation: model.abbreviation,
      employmentState: model.employmentState,
      organisationUnitId: model.organisationUnit?.id,
      dateOfHire: model.dateOfHire ? DateTime.fromJSDate(model.dateOfHire)
        .toISODate() : null
    };
  }

  getMemberOverviewByMemberId(id: number): Observable<MemberOverviewModel> {
    return of({
      member: {
        id: 1,
        firstName: 'Lena',
        lastName: 'Müller',
        employmentState: EmploymentState.MEMBER,
        abbreviation: 'LM',
        dateOfHire: new Date(),
        birthDate: new Date(),
        organisationUnit: {
          id: 1,
          name: '/zh'
        }
      },
      cv: {
        degrees: [{
          id: 1,
          name: 'Bachelor of Science in Mathematics',
          degreeTypeName: 'Bachelor\'s Degree',
          startDate: new Date(),
          endDate: new Date()
        }],
        experiences: [{
          id: 1,
          name: 'Software Engineer',
          employer: 'TechNova Solutions',
          experienceTypeName: 'Internship',
          comment: 'Worked on backend APIs and DevOps tasks.',
          startDate: new Date(),
          endDate: new Date()
        },
        {
          id: 3,
          name: 'Web Developer (Freelance)',
          employer: 'Freelance',
          experienceTypeName: 'Hackathon',
          comment: null,
          startDate: new Date(),
          endDate: new Date()
        }],
        certificates: [{
          id: 1,
          certificateTypeName: 'CompTIA A+',
          completedAt: new Date(),
          comment: 'Completed first aid training.'
        }],
        leadershipExperiences: [{
          id: 1,
          comment: 'LeadershipExperience',
          leadershipExperienceType: {
            name: 'Leadership Experience',
            certificateKind: CertificateKind.LEADERSHIP_TRAINING
          }
        }]
      }
    });
  }
}
