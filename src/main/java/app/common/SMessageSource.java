package app.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SMessageSource {

    private final static MessageSource mSource;

    static {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:messages/label");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        mSource = source;
    }

    public static String get(String code) throws NoSuchMessageException {
        return mSource.getMessage(code, null, Locale.getDefault());
    }

    public static String get(String code, Object... args) throws NoSuchMessageException {
        return mSource.getMessage(code, args, Locale.getDefault());
    }
}
