package app.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

public class HttpHelper {

    public static void addCookie(HttpServletResponse resp, Cookie cookie) {
//        resp.addCookie(cookie);

        String value = MessageFormat.format("{0}={1};path=/;SameSite=None", cookie.getName(), cookie.getValue());
        resp.addHeader("Set-Cookie", value);
    }
}
