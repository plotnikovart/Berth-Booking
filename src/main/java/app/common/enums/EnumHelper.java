package app.common.enums;

import app.common.exception.ServiceException;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class EnumHelper {

    private static Map<Class, Map<?, ?>> container = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Nullable
    public static <E extends EnumWithIdentifier<ID>, ID> E getEnumByIdentifier(ID id, Class<E> enumClass) throws ServiceException {
        if (!enumClass.isEnum()) {
            throw new ServiceException("not enum");
        }

        Map<ID, E> idToValues = (Map<ID, E>) container.get(enumClass);

        if (idToValues == null) {

            synchronized (EnumHelper.class) {
                idToValues = (Map<ID, E>) container.get(enumClass);
                if (idToValues == null) {
                    idToValues = (Map<ID, E>) new HashMap();

                    for (E e : enumClass.getEnumConstants()) {
                        if (idToValues.put(e.getIdentifier(), e) != null) {
                            throw new ServiceException("found duplicate id");
                        }
                    }
                }

                container.put(enumClass, idToValues);
            }
        }

        return idToValues.get(id);
    }
}
