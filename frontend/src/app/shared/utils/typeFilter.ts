export function filterType<T, K extends { [P in keyof T]: T[P] extends string ? P : never }[keyof T]>(value: string, options: T[], attr: K): T[] {
  if (!value) return options;

  return options.filter((option) =>
    (option[attr] as string).toLowerCase().includes(value.toLowerCase())
  );
}
