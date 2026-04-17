package com.nhnacademy.springmvcfinal.domain.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class InquiryFile {
    @Setter
    private int fileId;
    private final int inquiryId;
    private final String fileName;

    private InquiryFile (int inquiryId, String fileName) {
        this.inquiryId = inquiryId;
        this.fileName = fileName;
    }

    public static InquiryFile create(int inquiryId, String fileName) {
        return new InquiryFile(inquiryId, fileName);
    }

}

