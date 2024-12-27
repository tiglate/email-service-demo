package ludo.mentis.aciem.tabellarius.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class LogHelperTest {

    @Test
    void testTraceMethodCall() {
        Logger mockLogger = mock(Logger.class);
        when(mockLogger.isTraceEnabled()).thenReturn(true);

        LogHelper.setLogger(mockLogger);

        LogHelper.traceMethodCall(LogHelperTest.class, "testMethod", "param1", 123);

        verify(mockLogger).trace("ludo.mentis.aciem.tabellarius.util.LogHelperTest.testMethod(param1, 123)");
    }
}