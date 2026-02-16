package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.ExamType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateTypeValidationServiceTest
        extends
            ValidationBaseServiceTest<CertificateType, CertificateTypeValidationService> {

    @Mock
    private CertificateTypePersistenceService persistenceService;

    @InjectMocks
    CertificateTypeValidationService service;

    @Override
    CertificateType getValidModel() {
        return CertificateType.Builder
                .builder()
                .withName("CertificateType")
                .withPoints(BigDecimal.valueOf(10))
                .withComment("Comment")
                .withTags(Set.of(TAG_3))
                .withCertificateKind(CertificateKind.CERTIFICATE)
                .withEffort(6D)
                .withExamDuration(60)
                .withLink("https://www.example.com")
                .withExamType(ExamType.PRACTICAL)
                .withPublisher("Works on my machine GMBH")
                .build();
    }

    @Override
    CertificateTypeValidationService getService() {
        return service;
    }

    private static CertificateType createCertificateType(String name, BigDecimal points, Double effort,
                                                         Integer examDuration, String link, ExamType examType,
                                                         String publisher) {
        CertificateType c = new CertificateType();
        c.setName(name);
        c.setPoints(points);
        c.setComment("Comment");
        c.setTags(Set.of(TAG_1));
        c.setCertificateKind(CertificateKind.CERTIFICATE);
        c.setEffort(effort);
        c.setExamDuration(examDuration);
        c.setLink(link);
        c.setExamType(examType);
        c.setPublisher(publisher);

        return c;
    }

    // TODO: Re-implement the commented out methods once LeadershipexperienceTypes
    // are handled separately

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createCertificateType(null,
                                                  POSITIVE_BIG_DECIMAL,
                                                  10D,
                                                  60,
                                                  "https://www.example.com",
                                                  ExamType.PRACTICAL,
                                                  "Publisher"),
                            List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificateType("",
                                                      POSITIVE_BIG_DECIMAL,
                                                      10D,
                                                      60,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createCertificateType("h",
                                                      POSITIVE_BIG_DECIMAL,
                                                      10D,
                                                      60,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "h"))),
                    Arguments
                            .of(createCertificateType(TOO_LONG_STRING,
                                                      POSITIVE_BIG_DECIMAL,
                                                      10D,
                                                      60,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    TOO_LONG_STRING))),
                    Arguments
                            .of(createCertificateType("Name",
                                                      null,
                                                      10D,
                                                      60,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "points"))),
                    Arguments
                            .of(createCertificateType("Name",
                                                      BigDecimal.valueOf(-1),
                                                      10D,
                                                      60,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "points",
                                                    FieldKey.IS,
                                                    "-1"))),
                    // Arguments
                    // .of(createCertificateType("Name",
                    // POSITIVE_BIG_DECIMAL,
                    // null,
                    // 60,
                    // "https://www.example.com",
                    // ExamType.PRACTICAL,
                    // "Publisher"),
                    // List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD,
                    // "effort"))),
                    Arguments
                            .of(createCertificateType("Name",
                                                      POSITIVE_BIG_DECIMAL,
                                                      -1D,
                                                      60,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "effort",
                                                    FieldKey.IS,
                                                    "-1.0"))),
                    Arguments
                            .of(createCertificateType("Name",
                                                      POSITIVE_BIG_DECIMAL,
                                                      10D,
                                                      -1,
                                                      "https://www.example.com",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "CertificateType",
                                                    FieldKey.FIELD,
                                                    "examDuration",
                                                    FieldKey.IS,
                                                    "-1"))),
                    Arguments
                            .of(createCertificateType("Name",
                                                      POSITIVE_BIG_DECIMAL,
                                                      10D,
                                                      60,
                                                      "not-a-link-whatsoever",
                                                      ExamType.PRACTICAL,
                                                      "Publisher"),
                                List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD, "link")))
                // Arguments
                // .of(createCertificateType("Name",
                // POSITIVE_BIG_DECIMAL,
                // 10D,
                // 60,
                // "https://www.example.com",
                // null,
                // "Publisher"),
                // List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD,
                // "examType"))),
                // Arguments
                // .of(createCertificateType("Name",
                // POSITIVE_BIG_DECIMAL,
                // 10D,
                // 60,
                // "https://www.example.com",
                // ExamType.PRACTICAL,
                // null),
                // List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD,
                // "publisher"))),
                // Arguments
                // .of(createCertificateType("Name",
                // POSITIVE_BIG_DECIMAL,
                // 10D,
                // 60,
                // "https://www.example.com",
                // ExamType.PRACTICAL,
                // ""),
                // List.of(Map.of(FieldKey.CLASS, "CertificateType", FieldKey.FIELD,
                // "publisher"))),
                // Arguments
                // .of(createCertificateType("Name",
                // POSITIVE_BIG_DECIMAL,
                // 10D,
                // 60,
                // "https://www.example.com",
                // ExamType.PRACTICAL,
                // "h"),
                // List
                // .of(Map
                // .of(FieldKey.CLASS,
                // "CertificateType",
                // FieldKey.FIELD,
                // "publisher",
                // FieldKey.MIN,
                // "2",
                // FieldKey.MAX,
                // "250",
                // FieldKey.IS,
                // "h"))),
                // Arguments
                // .of(createCertificateType("Name",
                // POSITIVE_BIG_DECIMAL,
                // 10D,
                // 60,
                // "https://www.example.com",
                // ExamType.PRACTICAL,
                // TOO_LONG_STRING),
                // List
                // .of(Map
                // .of(FieldKey.CLASS,
                // "CertificateType",
                // FieldKey.FIELD,
                // "publisher",
                // FieldKey.MIN,
                // "2",
                // FieldKey.MAX,
                // "250",
                // FieldKey.IS,
                // TOO_LONG_STRING)))
                );
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate kind is not certificate")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateKindIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateCertificateKind(CertificateKind.LEADERSHIP_TRAINING));
        assertEquals(List.of(ErrorKey.ATTRIBUTE_WRONG_KIND), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "certificateKind",
                            FieldKey.IS,
                            "LEADERSHIP_TRAINING",
                            FieldKey.ENTITY,
                            CERTIFICATE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        CertificateType leadershipExperience = getValidModel();

        when(persistenceService.getByName(leadershipExperience.getName()))
                .thenReturn(Optional.of(new CertificateType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperience));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "CertificateType", FieldKey.ENTITY, CERTIFICATE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        CertificateType certificateType = getValidModel();
        CertificateType newCertificateType = getValidModel();
        certificateType.setId(CERTIFICATE_2_ID);

        when(persistenceService.getByName(newCertificateType.getName())).thenReturn(Optional.of(certificateType));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(CERT_TYPE_1_ID, newCertificateType));

        assertEquals(List.of(ErrorKey.INVALID_ARGUMENT), exception.getErrorKeys());
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "CertificateType", FieldKey.ENTITY, CERTIFICATE_TYPE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        CertificateType certificateType = getValidModel();

        CertificateTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<CertificateType>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(certificateType);

        verify(spyService).validateOnCreate(certificateType);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        CertificateType certificate = getValidModel();

        CertificateTypeValidationService spyService = spy(service);
        doNothing().when((ValidationBase<CertificateType>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(CERT_TYPE_1_ID, certificate);

        verify(spyService).validateOnUpdate(CERT_TYPE_1_ID, certificate);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should validate uniqueness of name and publisher excluding ID")
    @Test
    void shouldNotThrowExceptionWhenNameAndPublisherAreUnique() {
        String name = "Unique Name";
        String publisher = "Unique Publisher";
        Long id = 100L;

        when(persistenceService.nameAndPublisherExcludingIdAlreadyUsed(name, publisher, id)).thenReturn(false);

        assertDoesNotThrow(() -> service.validateUniquenessOfNameAndPublisherExcludingId(name, publisher, id));
    }

    @DisplayName("Should throw exception when name and publisher already exist for another ID")
    @Test
    void shouldThrowExceptionWhenNameAndPublisherAlreadyExist() {
        String name = "Duplicate Name";
        String publisher = "Duplicate Publisher";
        Long id = 100L;

        when(persistenceService.nameAndPublisherExcludingIdAlreadyUsed(name, publisher, id)).thenReturn(true);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateUniquenessOfNameAndPublisherExcludingId(name,
                                                                                                        publisher,
                                                                                                        id));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name & publisher")), exception.getErrorAttributes());
    }

    @DisplayName("Should pass validation when ExamType is NONE and Duration is null")
    @Test
    void shouldNotThrowExceptionWhenExamTypeIsNoneAndDurationIsNull() {
        assertDoesNotThrow(() -> service.validateThatDurationIsNullWhenExamTypeIsNone(ExamType.NONE, null));
    }

    @DisplayName("Should throw exception when ExamType is NONE but Duration is provided")
    @Test
    void shouldThrowExceptionWhenExamTypeIsNoneAndDurationIsNotNull() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateThatDurationIsNullWhenExamTypeIsNone(ExamType.NONE,
                                                                                                     60));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_NOT_NULL), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "duration")), exception.getErrorAttributes());
    }
}
