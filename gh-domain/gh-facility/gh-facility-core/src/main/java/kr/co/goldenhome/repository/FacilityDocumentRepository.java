package kr.co.goldenhome.repository;

import kr.co.goldenhome.entity.FacilityDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FacilityDocumentRepository extends ElasticsearchRepository<FacilityDocument, String> {
}
