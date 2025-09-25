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

@Configuration
@RequiredArgsConstructor
public class UpdateMysqlJobConfig {

    private final JobRepository jobRepository;
    private final DataSource dataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;

    @Bean
    public Job csvToMysqlJob(
                              Step csvToMysqlStep
    ) {
        return new JobBuilder("csvToMysqlJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .start(csvToMysqlStep)
                .build();
    }

    @Bean
    public Step csvToMysqlStep(
            ItemReader<Facility> csvFacilityReader,
            ItemProcessor<Facility, Facility> facilityItemProcessor,
            ItemWriter<Facility> facilityItemWriter
    ) {
        return new StepBuilder("csvToMysqlStep", jobRepository)
                .<Facility, Facility>chunk(500, platformTransactionManager)
                .reader(csvFacilityReader)
                .processor(facilityItemProcessor)
                .writer(facilityItemWriter)
                .build();
    }

    @Bean
    public ItemReader<Facility> csvFacilityReader() {
        String[] fieldNames = new String[]{
                "id",
                "institutionSymbol",
                "facilityType", "name", "address", "phoneNumber", "email",
                "homepage", "establishmentDate", "districtName", "capacity",
                "currentMale", "currentFemale", "currentTotal", "staffTotal",
                "latitude", "longitude"
        };

        return new FlatFileItemReaderBuilder<Facility>()
                .name("csvFacilityReader")
                .resource(new FileSystemResource("output/facilities.csv"))
                .delimited()
                .names(fieldNames)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Facility.class);
                }})
                .build();
    }

    @Bean
    ItemProcessor<Facility, Facility> facilityItemProcessor() {
        return Facility::calculateTotal;
    }

    @Bean
    public ItemWriter<Facility> facilityItemWriter() {
        return new JdbcBatchItemWriterBuilder<Facility>()
                .dataSource(dataSource)
                .sql("UPDATE facilities SET " +
                        "    latitude = :latitude, " +
                        "    longitude = :longitude " +
                        "WHERE institution_symbol = :institutionSymbol")
                .beanMapped()
                .build();
    }

}
