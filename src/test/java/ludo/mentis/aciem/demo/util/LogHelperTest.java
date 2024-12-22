package ludo.mentis.aciem.demo.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class LogHelperTest {

    @Test
    void testTraceMethodCall() {
        Logger mockLogger = mock(Logger.class);
        when(mockLogger.isTraceEnabled()).thenReturn(true);

        LogHelper.setLogger(mockLogger);

        LogHelper.traceMethodCall("testMethod", "param1", 123);

        verify(mockLogger).trace("testMethod(param1, 123)");
    }
}