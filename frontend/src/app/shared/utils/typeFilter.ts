/**
 * Filters an array of objects based on a specific string property (case-insensitive).
 * If the search value is empty, it returns the original array.
 *
 * @param value - The search string.
 * @param options - The array of objects to filter.
 * @param attr - The key of the object to search in (TypeScript restricts this to string properties only).
 * @returns The filtered array.
 */
export function filterType<T, K extends { [P in keyof T]: T[P] extends string ? P : never }[keyof T]>(
  value: string,
  options: T[],
  attr: K
): T[] {
  if (!value) {
    return options;
  }

  return options.filter((option) => (option[attr] as string).toLowerCase()
    .includes(value.toLowerCase()));
}
