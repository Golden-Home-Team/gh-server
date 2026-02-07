package kr.co.goldenhome;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.acm19.aws.interceptor.http.AwsRequestSigningApacheInterceptor;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.auth.aws.signer.AwsV4HttpSigner;
import software.amazon.awssdk.regions.Region;

@Configuration
public class ElasticConfig {

    @Value("${spring.elasticsearch.uris}")
    private String uris;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.iam.credentials.access-key}")
    private String accessKey;

    @Value("${aws.iam.credentials.secret-key}")
    private String secretKey;


    @Bean
    public ElasticsearchClient elasticsearchClient(ObjectMapper objectMapper) {

        AwsRequestSigningApacheInterceptor interceptor = new AwsRequestSigningApacheInterceptor(
                "es",
                AwsV4HttpSigner.create(),
                StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)),
                Region.of(region)
        );

        RestClient restClient = RestClient.builder(new HttpHost(uris, 443, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .addInterceptorLast((HttpRequestInterceptor) (request, context) -> {
                            request.setHeader("Content-Type", "application/json");
                            request.setHeader("Accept", "application/json");
                        })
                        .addInterceptorLast(interceptor)
                        .addInterceptorLast((HttpResponseInterceptor) (response, context) ->
                                response.addHeader("X-Elastic-Product", "Elasticsearch"))

                )
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(120000)
                )
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(objectMapper));

        return new ElasticsearchClient(transport);
    }


}
