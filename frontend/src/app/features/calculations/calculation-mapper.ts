import { toDegreeCalculationModel, toDegreeCalculationDto } from './degree-calculation/degree-calculation-mapper';
import { CalculationModel, CalculationDto } from './calculationDto';
import { CalculationInputDto } from './dto/calculation.dto';
import { Relevancy } from './relevancy.enum';

export function toCalculationModel(dto: CalculationDto): CalculationModel {
  return {
    ...dto,
    degreeCalculations: dto.degreeCalculations.map((degreeDto) => toDegreeCalculationModel(degreeDto))
  };
}

export function toCalculationDto(model: CalculationModel): CalculationDto {
  return {
    ...model,
    degreeCalculations: model.degreeCalculations.map(toDegreeCalculationDto)
  };
}

export function toCalculationInputDto(model: CalculationModel): CalculationInputDto {
  return {
    memberId: model.member.id,
    state: model.state,
    roleId: model.role.id,
    certificateCalculations: model.certificateCalculations.map((certificateCalculation) => {
      return {
        id: certificateCalculation.id,
        certificateId: certificateCalculation.certificate.id
      };
    }),
    degreeCalculations: model.degreeCalculations.map((degreeCalculation) => {
      const dto = toDegreeCalculationDto(degreeCalculation);

      return {
        id: dto.id,
        degreeId: dto.degree.id,
        relevancies: dto.relevancies as Record<Relevancy, number>,
        comment: degreeCalculation.comment
      };
    }),
    experienceCalculations: model.experienceCalculations.map((experienceCalculation) => {
      return {
        id: experienceCalculation.id,
        experienceId: experienceCalculation.experience.id,
        comment: experienceCalculation.comment,
        relevancy: experienceCalculation.relevancy
      };
    }),
    leadershipExperienceCalculations: model.leadershipExperienceCalculations.map((leadershipExperienceCalculation) => {
      return {
        id: model.id,
        leadershipExperienceId: leadershipExperienceCalculation.leadershipExperience.id
      };
    })
  };
}
