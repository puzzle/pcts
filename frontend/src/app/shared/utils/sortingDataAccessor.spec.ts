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

  it('should return empty string for missing property', () => {
    expect(nestedProperty(member1, 'nonExistent'))
      .toBe('');
  });

  it('should return empty string for nested object', () => {
    expect(nestedProperty(member1, 'organisationUnit'))
      .toBe('');
  });
});
