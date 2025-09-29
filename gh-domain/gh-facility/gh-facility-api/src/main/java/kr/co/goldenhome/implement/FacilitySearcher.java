    package kr.co.goldenhome.implement;

    import co.elastic.clients.elasticsearch._types.DistanceUnit;
    import co.elastic.clients.elasticsearch._types.GeoDistanceType;
    import co.elastic.clients.elasticsearch._types.GeoLocation;
    import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
    import co.elastic.clients.elasticsearch._types.query_dsl.*;
    import kr.co.goldenhome.entity.FacilityDocument;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.elasticsearch.client.elc.NativeQuery;
    import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
    import org.springframework.data.elasticsearch.core.SearchHit;
    import org.springframework.data.elasticsearch.core.SearchHits;
    import org.springframework.data.elasticsearch.core.geo.GeoPoint;
    import org.springframework.stereotype.Component;
    import org.springframework.util.StringUtils;

    import java.time.Year;
    import java.util.ArrayList;
    import java.util.List;

    @Component
    @RequiredArgsConstructor
    public class FacilitySearcher {

        private final ElasticsearchOperations elasticsearchOperations;

        public List<FacilityDocument> search(String name, String address, String facilityType, String grade, String sort, int withinYears, int page, int size, Double latitude, Double longitude, Double radiusKm) {
            List<Query> queries = new ArrayList<>();
            List<Query> filters = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                Query searchQuery = MatchQuery.of(m -> m
                        .query(name)
                        .field("name")
                        .fuzziness("AUTO")
                )._toQuery();
                queries.add(searchQuery);
            }

            if (StringUtils.hasText(address)) {
                Query addressQuery = MatchQuery.of(m -> m
                        .query(address)
                        .field("address")
                )._toQuery();
                queries.add(addressQuery);
            }

            if (StringUtils.hasText(facilityType)) {
                if (facilityType.equals("실버타운") || facilityType.equals("양로원")) {
                    Query searchQuery = MatchQuery.of(m -> m
                            .query(facilityType)
                            .field("name")
                            .fuzziness("AUTO")
                    )._toQuery();
                    queries.add(searchQuery);
                } else if (facilityType.equals("요양원")) {
                    Query nursingHomeQuery1 = MatchQuery.of(m -> m
                            .query("노인요양시설")
                            .field("facilityType")
                            .fuzziness("AUTO")
                    )._toQuery();
                    queries.add(nursingHomeQuery1);
                    Query nursingHomeQuery2 = MatchQuery.of(m -> m
                            .query("노인요양공동생활가정")
                            .field("facilityType")
                            .fuzziness("AUTO")
                    )._toQuery();
                    queries.add(nursingHomeQuery2);
                } else {
                    Query facilityTypeQuery = MatchQuery.of(m -> m
                            .query(facilityType)
                            .field("facilityType")
                    )._toQuery();
                    queries.add(facilityTypeQuery);
                }

            }

            if (StringUtils.hasText(grade)) {
                Query gradeFilter = TermQuery.of(t -> t
                        .field("grade")
                        .value(grade)
                )._toQuery();
                filters.add(gradeFilter);
            }

            if (withinYears > 0) {
                double currentYear =  Year.now().getValue();
                Query yearRangeFilter = NumberRangeQuery.of(r -> r
                        .field("establishmentYear")
                        .gte(currentYear - withinYears)
                        .lte(currentYear)
                )._toRangeQuery()._toQuery();
                filters.add(yearRangeFilter);
            }

            Query boolQuery = BoolQuery.of(b -> b
                    .must(queries)
                    .filter(filters)
            )._toQuery();

            Sort sortOption = switch (sort) {
                case "view" ->
                        Sort.by(Sort.Direction.DESC, "viewCount");
                case "review" ->
                        Sort.by(Sort.Direction.DESC, "reviewCount");
                case "like" ->
                        Sort.by(Sort.Direction.DESC, "likeCount");
                case "consultation" ->
                        Sort.by(Sort.Direction.DESC, "consultationCount");
                default ->
                        Sort.unsorted();
            };

            if (latitude != null && longitude != null && radiusKm != null) {
                LatLonGeoLocation latLon = LatLonGeoLocation.of(l -> l.lat(latitude).lon(longitude));

                Query geoDistanceQuery = GeoDistanceQuery.of(q -> q
                        .field("location")
                        .location(GeoLocation.of(l -> l.latlon(latLon)))
                        .distance(radiusKm+"km")
                        .distanceType(GeoDistanceType.Arc)
                )._toQuery();

                filters.add(geoDistanceQuery);

            }

            NativeQuery nativeQuery = NativeQuery.builder()
                    .withQuery(boolQuery)
                    .withPageable(PageRequest.of(page-1, size))
                    .withSort(sortOption)
                    .build();

            SearchHits<FacilityDocument> searchHits = elasticsearchOperations.search(nativeQuery, FacilityDocument.class);
            return searchHits.stream()
                    .map(SearchHit::getContent).toList();

        }
    }
