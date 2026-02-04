export function camelToSnake(camelString: string): string {
  return camelString.replaceAll(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`)
    .toUpperCase();
}
