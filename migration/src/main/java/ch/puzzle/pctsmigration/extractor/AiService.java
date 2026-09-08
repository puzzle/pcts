package ch.puzzle.pctsmigration.extractor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient client;

    public AiService(ChatClient.Builder builder) {
        this.client = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    }

    public <R> R extract(String parsedMarkdownContent, String prompt, Class<R> typeRef) {
        return this.client.prompt().system(prompt).user(u -> u.text("""
                You must always return a valid JSON object at the root level, starting with a curly brace
                Never return a JSON array starting with a bracket at the root level.
                Ensure your response strictly matches the provided JSON schema

                Extract the records from the following parsed spreadsheet content:

                {content}
                """).param("content", parsedMarkdownContent)).call().entity(typeRef);
    }
}
