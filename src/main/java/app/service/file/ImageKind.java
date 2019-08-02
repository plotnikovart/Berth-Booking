package app.service.file;

import app.common.SMessageSource;
import app.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageKind {

    USER("users"), SHIP("ships"), BERTH("berths");

    private String folderName;

    public static ImageKind get(String imageKindStr) {
        try {
            return ImageKind.valueOf(imageKindStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            String message = SMessageSource.get("image.not_found", imageKindStr);
            throw new ServiceException(message, e);
        }
    }
}
