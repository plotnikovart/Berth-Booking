package ru.hse.coursework.berth.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;
import ru.hse.coursework.berth.config.exception.impl.ServiceException;

@Slf4j
public abstract class AbstractHttpClient {

    protected abstract ObjectMapper getMapper();

    protected abstract RestTemplate getClient();


    protected <R> R sendRequest(String url, HttpMethod method, @Nullable Object body, @Nullable Class<R> respClass) throws ServiceException {
        JavaType javaType = respClass == null ? null : getMapper().getTypeFactory().constructType(respClass);
        return sendRequest(url, method, body, javaType);
    }


    protected <R> R sendRequest(String url, HttpMethod method, @Nullable Object body, @Nullable JavaType respClass) throws ServiceException {
        try {
            var headers = new HttpHeaders();
            addHeadersToReq(headers, body);

            var bodyJson = body == null ? null : getMapper().writeValueAsString(body);

            var entity = new HttpEntity<>(bodyJson, headers);

            ResponseEntity<String> resp = getClient().exchange(url, method, entity, String.class);
            checkBodyForError(resp.getBody(), resp.getStatusCodeValue());

            return respClass == null ? null : getMapper().readValue(resp.getBody(), respClass);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
    }


    protected void addHeadersToReq(HttpHeaders headers, @Nullable Object body) {
        if (body != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
    }

    protected void checkBodyForError(String body, int code) {
        // throw exception if necessary
    }
}