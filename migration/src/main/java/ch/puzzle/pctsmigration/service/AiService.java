package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.certificate.CertificateAiResultDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {
    private final ChatClient client;

    public AiService(ChatClient.Builder builder) {
        this.client = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    public <R> R extractCertificateData(String parsedMarkdownContent, String prompt) {
        return this.client.prompt()
                .system(prompt)
                .user(u -> u.text("""
                    Extract the records from the following parsed spreadsheet content:
                    
                    {content}
                    """)
                        .param("content", parsedMarkdownContent))
                .call()
                .entity(new ParameterizedTypeReference<R>() {
                });
    }
}
