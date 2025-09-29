package kr.co.goldenhome;

import kr.co.goldenhome.entity.Facility;
import kr.co.goldenhome.entity.FacilityDocument;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class ElasticSearchJobConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Bean
    public Job elasticSearchJob(
        Step facilityDocumentStep
    ) {
        return new JobBuilder("elasticSearchJob", jobRepository)
                .start(facilityDocumentStep)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .build();
    }

    @Bean
    public Step facilityDocumentStep(
            ItemReader<Facility> facilityItemReader,
            ItemProcessor<Facility, FacilityDocument> facilityDocumentItemProcessor,
            ItemWriter<FacilityDocument> facilityDocumentItemWriter
    ) {
        return new StepBuilder("facilityDocumentStep", jobRepository)
                .<Facility, FacilityDocument>chunk(500, transactionManager)
                .reader(facilityItemReader)
                .processor(facilityDocumentItemProcessor)
                .writer(facilityDocumentItemWriter)
                .build();
    }

    @Bean
    public ItemReader<Facility> facilityItemReader(PagingQueryProvider facilityPagingQueryProvider) {
        return new JdbcPagingItemReaderBuilder<Facility>()
                .name("facilityItemReader")
                .dataSource(dataSource)
                .queryProvider(facilityPagingQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(Facility.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider facilityPagingQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, institution_symbol, facility_type, name, address, phone_number, email, " +
                "homepage, establishment_date, district_name, capacity, " +
                "current_male, current_female, current_total, staff_total, " +
                "latitude, longitude");
        queryProvider.setFromClause("FROM facilities");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }


}
