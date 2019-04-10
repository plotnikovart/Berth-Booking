package app.service.fileStorage;

import app.common.ServiceException;

public enum ImageKind {

    USER("users"), SHIP("ships"), BERTH("berths");

    ImageKind(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public static ImageKind get(String imageKindStr) {
        try {
            return ImageKind.valueOf(imageKindStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ServiceException("Для типа изоображения '" + imageKindStr + "' не найдено перечисление", e);
        }
    }

    private String folderName;
}
