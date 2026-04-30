package com.nhnacademy.springsecurityfinal.config.converter;

import com.nhnacademy.springsecurityfinal.model.Role;
import com.nhnacademy.springsecurityfinal.model.dto.MemberCreateRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// 회원가입
public class CsvHttpMessageReadConverter extends AbstractHttpMessageConverter<MemberCreateRequest> {

    public CsvHttpMessageReadConverter() {
        super(new MediaType("text", "csv"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return MemberCreateRequest.class.equals(clazz);
    }

    @Override
    protected MemberCreateRequest readInternal(Class<? extends MemberCreateRequest> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
       MemberCreateRequest request;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputMessage.getBody()))) {
            br.readLine(); // (id,name...)
            String data = br.readLine();

            // TODO data가 없을 때 exception

            String[] split = data.split(",");
            request = new MemberCreateRequest(
                    split[0].trim(),
                    split[1].trim(),
                    split[2].trim(),
                    Integer.parseInt(split[3]),
                    Role.fromString(split[4])
            );
        }

        return request;
    }

    @Override
    protected void writeInternal(MemberCreateRequest request, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    }

    @Override
    protected boolean canWrite(@Nullable MediaType mediaType) {
        return false;
    }

}
