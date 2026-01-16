package ch.puzzle.pcts.util;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestData {

    private TestData() {
    }

    public static final LocalDate DATE_NOW = LocalDate.now();
    public static final LocalDate DATE_YESTERDAY = DATE_NOW.minusDays(1);
    public static final LocalDate DATE_TOMORROW = DATE_NOW.plusDays(1);

    public static final BigDecimal POSITIVE_BIG_DECIMAL = BigDecimal.valueOf(1);
    public static final BigDecimal NEGATIVE_BIG_DECIMAL = BigDecimal.valueOf(-1);

    public static final String VALID_STRING = "Valid String";
    public static final String TOO_LONG_STRING = "a".repeat(251);

    public static final Long INVALID_ID = 999L;

    public static final Long GENERIC_1_ID = 1L;
    public static final Long GENERIC_2_ID = 2L;

    public static final Long ORG_UNIT_1_ID = 1L;
    public static final Long ORG_UNIT_2_ID = 2L;

    public static final Long ROLE_1_ID = 1L;
    public static final Long ROLE_2_ID = 2L;

    public static final Long TAG_1_ID = 1L;
    public static final Long TAG_2_ID = 2L;

    public static final Long MEMBER_1_ID = 1L;
    public static final Long MEMBER_2_ID = 2L;

    public static final Long CERT_TYPE_1_ID = 1L;
    public static final Long CERT_TYPE_2_ID = 2L;
    public static final Long CERT_TYPE_3_ID = 3L;
    public static final Long CERT_TYPE_4_ID = 4L;
    public static final Long CERT_TYPE_5_ID = 5L;
    public static final Long CERT_TYPE_6_ID = 6L;

    public static final Long LEADERSHIP_TYPE_1_ID = 5L;
    public static final Long LEADERSHIP_TYPE_2_ID = 6L;
    public static final Long LEADERSHIP_TYPE_3_ID = 7L;
    public static final Long LEADERSHIP_TYPE_4_ID = 8L;
    public static final Long LEADERSHIP_TYPE_5_ID = 9L;

    public static final Long CERTIFICATE_1_ID = 1L;
    public static final Long CERTIFICATE_2_ID = 2L;
    public static final Long CERTIFICATE_3_ID = 3L;
    public static final Long CERTIFICATE_4_ID = 4L;

    public static final Long LEADERSHIP_CERT_1_ID = 1L;
    public static final Long LEADERSHIP_CERT_2_ID = 2L;

    public static final Long DEGREE_TYPE_1_ID = 1L;
    public static final Long DEGREE_TYPE_2_ID = 2L;

    public static final Long DEGREE_1_ID = 1L;
    public static final Long DEGREE_2_ID = 2L;

    public static final Long EXP_TYPE_1_ID = 1L;
    public static final Long EXP_TYPE_2_ID = 2L;

    public static final Long EXPERIENCE_1_ID = 1L;
    public static final Long EXPERIENCE_2_ID = 2L;
    public static final Long EXPERIENCE_3_ID = 3L;

    public static final Long CALCULATION_1_ID = 1L;
    public static final Long CALCULATION_2_ID = 2L;
    public static final Long CALCULATION_3_ID = 3L;
}
