package ludo.mentis.aciem.commons.web;

import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebUtilsTest {

    @Test
    void testGetRequest() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attributes);

        HttpServletRequest request = WebUtils.getRequest();
        assertNotNull(request);
        assertEquals(mockRequest, request);
    }

    @Test
    void testGetRequest_NoRequestAttributes() {
        RequestContextHolder.resetRequestAttributes();

        IllegalStateException exception = assertThrows(IllegalStateException.class, WebUtils::getRequest);
        assertEquals("RequestContextHolder.getRequestAttributes() cannot be null", exception.getMessage());
    }
}