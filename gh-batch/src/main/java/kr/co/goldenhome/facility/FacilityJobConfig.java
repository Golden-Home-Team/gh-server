package kr.co.goldenhome.facility;

import kr.co.goldenhome.JobCompletionNotificationListener;
import kr.co.goldenhome.dto.*;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.regex.Pattern;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class FacilityJobConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\s*\\d{2,4}-\\d{3,4}-\\d{4}.*");

    private boolean stopProcessingFlagForRetirementHome = false;

    private boolean startProcessingRangeForHomeCareFacility = false;
    private boolean stopProcessingRangeForHomeCareFacility = false;

    private boolean stopProcessingFlagForShortTermCare = false;

    private boolean startProcessingRangeForVisitingNursing = false;
    private boolean stopProcessingRangeForVisitingNursing = false;

    private boolean startProcessingRangeForVisitingBath = false;
    private boolean stopProcessingRangeForVisitingBath = false;

    private boolean startProcessingRangeForVisitingCare = false;
    private boolean stopProcessingRangeForVisitingCare = false;

    private boolean startProcessingRangeForDayNightCare = false;
    private boolean stopProcessingRangeForDayNightCare = false;


    /**
     * CSV 파일이 일정한 형식이 아니어서 각 타입에 따라 일정한 기준 잡아 테이블 분리하는 Job
     */
    @Bean
    public Job facilityJob(
            Step facilityStep,
            Step retirementHomeStep,
            Step homeCareFacilityStep,
            Step serviceStep,
            Step shortTermCareStep,
            Step visitingNursingStep,
            Step visitingBathStep,
            Step visitingCareStep,
            Step dayNightCareStep
    ) {
        return new JobBuilder("facilityJob", jobRepository)
                .start(facilityStep)
                .next(retirementHomeStep)
                .next(homeCareFacilityStep)
                .next(serviceStep)
                .next(shortTermCareStep)
                .next(visitingNursingStep)
                .next(visitingBathStep)
                .next(visitingCareStep)
                .next(dayNightCareStep)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .build();

    }

    @Bean
    public Step facilityStep(
            FlatFileItemReader<Facility> facilityItemReader,
            ItemWriter<Facility> facilityItemWriter
    ) {
        return new StepBuilder("facilityStep", jobRepository)
                .<Facility, Facility>chunk(500, transactionManager)
                .reader(facilityItemReader)
                .writer(facilityItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<Facility> facilityItemReader() {
        DefaultLineMapper<Facility> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setStrict(false);
        tokenizer.setNames(
                "type",
                "districtName",
                "ignore1",
                "ignore2",
                "ignore3",
                "name",
                "ignore4",
                "ignore5",
                "ignore6",
                "ignore7",
                "ignore8",
                "director",
                "ignore9",
                "ignore10",
                "ignore11",
                "capacity",
                "ignore12",
                "ignore13",
                "currentTotal",
                "ignore14",
                "currentMale",
                "ignore15",
                "currentFemale",
                "ignore16",
                "staffTotal",
                "ignore17",
                "staffMale",
                "ignore18",
                "ignore19",
                "ignore20",
                "staffFemale",
                "address",
                "ignore21",
                "ignore22",
                "ignore23",
                "ignore24",
                "ignore25",
                "ignore26",
                "ignore27",
                "phoneNumberAndFaxNumber1", // AN
                "ignore28",
                "phoneNumberAndFaxNumber2", //AP 노인복지주택(실버타운)에서 필드가 잠깐 바뀌고 다시 정상, 그러다 노인요양시설(요양원) 중 설악보건의료...에서 다시 바뀌고 단기보호시설도 이거 씀
                "ignore29",
                "establishmentDate1",
                "ignore30",
                "establishmentDate2",
                "ignore31",
                "operatingBody1",
                "ignore32",
                "operatingBody2",
                "ignore33",
                "ignore34",
                "ignore35",
                "ignore36",
                "ignore37"
        );
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new FacilityFieldSetMapper());
        return new FlatFileItemReaderBuilder<Facility>()
                .name("facilityItemReader")
                .resource(new ClassPathResource("facility.csv"))
                .lineMapper(lineMapper)
                .linesToSkip(12)
                .encoding("EUC-KR")
                .build();
    }


    @Bean
    public ItemWriter<Facility> facilityItemWriter() {
        return new JdbcBatchItemWriterBuilder<Facility>()
                .dataSource(dataSource)
                .sql("INSERT INTO facilities ( " +
                        "    type, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :type, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step retirementHomeStep(
            ItemReader<Facility> facilityDbReader,
            ItemProcessor<Facility, RetirementHome> facilityItemProcessorForRetirementHome,
            ItemWriter<RetirementHome> retirementHomeWriter
    ) {
        return new StepBuilder("retirementHomeStep", jobRepository)
                .<Facility, RetirementHome>chunk(500, transactionManager)
                .reader(facilityDbReader)
                .processor(facilityItemProcessorForRetirementHome)
                .writer(retirementHomeWriter)
                .build();
    }

    @Bean
    public ItemReader<Facility> facilityDbReader(PagingQueryProvider facilityQueryProvider) throws Exception {
        return new JdbcPagingItemReaderBuilder<Facility>()
                .name("facilityDbReader")
                .dataSource(dataSource)
                .queryProvider(facilityQueryProvider) // rowMapper 커스텀가능
                .rowMapper(new BeanPropertyRowMapper<>(Facility.class)) //BeanPropertyRowMapper Setter 메서드 존재: BeanPropertyRowMapper는 ResultSet에서 읽은 값을 해당 프로퍼티의 Setter 메서드를 통해 객체에 주입합니다.
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider facilityQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, type, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM facilities");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<Facility, RetirementHome> retirementHomeItemProcessor() {

         return item -> {
             if (stopProcessingFlagForRetirementHome) return null;
             if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                 return RetirementHome.from(item);
             }
             if (StringUtils.hasText(item.getType()) && item.getType().contains("노인공동생활가정")) stopProcessingFlagForRetirementHome = true;
             return null;
         };
    }

    @Bean
    public ItemWriter<RetirementHome> retirementHomeWriter() {
        return new JdbcBatchItemWriterBuilder<RetirementHome>()
                .dataSource(dataSource)
                .sql("INSERT INTO retirement_homes ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step homeCareFacilityStep(
            ItemReader<Facility> facilityDbReader,
            ItemProcessor<Facility, HomeCareFacility> homeCareFacilityItemProcessor,
            ItemWriter<HomeCareFacility> homeCareFacilityWriter
    ) {
        return new StepBuilder("homeCareFacilityStep", jobRepository)
                .<Facility, HomeCareFacility>chunk(500, transactionManager)
                .reader(facilityDbReader)
                .processor(homeCareFacilityItemProcessor)
                .writer(homeCareFacilityWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Facility, HomeCareFacility> homeCareFacilityItemProcessor() {

        return item -> {

            if (stopProcessingRangeForHomeCareFacility) return null;

            if (!startProcessingRangeForHomeCareFacility) {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("노인요양시설")) {
                    startProcessingRangeForHomeCareFacility = true;
                }
                return null;
            }
            else {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("노인복지관")) {
                    stopProcessingRangeForHomeCareFacility = true;
                }
                if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                    return HomeCareFacility.from(item);
                }
                return null;
            }

        };
    }

    @Bean
    public ItemWriter<HomeCareFacility> homeCareFacilityWriter() {
        return new JdbcBatchItemWriterBuilder<HomeCareFacility>()
                .dataSource(dataSource)
                .sql("INSERT INTO home_care_facilities ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step serviceStep(
            FlatFileItemReader<Service> serviceItemReader,
            ItemWriter<Service> serviceItemWriter
    ) {
        return new StepBuilder("serviceStep", jobRepository)
                .<Service, Service>chunk(500, transactionManager)
                .reader(serviceItemReader)
                .writer(serviceItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<Service> serviceItemReader() {
        DefaultLineMapper<Service> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setStrict(false);
        tokenizer.setNames(
                "type",
                "districtName",
                "ignore1",
                "ignore2",
                "name",
                "ignore3",
                "ignore4",
                "ignore5",
                "ignore6",
                "ignore7",
                "director",
                "ignore9",
                "ignore10",
                "capacity",
                "ignore13",
                "currentTotal",
                "ignore14",
                "ignore15",
                "currentMale",
                "ignore16",
                "currentFemale",
                "ignore21",
                "ignore22",
                "ignore23",
                "ignore24",
                "ignore25",
                "staffTotal",
                "ignore26",
                "ignore27",
                "ignore28",
                "staffMale",
                "staffFemale",
                "ignore28",
                "ignore29",
                "address",
                "ignore30",
                "ignore31",
                "ignore32",
                "ignore33",
                "ignore34",
                "ignore35",
                "phoneNumber"
        );
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new ServiceFieldSetMapper());
        return new FlatFileItemReaderBuilder<Service>()
                .name("serviceItemReader")
                .resource(new ClassPathResource("facility.csv"))
                .lineMapper(lineMapper)
                .linesToSkip(16212)
                .encoding("EUC-KR")
                .build();
    }

    @Bean
    public ItemWriter<Service> serviceItemWriter() {
        return new JdbcBatchItemWriterBuilder<Service>()
                .dataSource(dataSource)
                .sql("INSERT INTO services ( " +
                        "    type, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :type, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step shortTermCareStep(
            ItemReader<Service> serviceDbReader,
            ItemProcessor<Service, ShortTermCare> shortTermCareItemProcessor,
            ItemWriter<ShortTermCare> shortTermCareWriter
    ) {
        return new StepBuilder("shortTermCareStep", jobRepository)
                .<Service, ShortTermCare>chunk(500, transactionManager)
                .reader(serviceDbReader)
                .processor(shortTermCareItemProcessor)
                .writer(shortTermCareWriter)
                .build();
    }

    @Bean
    public ItemReader<Service> serviceDbReader(PagingQueryProvider serviceQueryProvider) throws Exception {
        return new JdbcPagingItemReaderBuilder<Service>()
                .name("serviceDbReader")
                .dataSource(dataSource)
                .queryProvider(serviceQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(Service.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public PagingQueryProvider serviceQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("SELECT id, type, name, district_name, director, capacity, current_total, " +
                "current_male, current_female, staff_total, staff_male, staff_female, " +
                "address, phone_number, establishment_date, operating_body");
        queryProvider.setFromClause("FROM services");
        queryProvider.setSortKey("id");
        return queryProvider.getObject();
    }

    @Bean
    public ItemProcessor<Service, ShortTermCare> shortTermCareItemProcessor() {
        return item -> {

            if (stopProcessingFlagForShortTermCare) return null;
            if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                return ShortTermCare.from(item);
            }
            if (StringUtils.hasText(item.getType()) && item.getType().contains("방문간호서비스")) stopProcessingFlagForShortTermCare = true;
            return null;
        };
    }

    @Bean
    public ItemWriter<ShortTermCare> shortTermCareWriter() {
        return new JdbcBatchItemWriterBuilder<ShortTermCare>()
                .dataSource(dataSource)
                .sql("INSERT INTO shor_term_cares ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step visitingNursingStep(
            ItemReader<Service> serviceDbReader,
            ItemProcessor<Service, VisitingNursing> visitingNursingItemProcessor,
            ItemWriter<VisitingNursing> visitingNursingWriter
    ) {
        return new StepBuilder("visitingNursingStep", jobRepository)
                .<Service, VisitingNursing>chunk(500, transactionManager)
                .reader(serviceDbReader)
                .processor(visitingNursingItemProcessor)
                .writer(visitingNursingWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Service, VisitingNursing> visitingNursingItemProcessor() {

        return item -> {

            if (stopProcessingRangeForVisitingNursing) return null;

            if (!startProcessingRangeForVisitingNursing) {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("방문간호서비스")) {
                    startProcessingRangeForVisitingNursing = true;
                }
                return null;
            }
            else {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("방문목욕서비스")) {
                    stopProcessingRangeForVisitingNursing = true;
                }
                if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                    return VisitingNursing.from(item);
                }
                return null;
            }

        };
    }

    @Bean
    public ItemWriter<VisitingNursing> visitingNursingWriter() {
        return new JdbcBatchItemWriterBuilder<VisitingNursing>()
                .dataSource(dataSource)
                .sql("INSERT INTO visiting_nursings ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step visitingBathStep(
            ItemReader<Service> serviceDbReader,
            ItemProcessor<Service, VisitingBath> visitingBathItemProcessor,
            ItemWriter<VisitingBath> visitingBathWriter
    ) {
        return new StepBuilder("visitingBathStep", jobRepository)
                .<Service, VisitingBath>chunk(500, transactionManager)
                .reader(serviceDbReader)
                .processor(visitingBathItemProcessor)
                .writer(visitingBathWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Service, VisitingBath> visitingBathItemProcessor() {

        return item -> {

            if (stopProcessingRangeForVisitingBath) return null;

            if (!startProcessingRangeForVisitingBath) {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("방문목욕서비스")) {
                    startProcessingRangeForVisitingBath = true;
                }
                return null;
            }
            else {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("방문요양서비스")) {
                    stopProcessingRangeForVisitingBath = true;
                }
                if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                    return VisitingBath.from(item);
                }
                return null;
            }

        };
    }

    @Bean
    public ItemWriter<VisitingBath> visitingBathWriter() {
        return new JdbcBatchItemWriterBuilder<VisitingBath>()
                .dataSource(dataSource)
                .sql("INSERT INTO visiting_baths ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step visitingCareStep(
            ItemReader<Service> serviceDbReader,
            ItemProcessor<Service, VisitingCare> visitingCareItemProcessor,
            ItemWriter<VisitingCare> visitingCareItemWriter
    ) {
        return new StepBuilder("visitingCareStep", jobRepository)
                .<Service, VisitingCare>chunk(500, transactionManager)
                .reader(serviceDbReader)
                .processor(visitingCareItemProcessor)
                .writer(visitingCareItemWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Service, VisitingCare> visitingCareItemProcessor() {

        return item -> {

            if (stopProcessingRangeForVisitingCare) return null;

            if (!startProcessingRangeForVisitingCare) {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("방문요양서비스")) {
                    startProcessingRangeForVisitingCare = true;
                }
                return null;
            }
            else {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("복지용구지원서비스")) {
                    stopProcessingRangeForVisitingCare = true;
                }
                if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                    return VisitingCare.from(item);
                }
                return null;
            }

        };
    }

    @Bean
    public ItemWriter<VisitingCare> visitingCareItemWriter() {
        return new JdbcBatchItemWriterBuilder<VisitingCare>()
                .dataSource(dataSource)
                .sql("INSERT INTO visiting_cares ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step dayNightCareStep(
            ItemReader<Service> serviceDbReader,
            ItemProcessor<Service, DayNightCare> dayNightCareItemProcessor,
            ItemWriter<DayNightCare> dayNightCareItemWriter
    ) {
        return new StepBuilder("dayNightCareStep", jobRepository)
                .<Service, DayNightCare>chunk(500, transactionManager)
                .reader(serviceDbReader)
                .processor(dayNightCareItemProcessor)
                .writer(dayNightCareItemWriter)
                .build();
    }

    @Bean
    public ItemProcessor<Service, DayNightCare> dayNightCareItemProcessor() {

        return item -> {

            if (stopProcessingRangeForDayNightCare) return null;

            if (!startProcessingRangeForDayNightCare) {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("주?야간보호서비스")) {
                    startProcessingRangeForDayNightCare = true;
                }
                return null;
            }
            else {
                if (StringUtils.hasText(item.getType()) && item.getType().contains("노인보호전문기관")) {
                    stopProcessingRangeForDayNightCare = true;
                }
                if (StringUtils.hasText(item.getPhoneNumber()) && PHONE_NUMBER_PATTERN.matcher(item.getPhoneNumber()).matches()) {
                    return DayNightCare.from(item);
                }
                return null;
            }

        };
    }

    @Bean
    public ItemWriter<DayNightCare> dayNightCareItemWriter() {
        return new JdbcBatchItemWriterBuilder<DayNightCare>()
                .dataSource(dataSource)
                .sql("INSERT INTO day_night_cares ( " +
                        "    id, name, district_name, director, capacity, " +
                        "    current_total, current_male, current_female, staff_total, staff_male, staff_female, address, phone_number, establishment_date, operating_body" +
                        ") VALUES (" +
                        "    :id, :name, :districtName, :director, :capacity, " +
                        "    :currentTotal, :currentMale, :currentFemale, :staffTotal, :staffMale, :staffFemale, :address, :phoneNumber, :establishmentDate, :operatingBody" +
                        ")")
                .beanMapped()
                .build();
    }

}
