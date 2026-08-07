const SEGMENT_SEPARATOR = '.';

/** A prefix, or any truncation of one, as ordered segments. */
type Scopes = readonly string[];

function qualify(scope: Scopes, key: string): string {
  return [...scope,
    key].join(SEGMENT_SEPARATOR);
}

/**
 * One position within a prefix, and the two equally specific scopes that
 * position marks the boundary of.
 *
 * Within `MEMBER.CV.DEGREE`, position 1 (`CV`) yields:
 *   hoisted     -> CV.DEGREE   (the two innermost segments)
 *   generalized -> MEMBER.CV   (the two outermost segments)
 *
 * Both retain two of three segments, so neither may outrank the other.
 */
class TranslatePathSegment {
  public constructor(private readonly scope: Scopes,
    private readonly index: number) {}

  /** The segment this position is anchored on. */
  public get segment(): string {
    return this.scope[this.index];
  }

  /** How many segments survive here — the same count in either direction. */
  private get survivingCount(): number {
    return this.scope.length - this.index;
  }

  /** Sheds the outermost segments: MEMBER.CV.DEGREE -> CV.DEGREE -> DEGREE */
  public hoistedCandidate(key: string): string {
    return qualify(this.scope.slice(this.index), key);
  }

  /** Sheds the innermost segments: MEMBER.CV.DEGREE -> MEMBER.CV -> MEMBER */
  public generalizedCandidate(key: string): string {
    return qualify(this.scope.slice(0, this.survivingCount), key);
  }
}

/**
 * An i18n scope prefix such as `MEMBER.CV.DEGREE`, and the candidate keys
 * derivable from it.
 *
 * Empty segments are dropped, so `A..B` and `A.B.` both normalize to `A.B`.
 */
export class TranslationKeyPath {
  private readonly segments: readonly TranslatePathSegment[];

  private constructor(private readonly scope: Scopes) {
    this.segments = scope.map((_, index) => new TranslatePathSegment(scope, index));
  }

  public static fromPrefix(prefix: string): TranslationKeyPath {
    const scope = prefix
      .split(SEGMENT_SEPARATOR)
      .filter((segment) => segment.length > 0);

    return new TranslationKeyPath(scope);
  }

  /**
   * Every key worth trying for `key`, most specific first.
   *
   * `MEMBER.CV.DEGREE` + `MODEL_NAME` yields, in order:
   *   MEMBER.CV.DEGREE.MODEL_NAME
   *   CV.DEGREE.MODEL_NAME
   *   MEMBER.CV.MODEL_NAME
   *   DEGREE.MODEL_NAME
   *   MEMBER.MODEL_NAME
   *   MODEL_NAME
   *
   * Offering both of a position's candidates before moving to the next is what
   * stops a generic key from outranking a specific one. The bare key is
   * appended last: no segment survives there, so no position can represent it.
   */
  public candidatesFor(key: string): readonly string[] {
    const candidates = [...this.segments.flatMap((segment) => [segment.hoistedCandidate(key),
      segment.generalizedCandidate(key)]),
    key];

    return Object.freeze([...new Set(candidates)]);
  }

  /** The unabbreviated key, rendered verbatim when nothing resolves. */
  public fullyQualified(key: string): string {
    return qualify(this.scope, key);
  }
}
