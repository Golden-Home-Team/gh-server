package kr.co.goldenhome.elderty_facility;

import kr.co.goldenhome.JobCompletionNotificationListener;
import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.entity.ElderlyFacility;
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
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ElderlyFacilityJobConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Bean
    public Job elderlyFacilityJob(
            Step retirementHome,
            Step homeCareFacility,
            Step shortTermCare,
            Step visitingNursing,
            Step visitingCare,
            Step visitingBath,
            Step dayNightCare
    ) {
        return new JobBuilder("elderlyFacilityJob", jobRepository)
                .start(retirementHome)
                .next(homeCareFacility)
                .next(shortTermCare)
                .next(visitingNursing)
                .next(visitingCare)
                .next(visitingBath)
                .next(dayNightCare)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .build();
    }

    @Bean
    public Step retirementHome(
            ItemReader<RetirementHome> retirementHomeDBReader,
            ItemProcessor<RetirementHome, ElderlyFacility> retirementHomeToElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("retirementHome", jobRepository)
                .<RetirementHome, ElderlyFacility>chunk(500, transactionManager)
                .reader(retirementHomeDBReader)
                .processor(retirementHomeToElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<RetirementHome> retirementHomeDBReader(PagingQueryProvider retirementHomeQueryProvider) {
        return new JdbcPagingItemReaderBuilder<RetirementHome>()
                .name("retirementHomeDBReader")
                .dataSource(dataSource)
                .queryProvider(retirementHomeQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(RetirementHome.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider retirementHomeQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM retirement_homes");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<RetirementHome, ElderlyFacility> retirementHomeToElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }

    @Bean
    public ItemWriter<ElderlyFacility> elderlyFacilityItemWriter() {
        return new JdbcBatchItemWriterBuilder<ElderlyFacility>()
                .dataSource(dataSource)
                .sql("INSERT INTO elderly_facilities ( " +
                        "    name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body, facility_type" +
                        ") VALUES (" +
                        "    :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody, :facilityType" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step homeCareFacility(
            ItemReader<HomeCareFacility> homeCareFacilityDBReader,
            ItemProcessor<HomeCareFacility, ElderlyFacility> homeCareFacilityElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("homeCareFacility", jobRepository)
                .<HomeCareFacility, ElderlyFacility>chunk(500, transactionManager)
                .reader(homeCareFacilityDBReader)
                .processor(homeCareFacilityElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<HomeCareFacility> homeCareFacilityDBReader(PagingQueryProvider homeCareFacilityQueryProvider) {
        return new JdbcPagingItemReaderBuilder<HomeCareFacility>()
                .name("homeCareFacilityDBReader")
                .dataSource(dataSource)
                .queryProvider(homeCareFacilityQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(HomeCareFacility.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider homeCareFacilityQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM home_care_facilities");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<HomeCareFacility, ElderlyFacility> homeCareFacilityElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }

    @Bean
    public Step shortTermCare(
            ItemReader<ShortTermCare> shortTermCareDBReader,
            ItemProcessor<ShortTermCare, ElderlyFacility> shortTermCareToElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("shortTermCare", jobRepository)
                .<ShortTermCare, ElderlyFacility>chunk(500, transactionManager)
                .reader(shortTermCareDBReader)
                .processor(shortTermCareToElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<ShortTermCare> shortTermCareDBReader(PagingQueryProvider shortTermCareQueryProvider) {
        return new JdbcPagingItemReaderBuilder<ShortTermCare>()
                .name("shortTermCareDBReader")
                .dataSource(dataSource)
                .queryProvider(shortTermCareQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(ShortTermCare.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider shortTermCareQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM shor_term_cares");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<ShortTermCare, ElderlyFacility> shortTermCareToElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }

    @Bean
    public Step visitingNursing(
            ItemReader<VisitingNursing> visitingNursingDBReader,
            ItemProcessor<VisitingNursing, ElderlyFacility> visitingNursingToElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("visitingNursing", jobRepository)
                .<VisitingNursing, ElderlyFacility>chunk(500, transactionManager)
                .reader(visitingNursingDBReader)
                .processor(visitingNursingToElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<VisitingNursing> visitingNursingDBReader(PagingQueryProvider visitingNursingQueryProvider) {
        return new JdbcPagingItemReaderBuilder<VisitingNursing>()
                .name("visitingNursingDBReader")
                .dataSource(dataSource)
                .queryProvider(visitingNursingQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(VisitingNursing.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider visitingNursingQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM visiting_nursings");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<VisitingNursing, ElderlyFacility> visitingNursingToElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }

    @Bean
    public Step visitingCare(
            ItemReader<VisitingCare> visitingCareDBReader,
            ItemProcessor<VisitingCare, ElderlyFacility> visitingCareToElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("visitingCare", jobRepository)
                .<VisitingCare, ElderlyFacility>chunk(500, transactionManager)
                .reader(visitingCareDBReader)
                .processor(visitingCareToElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<VisitingCare> visitingCareDBReader(PagingQueryProvider visitingCareQueryProvider) {
        return new JdbcPagingItemReaderBuilder<VisitingCare>()
                .name("visitingCareDBReader")
                .dataSource(dataSource)
                .queryProvider(visitingCareQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(VisitingCare.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider visitingCareQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM visiting_cares");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<VisitingCare, ElderlyFacility> visitingCareToElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }

    @Bean
    public Step visitingBath(
            ItemReader<VisitingBath> visitingBathDBReader,
            ItemProcessor<VisitingBath, ElderlyFacility> visitingBathToElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("visitingBath", jobRepository)
                .<VisitingBath, ElderlyFacility>chunk(500, transactionManager)
                .reader(visitingBathDBReader)
                .processor(visitingBathToElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<VisitingBath> visitingBathDBReader(PagingQueryProvider visitingBathQueryProvider) {
        return new JdbcPagingItemReaderBuilder<VisitingBath>()
                .name("visitingBathDBReader")
                .dataSource(dataSource)
                .queryProvider(visitingBathQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(VisitingBath.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider visitingBathQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM visiting_baths");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<VisitingBath, ElderlyFacility> visitingBathToElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }

    @Bean
    public Step dayNightCare(
            ItemReader<DayNightCare> dayNightCareDBReader,
            ItemProcessor<DayNightCare, ElderlyFacility> dayNightCareToElderlyFacilityProcessor,
            ItemWriter<ElderlyFacility> elderlyFacilityItemWriter
    ) {
        return new StepBuilder("dayNightCare", jobRepository)
                .<DayNightCare, ElderlyFacility>chunk(500, transactionManager)
                .reader(dayNightCareDBReader)
                .processor(dayNightCareToElderlyFacilityProcessor)
                .writer(elderlyFacilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<DayNightCare> dayNightCareDBReader(PagingQueryProvider dayNightCareQueryProvider) {
        return new JdbcPagingItemReaderBuilder<DayNightCare>()
                .name("dayNightCareDBReader")
                .dataSource(dataSource)
                .queryProvider(dayNightCareQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(DayNightCare.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider dayNightCareQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM day_night_cares");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<DayNightCare, ElderlyFacility> dayNightCareToElderlyFacilityProcessor() {
        return ElderlyFacility::from;
    }


}
