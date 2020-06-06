package ru.hse.coursework.berth.service.account.facebook;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.hse.coursework.berth.common.AbstractHttpClient;
import ru.hse.coursework.berth.config.exception.impl.UnauthorizedException;

@Component
@RequiredArgsConstructor
public class FacebookAuthClient extends AbstractHttpClient {

    private final RestTemplate client;
    private final ObjectMapper mapper;

    @Value("${facebook.client_id}")
    private String clientId;
    @Value("${facebook.client_secret}")
    private String clientSecret;

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    protected RestTemplate getClient() {
        return client;
    }


    public FacebookUserInfo authenticate(String code, String redirectUri) throws UnauthorizedException {
        try {
            var accessTokenUri = "https://graph.facebook.com/v7.0/oauth/access_token" +
                    "?client_id=" + clientId +
                    "&redirect_uri=" + redirectUri +
                    "&client_secret=" + clientSecret +
                    "&code=" + code;

            GetAccessTokenResp accessToken = sendRequest(accessTokenUri, HttpMethod.GET, null, GetAccessTokenResp.class);

            var userInfoUri = "https://graph.facebook.com/me" +
                    "?fields=id,first_name,last_name,profile_pic,email" +
                    "&access_token=" + accessToken.getAccessToken();

            return sendRequest(userInfoUri, HttpMethod.GET, null, FacebookUserInfo.class);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }
}
