package kr.co.goldenhome;

import kr.co.goldenhome.entity.*;
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
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

//@Configuration
@RequiredArgsConstructor
public class MysqlJobConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Bean
    public Job csvToMysqlJob(
                              Step csvToMysqlStep,
                              Step facilityDetailCsvToMysqlStep,
                              Step facilityPhotoCsvToMysqlStep,
                              Step facilityProgramCsvToMysqlStep,
                              Step facilityStaffInformationCsvToMysqlStep
    ) {
        return new JobBuilder("csvToMysqlJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .start(csvToMysqlStep)
                .next(facilityDetailCsvToMysqlStep)
                .next(facilityPhotoCsvToMysqlStep)
                .next(facilityProgramCsvToMysqlStep)
                .next(facilityStaffInformationCsvToMysqlStep)
                .build();
    }

    @Bean
    public Step csvToMysqlStep(
            ItemReader<Facility> csvFacilityReader,
            ItemProcessor<Facility, Facility> facilityItemProcessor,
            ItemWriter<Facility> facilityItemWriter
    ) {
        return new StepBuilder("csvToMysqlStep", jobRepository)
                .<Facility, Facility>chunk(500, platformTransactionManager) // MySQL 트랜잭션 매니저 사용
                .reader(csvFacilityReader)
                .processor(facilityItemProcessor)
                .writer(facilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<Facility> csvFacilityReader() {
        // CSV 파일의 컬럼 이름과 순서를 정의합니다. Facility 엔티티의 필드명과 일치해야 합니다.
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", // CSV에 ID 컬럼이 있다면 추가. Facility 엔티티에 ID 필드가 없다면 제거.
                "facilityType", "name", "address", "phoneNumber", "email",
                "homepage", "establishmentDate", "districtName", "grade", "capacity",
                "currentMale", "currentFemale", "currentTotal", "staffTotal"
        };

        return new FlatFileItemReaderBuilder<Facility>()
                .name("csvFacilityReader")
                .resource(new FileSystemResource("output/facilities.csv"))
                .delimited() // 구분자로 분리된 파일 형식
                .names(fieldNames) // CSV 컬럼 이름 설정
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Facility.class); // 매핑할 대상 클래스
                }})
                .build();
    }

    @Bean
    ItemProcessor<Facility, Facility> facilityItemProcessor() {
        return Facility::calculateTotal;
    }

    /**
     * Facility 객체를 MySQL 테이블에 쓰는 ItemWriter
     * mysqlDataSource를 명시적으로 사용합니다.
     */
    @Bean
    public ItemWriter<Facility> facilityItemWriter() {
        return new JdbcBatchItemWriterBuilder<Facility>()
                .dataSource(dataSource)
                .sql("INSERT INTO facilities ( " +
                        "    institution_symbol, facility_type, name, address, phone_number, email, homepage, establishment_date, district_name, grade, capacity, current_male, current_female, current_total, staff_total" +
                        ") VALUES (" +
                        "    :institutionSymbol, :facilityType, :name, :address, :phoneNumber, :email, :homepage, :establishmentDate, :districtName, :grade, :capacity, :currentMale, :currentFemale, :currentTotal, :staffTotal" +
                        ")")
                .beanMapped()
                .build();
    }


    @Bean
    public Step facilityDetailCsvToMysqlStep(
            ItemReader<FacilityDetail> facilityDetailItemReader,
            ItemWriter<FacilityDetail> facilityDetailItemWriter
    ) {
        return new StepBuilder("facilityDetailCsvToMysqlStep", jobRepository)
                .<FacilityDetail, FacilityDetail>chunk(500, platformTransactionManager) // MySQL 트랜잭션 매니저 사용
                .reader(facilityDetailItemReader)
                .writer(facilityDetailItemWriter)
                .build();
    }


    @Bean
    public ItemReader<FacilityDetail> facilityDetailItemReader() {
        // CSV 파일의 컬럼 이름과 순서를 정의합니다. Facility 엔티티의 필드명과 일치해야 합니다.
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "singleRoomCount", "doubleRoomCount", "tripleRoomCount", "quadRoomCount", "officeCount",
                "medicalNurseRoomCount", "dailyLivingTrainingRoomCount", "programRoomCount", "kitchenDiningRoomCount", "bathroomCount",
                "washBathRoomCount", "laundryRoomCount"
        };

        return new FlatFileItemReaderBuilder<FacilityDetail>()
                .name("facilityDetailItemReader")
                .resource(new FileSystemResource("output/facility_details.csv"))
                .delimited() // 구분자로 분리된 파일 형식
                .names(fieldNames) // CSV 컬럼 이름 설정
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(FacilityDetail.class); // 매핑할 대상 클래스
                }})
                .build();
    }


    @Bean
    public ItemWriter<FacilityDetail> facilityDetailItemWriter() {
        return new JdbcBatchItemWriterBuilder<FacilityDetail>()
                .dataSource(dataSource)
                .sql("INSERT INTO facility_details ( " +
                        "    institution_symbol, single_room_count, double_room_count, triple_room_count, quad_room_count, office_count, medical_nurse_room_count, daily_living_training_room_count, program_room_count, kitchen_dining_room_count, bathroom_count, wash_bath_room_count, laundry_room_count" +
                        ") VALUES (" +
                        "    :institutionSymbol, :singleRoomCount, :doubleRoomCount, :tripleRoomCount, :quadRoomCount, :officeCount, :medicalNurseRoomCount, :dailyLivingTrainingRoomCount, :programRoomCount, :kitchenDiningRoomCount, :bathroomCount, :washBathRoomCount, :laundryRoomCount" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step facilityPhotoCsvToMysqlStep(
            ItemReader<FacilityPhoto> facilityPhotoItemReader,
            ItemWriter<FacilityPhoto> facilityPhotoItemWriter
    ) {
        return new StepBuilder("facilityPhotoCsvToMysqlStep", jobRepository)
                .<FacilityPhoto, FacilityPhoto>chunk(500, platformTransactionManager) // MySQL 트랜잭션 매니저 사용
                .reader(facilityPhotoItemReader)
                .writer(facilityPhotoItemWriter)
                .build();
    }

    @Bean
    public ItemReader<FacilityPhoto> facilityPhotoItemReader() {
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "type", "name", "imageUrl", "description"
        };

        return new FlatFileItemReaderBuilder<FacilityPhoto>()
                .name("facilityPhotoItemReader")
                .resource(new FileSystemResource("output/facility_photos.csv"))
                .delimited()
                .names(fieldNames)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(FacilityPhoto.class);
                }})
                .build();
    }

    @Bean
    public ItemWriter<FacilityPhoto> facilityPhotoItemWriter() {
        return new JdbcBatchItemWriterBuilder<FacilityPhoto>()
                .dataSource(dataSource)
                .sql("INSERT INTO facility_photos ( " +
                        "    institution_symbol, type, name, image_url, description" +
                        ") VALUES (" +
                        "    :institutionSymbol, :type, :name, :imageUrl, :description" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step facilityProgramCsvToMysqlStep(
            ItemReader<FacilityProgram> facilityProgramItemReader,
            ItemWriter<FacilityProgram> facilityProgramItemWriter
    ) {
        return new StepBuilder("facilityProgramCsvToMysqlStep", jobRepository)
                .<FacilityProgram, FacilityProgram>chunk(500, platformTransactionManager) // MySQL 트랜잭션 매니저 사용
                .reader(facilityProgramItemReader)
                .writer(facilityProgramItemWriter)
                .build();
    }

    @Bean
    public ItemReader<FacilityProgram> facilityProgramItemReader() {
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "type", "name", "capacity", "time", "place"
        };

        return new FlatFileItemReaderBuilder<FacilityProgram>()
                .name("facilityProgramItemReader")
                .resource(new FileSystemResource("output/facility_programs.csv"))
                .delimited()
                .names(fieldNames)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(FacilityProgram.class);
                }})
                .build();
    }

    @Bean
    public ItemWriter<FacilityProgram> facilityProgramItemWriter() {
        return new JdbcBatchItemWriterBuilder<FacilityProgram>()
                .dataSource(dataSource)
                .sql("INSERT INTO facility_programs ( " +
                        "    institution_symbol, type, name, capacity, time, place" +
                        ") VALUES (" +
                        "    :institutionSymbol, :type, :name, :capacity, :time, :place" +
                        ")")
                .beanMapped()
                .build();
    }

    @Bean
    public Step facilityStaffInformationCsvToMysqlStep(
        ItemReader<FacilityStaffInformation> facilityStaffInformationItemReader,
        ItemWriter<FacilityStaffInformation> facilityStaffInformationItemWriter
    ) {
        return new StepBuilder("facilityStaffInformationCsvToMysqlStep", jobRepository)
                .<FacilityStaffInformation, FacilityStaffInformation>chunk(500, platformTransactionManager) // MySQL 트랜잭션 매니저 사용
                .reader(facilityStaffInformationItemReader)
                .writer(facilityStaffInformationItemWriter)
                .build();
    }

    @Bean
    public ItemReader<FacilityStaffInformation> facilityStaffInformationItemReader() {
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol", "directorCount", "headOfOfficeCount", "socialWorkerCount", "residentDoctorCount", "visitingDoctorCount", "nurseCount", "assistantNurseCount", "dentalHygienistCount", "physicalTherapistCount", "occupationalTherapistCount",
                "caregiverLevel1Count", "caregiverLevel2Count", "caregiverDeferredCount", "officeWorkerCount", "dietitianCount", "cookCount", "hygieneWorkerCount", "maintenanceWorkerCount", "assistantWorkerCount", "otherWorkerCount", "staffTotal"
        };

        return new FlatFileItemReaderBuilder<FacilityStaffInformation>()
                .name("facilityStaffInformationItemReader")
                .resource(new FileSystemResource("output/facility_staff_information.csv"))
                .delimited()
                .names(fieldNames)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(FacilityStaffInformation.class);
                }})
                .build();
    }

    @Bean
    public ItemWriter<FacilityStaffInformation> facilityStaffInformationItemWriter() {
        return new JdbcBatchItemWriterBuilder<FacilityStaffInformation>()
                .dataSource(dataSource)
                .sql("INSERT INTO facility_staff_information ( " +
                        "    institution_symbol, director_count, head_of_office_count, social_worker_count, resident_doctor_count, visiting_doctor_count, nurse_count, assistant_nurse_count, dental_hygienist_count, physical_therapist_count, occupational_therapist_count, caregiver_level1_count, caregiver_level2_count, caregiver_deferred_count, office_worker_count, dietitian_count, cook_count, hygiene_worker_count, maintenance_worker_count, assistant_worker_count, other_worker_count, staff_total" +
                        ") VALUES (" +
                        "    :institutionSymbol, :directorCount, :headOfOfficeCount, :socialWorkerCount, :residentDoctorCount, :visitingDoctorCount, :nurseCount, :assistantNurseCount, :dentalHygienistCount, :physicalTherapistCount, :occupationalTherapistCount, :caregiverLevel1Count, :caregiverLevel2Count, :caregiverDeferredCount, :officeWorkerCount, :dietitianCount, :cookCount, :hygieneWorkerCount, :maintenanceWorkerCount, :assistantWorkerCount, :otherWorkerCount, :staffTotal" +
                        ")")
                .beanMapped()
                .build();
    }
}
