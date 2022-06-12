package reengineering.ddd.accounting.api.representation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import java.util.Optional;

public class SourceEvidenceJsonReader implements SourceEvidenceReader {
    private ObjectMapper mapper;

    @Inject
    public SourceEvidenceJsonReader(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<SourceEvidenceRequest<?>> read(String json) {
        try {
            return Optional.of(mapper.readValue(json, SourceEvidenceRequest.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
