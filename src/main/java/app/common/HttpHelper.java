package app.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

public class HttpHelper {

    public static void addCookie(HttpServletResponse resp, Cookie cookie) {
        String other = MessageFormat.format("{0}={1};path=/;max-age={2}", cookie.getName(), cookie.getValue(), String.valueOf(cookie.getMaxAge()));
        String chrome = other + ";SameSite=None";

        resp.addHeader("Set-Cookie", other);
        resp.addHeader("Set-Cookie", chrome);
    }
}
