package com.example.springbatchsample.config;

import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;

@SuppressWarnings("NullableProblems")
public class BlankLineRecordSeparatorPolicy extends SimpleRecordSeparatorPolicy {

    @Override
    public boolean isEndOfRecord(String line) {
        return !line.trim().isEmpty() && super.isEndOfRecord(line);
    }

    @Override
    public String postProcess(String record) {
        if (record.trim().isEmpty()) {
            return null;
        }
        return super.postProcess(record);
    }
}
