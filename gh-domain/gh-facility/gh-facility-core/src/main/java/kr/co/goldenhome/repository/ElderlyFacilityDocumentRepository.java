package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.ElderlyFacilityDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElderlyFacilityDocumentRepository extends ElasticsearchRepository<ElderlyFacilityDocument, String> {
}
