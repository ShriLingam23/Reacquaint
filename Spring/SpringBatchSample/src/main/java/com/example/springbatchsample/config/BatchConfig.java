package com.example.springbatchsample.config;

import com.example.springbatchsample.entity.Invoice;
import com.example.springbatchsample.repository.InvoiceRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
//@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Bean
    public FlatFileItemReader<Invoice> reader(){
        FlatFileItemReader<Invoice> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("/invoices.csv"));
        reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper<>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){
                {
                    setDelimiter(DELIMITER_COMMA);
                    setNames("name", "number", "amount", "discount", "location");
                }
            });

            setFieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
                setTargetType(Invoice.class);
            }});
        }});

        reader.setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());

        return reader;

    }

    @Bean
    public ItemWriter<Invoice> writer(){
        return invoices -> {
            System.out.println("Saving Invoice Records: " +invoices);
            invoiceRepository.saveAll(invoices);
        };
    }


    @Bean
    public ItemProcessor<Invoice, Invoice> processor(){
        return invoice -> {
            Double discount = invoice.getAmount() * (invoice.getDiscount()/100.0);
            Double finalAmount = invoice.getAmount() - discount;
            invoice.setFinalAmount(finalAmount);
            return invoice;
        };
    }

    @Bean
    public JobExecutionListener listener(){
        return new InvoiceListener();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }


    @Bean
    public Step stepA(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("csv-step", jobRepository)
                .<Invoice, Invoice>chunk(2, transactionManager)
                .reader(reader())
                .processor(processor()) // default interface passed in
                .writer(writer()) // default interface passed in
                .build();
    }

    @Bean
    public Job jobA(JobRepository jobRepository, JobExecutionListener listener, @Qualifier("stepA") Step stepA) {
        return new JobBuilder("csv-job", jobRepository)
                .listener(listener)
                .start(stepA)
                // .next(stepB())
                // .next(stepC())
                .build();
    }

}
