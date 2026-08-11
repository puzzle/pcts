package ch.puzzle.pctsmigration.extractor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

@Service
public class ExtractorAiService {
    private final ChatClient client;

    public ExtractorAiService(ChatClient.Builder builder) {
        this.client = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    public <R> R extractCertificateData(String parsedMarkdownContent, String prompt, Class<R> typeRef) {
        return this.client.prompt().system(prompt).user(u -> u.text("""
                Extract the records from the following parsed spreadsheet content:

                {content}
                """).param("content", parsedMarkdownContent)).call().entity(typeRef);
    }
}
