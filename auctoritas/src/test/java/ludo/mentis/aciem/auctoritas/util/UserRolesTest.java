package ludo.mentis.aciem.auctoritas.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class UserRolesTest {

    @Test
    void testPublicStaticFinalStrings() throws IllegalAccessException {
        Field[] fields = UserRoles.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) &&
                Modifier.isStatic(field.getModifiers()) &&
                Modifier.isFinal(field.getModifiers()) &&
                field.getType().equals(String.class)) {

                String fieldName = field.getName();
                String fieldValue = (String) field.get(null);
                assertEquals(fieldName, fieldValue, "Field " + fieldName + " does not match its value");
            }
        }
    }

    @Test
    void testPrivateConstructor() {
        Constructor<?>[] constructors = UserRoles.class.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()), "Constructor should be private");
            constructor.setAccessible(true);
            var exception = assertThrows(InvocationTargetException.class, constructor::newInstance, "Constructor should throw InvocationTargetException");
            assertInstanceOf(IllegalStateException.class, exception.getCause(), "Constructor should throw AssertionError");
        }
    }
}