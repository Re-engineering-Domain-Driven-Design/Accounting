package reengineering.ddd.accounting.api.representation;

import java.util.Optional;

public interface SourceEvidenceReader {
    Optional<SourceEvidenceRequest<?>> read(String json);
}
