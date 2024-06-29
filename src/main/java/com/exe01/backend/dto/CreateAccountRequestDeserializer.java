package com.exe01.backend.dto;
import com.exe01.backend.dto.request.account.CreateAccountRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CreateAccountRequestDeserializer extends JsonDeserializer<CreateAccountRequest> {

    @Override
    public CreateAccountRequest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        return mapper.readValue(p, CreateAccountRequest.class);
    }
}