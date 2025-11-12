const nestedProperty = <T extends object>(data: T, sortHeaderId: string): string | number => {
  if (!data || typeof data !== 'object') {
    return '';
  }

  const value = sortHeaderId
    .split('.')
    .reduce<any>((acc, key) => {
      if (acc && typeof acc === 'object') {
        return acc[key];
      }
      return undefined;
    }, data);

  if (typeof value === 'string' || typeof value === 'number') {
    return value;
  }

  return '';
};

const sortingDataAccessor = {
  nestedProperty
};

export default sortingDataAccessor;
