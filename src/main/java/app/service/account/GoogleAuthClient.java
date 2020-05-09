package app.service.account;

import app.config.exception.impl.UnauthorizedException;
import app.service.account.dto.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;

@Slf4j
@Component
public class GoogleAuthClient {

    private final HttpTransport transport;
    private final JsonFactory jsonFactory;
    private final GoogleClientSecrets clientSecrets;

    @Value("${google.secret_path}")
    private String secretPath;

    public GoogleAuthClient(@Value("${google.secret_path}") String secretPath) throws Exception {
        transport = new NetHttpTransport();
        jsonFactory = new JacksonFactory();
        clientSecrets = GoogleClientSecrets.load(jsonFactory, new FileReader(secretPath));
    }

    public GoogleUserInfo authenticate(String authCode, String redirectUri) throws UnauthorizedException {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory, "https://oauth2.googleapis.com/token",
                    clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(),
                    authCode, redirectUri)
                    .execute();

            String accessToken = tokenResponse.getAccessToken();

            var credential = new GoogleCredential().setAccessToken(accessToken);
            var oauth2 = new Oauth2.Builder(transport, jsonFactory, credential).setApplicationName("Oauth2").build();
            var userInfo = oauth2.userinfo().get().execute();

            return new GoogleUserInfo()
                    .setGmail(userInfo.getEmail())
                    .setFirstName(userInfo.getGivenName())
                    .setLastName(userInfo.getFamilyName())
                    .setPhotoLink(userInfo.getPicture());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UnauthorizedException();
        }
    }
}
