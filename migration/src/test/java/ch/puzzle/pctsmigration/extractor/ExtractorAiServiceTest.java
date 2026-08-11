package ch.puzzle.pctsmigration.extractor;

import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;
import org.springframework.ai.chat.client.advisor.api.Advisor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExtractorAiServiceTest {

    @Mock
    private ChatClient.Builder builder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClientRequestSpec requestSpec;

    @Mock
    private CallResponseSpec callResponseSpec;

    private ExtractorAiService aiService;

    @BeforeEach
    void setUp() {
        when(builder.defaultAdvisors(any(Advisor[].class))).thenReturn(builder);
        when(builder.build()).thenReturn(chatClient);

        aiService = new ExtractorAiService(builder);
    }

    @Test
    @DisplayName("extractCertificateData sollte System-Prompt, Markdown im User-Prompt und Ziel-Klasse an Spring AI übergeben")
    void extractCertificateData_callsChatClientWithCorrectPromptsAndReturnsEntity() {
        // Given
        String markdown = "| A | B |";
        String systemPrompt = "Prompt";
        DummyResult expectedResult = new DummyResult("A, B");

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(systemPrompt)).thenReturn(requestSpec);
        when(requestSpec.user(any(Consumer.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.entity(eq(DummyResult.class))).thenReturn(expectedResult);

        DummyResult actualResult = aiService.aiExtractFrom(
                markdown,
                systemPrompt,
                DummyResult.class
        );

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(chatClient, times(1)).prompt();
        verify(requestSpec).system(systemPrompt);
        verify(callResponseSpec).entity(DummyResult.class);
    }

    record DummyResult(String value) {}
}