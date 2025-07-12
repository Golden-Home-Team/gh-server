package kr.co.goldenhome.elderty_facility;

import kr.co.goldenhome.JobCompletionNotificationListener;
import kr.co.goldenhome.entity.ElderlyFacility;
import kr.co.goldenhome.entity.ElderlyFacilityDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class ElderlyFacilityDocumentJobConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    /**
     * RDB 에 저장한 데이터를 엘라스틱 서치에 동기화
     */
    @Bean
    public Job elderlyFacilityDocumentJob(Step elderlyFacilityDocumentStep) {
        return new JobBuilder("elderlyFacilityDocumentJob", jobRepository)
                .start(elderlyFacilityDocumentStep)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .build();
    }

    @Bean
    public Step elderlyFacilityDocumentStep(
            ItemReader<ElderlyFacility> elderlyFacilityItemReader,
            ItemProcessor<ElderlyFacility, ElderlyFacilityDocument> elderlyFacilityDocumentItemProcessor,
            ItemWriter<ElderlyFacilityDocument> elderlyFacilityDocumentItemWriter
    ) {
        return new StepBuilder("elderlyFacilityDocumentStep", jobRepository)
                .<ElderlyFacility, ElderlyFacilityDocument>chunk(500, transactionManager)
                .reader(elderlyFacilityItemReader)
                .processor(elderlyFacilityDocumentItemProcessor)
                .writer(elderlyFacilityDocumentItemWriter)
                .build();
    }

    @Bean
    public ItemReader<ElderlyFacility> elderlyFacilityItemReader(PagingQueryProvider elderlyFacilityPagingQueryProvider) {
        return new JdbcPagingItemReaderBuilder<ElderlyFacility>()
                .name("elderlyFacilityItemReader")
                .dataSource(dataSource)
                .queryProvider(elderlyFacilityPagingQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(ElderlyFacility.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider elderlyFacilityPagingQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body, facility_type");
        queryProvider.setFromClause("FROM elderly_facilities");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    ItemProcessor<ElderlyFacility, ElderlyFacilityDocument> elderlyFacilityDocumentItemProcessor() {
        return ElderlyFacilityDocument::from;
    }

//    @Bean
//    public RepositoryItemWriter<ElderlyFacilityDocument> elderlyFacilityDocumentItemWriter() {
//        RepositoryItemWriter<ElderlyFacilityDocument> writer = new RepositoryItemWriter<>();
//        writer.setRepository(documentRepository);
//        writer.setMethodName("save");
//        return writer;
//    }
}
