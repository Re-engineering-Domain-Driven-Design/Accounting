package reengineering.ddd.accounting.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import reengineering.ddd.accounting.model.SourceEvidence;

import java.util.List;

@Mapper
public interface SourceEvidenceMapper {
    List<SourceEvidence> findByCustomerId(String customerId);
}
