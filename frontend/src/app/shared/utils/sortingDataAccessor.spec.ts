import sortingDataAccessor from './sortingDataAccessor';
import { member1 } from '../test/test-data';

describe('nestedProperty with MemberModel', () => {
  const { nestedProperty } = sortingDataAccessor;

  it('should return nested property value', () => {
    expect(nestedProperty(member1, 'organisationUnit.name'))
      .toBe('/mem');
  });

  it('should return a top-level string property', () => {
    expect(nestedProperty(member1, 'firstName'))
      .toBe('Lena');
    expect(nestedProperty(member1, 'lastName'))
      .toBe('Müller');
  });

  it('should return a top-level number property', () => {
    expect(nestedProperty(member1, 'id'))
      .toBe(1);
  });

  it('should return a top-level DateTime property in milliseconds', () => {
    expect(nestedProperty(member1, 'birthDate'))
      .toBe(member1.birthDate.toMillis());
  });

  it('should return empty string for missing property', () => {
    expect(nestedProperty(member1, 'nonExistent'))
      .toBe('');
  });

  it('should return empty string when accessing a nested object directly', () => {
    expect(nestedProperty(member1, 'organisationUnit'))
      .toBe('');
  });

  it('should return a top-level normal Date property in milliseconds', () => {
    const data = {
      normalDate: new Date('2020-05-20T10:30:00Z')
    };

    expect(nestedProperty(data, 'normalDate'))
      .toBe(data.normalDate.getTime());
  });
});
