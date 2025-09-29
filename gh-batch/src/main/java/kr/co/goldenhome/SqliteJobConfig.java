package kr.co.goldenhome;

import kr.co.goldenhome.dto.*;
import kr.co.goldenhome.entity.*;
import kr.co.goldenhome.entity.Facility;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlitePagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;

import java.util.stream.Collectors;

//@Configuration
public class SqliteJobConfig {

    private final JobRepository jobRepository;
    private final DataSource sqliteDataSource;
    private final PlatformTransactionManager sqliteTransactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    public SqliteJobConfig(JobRepository jobRepository,
                           @Qualifier("sqliteDataSource") DataSource sqliteDataSource,
                           @Qualifier("sqliteTransactionManager") PlatformTransactionManager sqliteTransactionManager,
                           JobCompletionNotificationListener jobCompletionNotificationListener) {
        this.jobRepository = jobRepository;
        this.sqliteDataSource = sqliteDataSource;
        this.sqliteTransactionManager = sqliteTransactionManager;
        this.jobCompletionNotificationListener = jobCompletionNotificationListener;
    }

    @Bean
    public Job sqliteToCsvJob(
                               Step sqliteToCsvStep,
                               Step detailFacilityStep,
                               Step photoStep,
                               Step programStep,
                               Step staffInfoStep,
                               Step gradeStep

    ) {
        return new JobBuilder("sqliteToCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .start(sqliteToCsvStep)
                .next(detailFacilityStep)
                .next(photoStep)
                .next(programStep)
                .next(staffInfoStep)
                .next(gradeStep)
                .build();
    }

    @Bean
    public Step sqliteToCsvStep(
            ItemReader<SqliteFacility> sqliteFacilityReader,
            ItemProcessor<SqliteFacility, Facility> sqliteFacilityToFacilityItemProcessor,
            ItemWriter<Facility> csvFacilityWriter
    ) {
        return new StepBuilder("sqliteToCsvStep", jobRepository)
                .<SqliteFacility, Facility>chunk(500, sqliteTransactionManager)
                .reader(sqliteFacilityReader)
                .processor(sqliteFacilityToFacilityItemProcessor)
                .writer(csvFacilityWriter)
                .build();
    }


    @Bean
    public ItemReader<SqliteFacility> sqliteFacilityReader(SqlitePagingQueryProvider sqliteFacilityQueryProvider) {
        return new JdbcPagingItemReaderBuilder<SqliteFacility>()
                .name("sqliteFacilityReader")
                .dataSource(sqliteDataSource)
                .queryProvider(sqliteFacilityQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(SqliteFacility.class))
                .pageSize(1000)
                .build();
    }


    @Bean
    public SqlitePagingQueryProvider sqliteFacilityQueryProvider() throws Exception {
        SqlitePagingQueryProvider queryProvider = new SqlitePagingQueryProvider();
        queryProvider.setSelectClause("MIN(`index`) as `index`, id, facility_type, name, address, phone_number, email, homepage, establishment_date, district_name, capacity, current_male, current_female, current_total, staff_total, latitude, longitude");
        queryProvider.setFromClause("from faclilty"); // 오타 아니고 맞음: faclilty
        queryProvider.setGroupClause("GROUP BY id");
        queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));
        return queryProvider;
    }


    @Bean
    public ItemProcessor<SqliteFacility, Facility> sqliteFacilityToFacilityItemProcessor() {
        return Facility::from;

    }

    @Bean
    public ItemWriter<Facility> csvFacilityWriter() {
        FlatFileItemWriter<Facility> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/facilities.csv")); // 출력 CSV 파일 경로 설정
        writer.setAppendAllowed(false); // 파일이 이미 존재하면 이어쓰기 허용 (false로 하면 덮어씀)

        BeanWrapperFieldExtractor<Facility> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "facilityType", "name", "address", "phoneNumber", "email",
                "homepage", "establishmentDate", "districtName", "capacity",
                "currentMale", "currentFemale", "currentTotal", "staffTotal", "latitude", "longitude"
        };
        fieldExtractor.setNames(fieldNames);

        // 커스텀 라인 집계기 설정: 필드 내 쉼표를 포함하는 경우 따옴표로 묶습니다.
        QuotingDelimitedLineAggregator<Facility> lineAggregator = new QuotingDelimitedLineAggregator<>();
        lineAggregator.setDelimiter(","); // 쉼표로 구분
        lineAggregator.setFieldExtractor(fieldExtractor); // 필드 추출기 설정

        writer.setLineAggregator(lineAggregator);

        // 헤더 쓰기 설정: 첫 줄에 헤더를 추가합니다.
        writer.setHeaderCallback(writer1 -> {
            // 헤더 필드명도 따옴표로 묶어야 할 수 있으므로, QuotingDelimitedLineAggregator를 사용하여 처리합니다.
            // 여기서는 단순 문자열로 작성하지만, 필요에 따라 더 복잡한 로직을 추가할 수 있습니다.
            String headerLine = Arrays.stream(fieldNames)
                    .map(name -> {
                        // 헤더 이름 자체에 쉼표 등이 있을 경우를 대비해 따옴표 처리
                        if (name.contains(",") || name.contains("\"") || name.contains("\n") || name.contains("\r")) {
                            String escapedName = name.replace("\"", "\"\"");
                            return "\"" + escapedName + "\"";
                        }
                        return name;
                    })
                    .collect(Collectors.joining(","));
            writer1.write(headerLine);
        });

        return writer;
    }


    public static class QuotingDelimitedLineAggregator<T> extends DelimitedLineAggregator<T> {

        private char quoteCharacter = '"'; // 기본 따옴표 문자
        private FieldExtractor<T> fieldExtractor; // FieldExtractor를 멤버 변수로 저장
        private String internalDelimiter;

        // FieldExtractor를 설정하는 메서드를 오버라이드하여 내부 멤버 변수에 저장
        @Override
        public void setFieldExtractor(FieldExtractor<T> fieldExtractor) {
            super.setFieldExtractor(fieldExtractor); // 부모 클래스의 설정도 호출
            this.fieldExtractor = fieldExtractor; // 내부 멤버 변수에 저장
        }

        @Override
        public String aggregate(T item) {
            if (this.fieldExtractor == null) {
                throw new IllegalStateException("FieldExtractor must be set");
            }
            Object[] fields = this.fieldExtractor.extract(item);
            String delimiterToUse = (this.internalDelimiter != null) ? this.internalDelimiter : ",";

            return Arrays.stream(fields)
                    .map(field -> {
                        String fieldValue = (field != null) ? field.toString() : "";
                        // 필드 값에 구분자, 따옴표 문자, 또는 개행 문자가 포함되어 있으면 따옴표로 묶습니다.
                        // 기존 따옴표는 두 번 반복하여 이스케이프 처리합니다.
                        if (fieldValue.contains(delimiterToUse) ||
                                fieldValue.contains(String.valueOf(quoteCharacter)) ||
                                fieldValue.contains("\n") || fieldValue.contains("\r")) {

                            fieldValue = fieldValue.replace(String.valueOf(quoteCharacter), String.valueOf(quoteCharacter) + String.valueOf(quoteCharacter));
                            return quoteCharacter + fieldValue + quoteCharacter;
                        }
                        return fieldValue;
                    })
                    .collect(Collectors.joining(delimiterToUse));
        }
    }

    @Bean
    public Step detailFacilityStep(
            ItemReader<SqliteDetailFacility> detailFacilityReader,
            ItemProcessor<SqliteDetailFacility, FacilityDetail> sqliteDetailFacilityToFacilityDetailItemProcessor,
            ItemWriter<FacilityDetail> facilityDetailItemWriter
    ) {
        return new StepBuilder("detailFacilityStep", jobRepository)
                .<SqliteDetailFacility, FacilityDetail>chunk(500, sqliteTransactionManager)
                .reader(detailFacilityReader)
                .processor(sqliteDetailFacilityToFacilityDetailItemProcessor)
                .writer(facilityDetailItemWriter)
                .build();
    }

    @Bean
    public ItemReader<SqliteDetailFacility> detailFacilityReader(SqlitePagingQueryProvider detailFacilityQueryProvider) {
        return new JdbcPagingItemReaderBuilder<SqliteDetailFacility>()
                .name("detailFacilityReader")
                .dataSource(sqliteDataSource)
                .queryProvider(detailFacilityQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(SqliteDetailFacility.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public SqlitePagingQueryProvider detailFacilityQueryProvider() throws Exception {
        SqlitePagingQueryProvider queryProvider = new SqlitePagingQueryProvider();
        queryProvider.setSelectClause("id, facility_id, singleRoomCount, doubleRoomCount, tripleRoomCount, quadRoomCount, specialBedroomCount, officeCount, medicalNurseRoomCount, dailyLivingTrainingRoomCount, programRoomCount, kitchenDiningRoomCount, bathroomCount, washBathRoomCount, laundryRoomCount");
        queryProvider.setFromClause("from detail_facility");
        queryProvider.setGroupClause("group by facility_id");
        queryProvider.setSortKeys(Collections.singletonMap("facility_id", Order.ASCENDING));
        return queryProvider;
    }

    @Bean
    public ItemProcessor<SqliteDetailFacility, FacilityDetail> sqliteDetailFacilityToFacilityDetailItemProcessor() {
        return FacilityDetail::from;
    }

    @Bean
    public ItemWriter<FacilityDetail> facilityDetailItemWriter() {
        FlatFileItemWriter<FacilityDetail> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/facility_details.csv")); // 출력 CSV 파일 경로 설정
        writer.setAppendAllowed(false);
        BeanWrapperFieldExtractor<FacilityDetail> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "singleRoomCount", "doubleRoomCount", "tripleRoomCount", "quadRoomCount", "specialBedroomCount", "officeCount",
                "medicalNurseRoomCount", "dailyLivingTrainingRoomCount", "programRoomCount", "kitchenDiningRoomCount", "bathroomCount",
                "washBathRoomCount", "laundryRoomCount"
        };

        fieldExtractor.setNames(fieldNames);

        QuotingDelimitedLineAggregator<FacilityDetail> lineAggregator = new QuotingDelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);

        writer.setHeaderCallback(writer1 -> {
            String headerLine = Arrays.stream(fieldNames)
                    .map(name -> {
                        if (name.contains(",") || name.contains("\"") || name.contains("\n") || name.contains("\r")) {
                            String escapedName = name.replace("\"", "\"\"");
                            return "\"" + escapedName + "\"";
                        }
                        return name;
                    })
                    .collect(Collectors.joining(","));
            writer1.write(headerLine);
        });

        return writer;
    }


    @Bean
    public Step photoStep(
            ItemReader<SqlitePhoto> sqlitePhotoItemReader,
            ItemProcessor<SqlitePhoto, FacilityPhoto> sqlitePhotoFacilityPhotoItemProcessor,
            ItemWriter<FacilityPhoto> facilityPhotoItemWriter
    ) {
        return new StepBuilder("photoStep", jobRepository)
                .<SqlitePhoto, FacilityPhoto>chunk(500, sqliteTransactionManager) // SQLite 트랜잭션 매니저 사용
                .reader(sqlitePhotoItemReader)
                .processor(sqlitePhotoFacilityPhotoItemProcessor)
                .writer(facilityPhotoItemWriter)
                .build();
    }

    @Bean
    public ItemReader<SqlitePhoto> sqlitePhotoItemReader(SqlitePagingQueryProvider sqlitePhotoQueryProvider) {
        return new JdbcPagingItemReaderBuilder<SqlitePhoto>()
                .name("sqlitePhotoItemReader")
                .dataSource(sqliteDataSource)
                .queryProvider(sqlitePhotoQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(SqlitePhoto.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public SqlitePagingQueryProvider sqlitePhotoQueryProvider() {
        SqlitePagingQueryProvider queryProvider = new SqlitePagingQueryProvider();
        queryProvider.setSelectClause("id, type, name, image_url, description, facility_id");
        queryProvider.setFromClause("from photo");
        queryProvider.setGroupClause("GROUP BY type, name, image_url, description, facility_id");
        queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));
        return queryProvider;
    }

    @Bean
    public ItemProcessor<SqlitePhoto, FacilityPhoto> sqlitePhotoFacilityPhotoItemProcessor() {
        return FacilityPhoto::from;
    }

    @Bean
    public ItemWriter<FacilityPhoto> facilityPhotoItemWriter() {
        FlatFileItemWriter<FacilityPhoto> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/facility_photos.csv")); // 출력 CSV 파일 경로 설정
        writer.setAppendAllowed(false);

        BeanWrapperFieldExtractor<FacilityPhoto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "type", "name", "imageUrl", "description"
        };
        fieldExtractor.setNames(fieldNames);

        QuotingDelimitedLineAggregator<FacilityPhoto> lineAggregator = new QuotingDelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor); // 필드 추출기 설정

        writer.setLineAggregator(lineAggregator);

        writer.setHeaderCallback(writer1 -> {
            String headerLine = Arrays.stream(fieldNames)
                    .map(name -> {
                        if (name.contains(",") || name.contains("\"") || name.contains("\n") || name.contains("\r")) {
                            String escapedName = name.replace("\"", "\"\"");
                            return "\"" + escapedName + "\"";
                        }
                        return name;
                    })
                    .collect(Collectors.joining(","));
            writer1.write(headerLine);
        });

        return writer;
    }

    @Bean
    public Step programStep(
            ItemReader<SqliteProgram> sqliteProgramItemReader,
            ItemProcessor<SqliteProgram, FacilityProgram> sqliteProgramItemProcessor,
            ItemWriter<FacilityProgram> facilityProgramItemWriter

    ) {
        return new StepBuilder("programStep", jobRepository)
                .<SqliteProgram, FacilityProgram>chunk(500, sqliteTransactionManager) // SQLite 트랜잭션 매니저 사용
                .reader(sqliteProgramItemReader)
                .processor(sqliteProgramItemProcessor)
                .writer(facilityProgramItemWriter)
                .build();
    }

    @Bean
    public ItemReader<SqliteProgram> sqliteProgramItemReader(SqlitePagingQueryProvider sqliteProgramQueryProvider) {
        return new JdbcPagingItemReaderBuilder<SqliteProgram>()
                .name("sqliteProgramItemReader")
                .dataSource(sqliteDataSource)
                .queryProvider(sqliteProgramQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(SqliteProgram.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public SqlitePagingQueryProvider sqliteProgramQueryProvider() {
        SqlitePagingQueryProvider queryProvider = new SqlitePagingQueryProvider();
        queryProvider.setSelectClause("id, type, name, capacity, time, place, facility_id");
        queryProvider.setFromClause("from program");
        queryProvider.setGroupClause("GROUP BY type, name, capacity, time, place, facility_id");
        queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));
        return queryProvider;
    }

    @Bean
    public ItemProcessor<SqliteProgram, FacilityProgram> sqliteProgramItemProcessor() {
        return FacilityProgram::from;
    }

    @Bean
    public ItemWriter<FacilityProgram> facilityProgramItemWriter() {
        FlatFileItemWriter<FacilityProgram> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/facility_programs.csv"));
        writer.setAppendAllowed(false);

        BeanWrapperFieldExtractor<FacilityProgram> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "type", "name", "capacity", "time", "place"
        };
        fieldExtractor.setNames(fieldNames);

        QuotingDelimitedLineAggregator<FacilityProgram> lineAggregator = new QuotingDelimitedLineAggregator<>();
        lineAggregator.setDelimiter(","); // 쉼표로 구분
        lineAggregator.setFieldExtractor(fieldExtractor); // 필드 추출기 설정

        writer.setLineAggregator(lineAggregator);

        writer.setHeaderCallback(writer1 -> {
            String headerLine = Arrays.stream(fieldNames)
                    .map(name -> {
                        if (name.contains(",") || name.contains("\"") || name.contains("\n") || name.contains("\r")) {
                            String escapedName = name.replace("\"", "\"\"");
                            return "\"" + escapedName + "\"";
                        }
                        return name;
                    })
                    .collect(Collectors.joining(","));
            writer1.write(headerLine);
        });

        return writer;
    }

    @Bean
    public Step staffInfoStep(
            ItemReader<SqliteStaffInfo> sqliteStaffInfoItemReader,
            ItemProcessor<SqliteStaffInfo, FacilityStaffInformation> sqliteStaffInfoItemProcessor,
            ItemWriter<FacilityStaffInformation> facilityStaffInfoItemWriter
    ) {
        return new StepBuilder("staffInfoStep", jobRepository)
                .<SqliteStaffInfo, FacilityStaffInformation>chunk(500, sqliteTransactionManager) // SQLite 트랜잭션 매니저 사용
                .reader(sqliteStaffInfoItemReader)
                .processor(sqliteStaffInfoItemProcessor)
                .writer(facilityStaffInfoItemWriter)
                .build();
    }

    @Bean
    public ItemReader<SqliteStaffInfo> sqliteStaffInfoItemReader(SqlitePagingQueryProvider sqliteStaffInfoQueryProvider) {
        return new JdbcPagingItemReaderBuilder<SqliteStaffInfo>()
                .name("SqliteStaffInfo")
                .dataSource(sqliteDataSource)
                .queryProvider(sqliteStaffInfoQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(SqliteStaffInfo.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public SqlitePagingQueryProvider sqliteStaffInfoQueryProvider() {
        SqlitePagingQueryProvider queryProvider = new SqlitePagingQueryProvider();
        queryProvider.setSelectClause("id, directorCount, headOfOfficeCount, socialWorkerCount, residentDoctorCount, visitingDoctorCount, facility_id, nurseCount, assistantNurseCount, dentalHygienistCount, physicalTherapistCount, occupationalTherapistCount, caregiverLevel1Count, caregiverLevel2Count, caregiverDeferredCount, officeWorkerCount, dietitianCount, cookCount, hygieneWorkerCount, maintenanceWorkerCount, assistantWorkerCount, otherWorkerCount, staff_total");
        queryProvider.setFromClause("from staff_info");
        queryProvider.setGroupClause("group by facility_id");
        queryProvider.setSortKeys(Collections.singletonMap("facility_id", Order.ASCENDING));
        return queryProvider;
    }

    @Bean
    public ItemProcessor<SqliteStaffInfo, FacilityStaffInformation> sqliteStaffInfoItemProcessor() {
        return FacilityStaffInformation::from;
    }

    @Bean
    public ItemWriter<FacilityStaffInformation> facilityStaffInfoItemWriter() {
        FlatFileItemWriter<FacilityStaffInformation> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/facility_staff_information.csv"));
        writer.setAppendAllowed(false);

        BeanWrapperFieldExtractor<FacilityStaffInformation> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "directorCount", "headOfOfficeCount", "socialWorkerCount", "residentDoctorCount", "visitingDoctorCount", "nurseCount", "assistantNurseCount", "dentalHygienistCount", "physicalTherapistCount", "occupationalTherapistCount",
                "caregiverLevel1Count", "caregiverLevel2Count", "caregiverDeferredCount", "officeWorkerCount", "dietitianCount", "cookCount", "hygieneWorkerCount", "maintenanceWorkerCount", "assistantWorkerCount", "otherWorkerCount", "staffTotal"
        };
        fieldExtractor.setNames(fieldNames);

        QuotingDelimitedLineAggregator<FacilityStaffInformation> lineAggregator = new QuotingDelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);

        writer.setHeaderCallback(writer1 -> {
            String headerLine = Arrays.stream(fieldNames)
                    .map(name -> {
                        if (name.contains(",") || name.contains("\"") || name.contains("\n") || name.contains("\r")) {
                            String escapedName = name.replace("\"", "\"\"");
                            return "\"" + escapedName + "\"";
                        }
                        return name;
                    })
                    .collect(Collectors.joining(","));
            writer1.write(headerLine);
        });

        return writer;
    }

    @Bean
    public Step gradeStep(
            ItemReader<SqliteGrade> sqliteGradeItemReader,
            ItemProcessor<SqliteGrade, FacilityGrade> sqliteGradeToFacilityGradeItemProcessor,
            ItemWriter<FacilityGrade> facilityGradeItemWriter
    ) {
        return new StepBuilder("gradeStep", jobRepository)
                .<SqliteGrade, FacilityGrade>chunk(500, sqliteTransactionManager)
                .reader(sqliteGradeItemReader)
                .processor(sqliteGradeToFacilityGradeItemProcessor)
                .writer(facilityGradeItemWriter)
                .build();
    }

    @Bean
    public ItemReader<SqliteGrade> sqliteGradeItemReader(SqlitePagingQueryProvider sqliteGradeQueryProvider) {
        return new JdbcPagingItemReaderBuilder<SqliteGrade>()
                .name("sqliteGradeItemReader")
                .dataSource(sqliteDataSource)
                .queryProvider(sqliteGradeQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(SqliteGrade.class))
                .pageSize(1000)
                .build();
    }

    @Bean
    public SqlitePagingQueryProvider sqliteGradeQueryProvider() {
        SqlitePagingQueryProvider queryProvider = new SqlitePagingQueryProvider();
        queryProvider.setSelectClause("facility_id, evaluationDate, grade, totalScore, management, environmentSafety, rights, process, result");
        queryProvider.setFromClause("from grade");
        queryProvider.setGroupClause("group by facility_id");
        queryProvider.setSortKeys(Collections.singletonMap("facility_id", Order.ASCENDING));
        return queryProvider;
    }

    @Bean
    public ItemProcessor<SqliteGrade, FacilityGrade> sqliteGradeToFacilityGradeItemProcessor() {
        return FacilityGrade::from;
    }

    @Bean
    public ItemWriter<FacilityGrade> facilityGradeItemWriter() {
        FlatFileItemWriter<FacilityGrade> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/facility_grade.csv"));
        writer.setAppendAllowed(false);

        BeanWrapperFieldExtractor<FacilityGrade> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "evaluationDate", "grade", "totalScore", "management", "environmentSafety", "rights", "process", "result"
        };
        fieldExtractor.setNames(fieldNames);

        QuotingDelimitedLineAggregator<FacilityGrade> lineAggregator = new QuotingDelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);

        writer.setHeaderCallback(writer1 -> {
            String headerLine = Arrays.stream(fieldNames)
                    .map(name -> {
                        if (name.contains(",") || name.contains("\"") || name.contains("\n") || name.contains("\r")) {
                            String escapedName = name.replace("\"", "\"\"");
                            return "\"" + escapedName + "\"";
                        }
                        return name;
                    })
                    .collect(Collectors.joining(","));
            writer1.write(headerLine);
        });

        return writer;
    }



}
