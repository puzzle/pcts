import { toDegreeCalculationDto, toDegreeCalculationModel } from './degree-calculation-mapper';
import { degree1, degreeCalculation1 } from '../../../shared/test/test-data';
import { DegreeCalculationModel } from './degree-calculation.dto';
import { Relevancy } from '../relevancy.enum';

describe('DegreeCalculationMapper', () => {
  it('should map to degree calculation model correctly', () => {
    const expectedModel: DegreeCalculationModel = {
      id: 1,
      degree: degree1,
      comment: null,
      relevancies: {
        [Relevancy.STRONGLY]: { weight: 100,
          checked: true },
        [Relevancy.NORMAL]: { weight: 0,
          checked: false },
        [Relevancy.POORLY]: { weight: 0,
          checked: false }
      }
    };

    expect(toDegreeCalculationModel(degreeCalculation1))
      .toEqual(expectedModel);
  });

  it('should map to degree calculation dto correctly', () => {
    const input: DegreeCalculationModel = {
      id: 1,
      degree: degree1,
      comment: null,
      relevancies: {
        [Relevancy.STRONGLY]: { weight: 100,
          checked: true },
        [Relevancy.NORMAL]: { weight: 0,
          checked: false },
        [Relevancy.POORLY]: { weight: 0,
          checked: false }
      }
    };

    const expectedResult = {
      ...degreeCalculation1,
      relevancies: new Map([[Relevancy.STRONGLY,
        100],
      [Relevancy.NORMAL,
        0],
      [Relevancy.POORLY,
        0]])
    };
    expect(toDegreeCalculationDto(input))
      .toEqual(expectedResult);
  });
});
