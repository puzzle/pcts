const nestedProperty = (data: any, sortHeaderId: string): string | number => {
  return sortHeaderId.split('.')
    .reduce((accumulator, key) => accumulator && accumulator[key], data) as string | number;
};

const sortingDataAccessor = {
  nestedProperty
};

export default sortingDataAccessor;
