package com.nhnacademy.springsecurityfinal.config.converter;

import com.nhnacademy.springsecurityfinal.model.dto.MemberResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

// 멤버 조회
public class CsvHttpMessageWriteConverter extends AbstractHttpMessageConverter<MemberResponse> {

    public CsvHttpMessageWriteConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return MemberResponse.class.equals(clazz);
    }

    @Override
    protected MemberResponse readInternal(Class<? extends MemberResponse> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected boolean canRead(@Nullable MediaType mediaType) {
        return false;
    }

    @Override
    protected void writeInternal(MemberResponse memberResponse, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentType(MediaType.valueOf("text/csv; charset=UTF-8"));

        try(Writer writer = new OutputStreamWriter(outputMessage.getBody())) {
            writer.write("id,name,age,role\n");
            String data = String.join(",",
                    memberResponse.getId(),
                    memberResponse.getName(),
                    String.valueOf(memberResponse.getAge()),
                    memberResponse.getRole().toString());

            writer.write(data);
            writer.flush();
        }
    }

}
