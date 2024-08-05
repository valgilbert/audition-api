package com.audition.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@ExtendWith(MockitoExtension.class)
class LoggingInterceptorTest {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Mock
    private AuditionLogger logger;

    @InjectMocks
    private LoggingInterceptor loggingInterceptor;


    @Test
    void testInterceptShouldInterceptResponse() {
        // Given
        final String body = "Body";
        final HttpRequest httpRequest = mock(HttpRequest.class);

        final ClientHttpRequestExecution clientHttpRequestExecution = mock(ClientHttpRequestExecution.class);

        // make PMD-test happy
        try (ClientHttpResponse clientHttpResponse = mock(ClientHttpResponse.class)) {
            //When
            when(clientHttpRequestExecution.execute(httpRequest, body.getBytes(StandardCharsets.UTF_8))).thenReturn(
                clientHttpResponse);
        } catch (IOException ioException) {
            if (LOG.isDebugEnabled()) {
                LOG.error("error", ioException);
            }
        }

        // Execute
        try (ClientHttpResponse actual = loggingInterceptor.intercept(httpRequest,
            body.getBytes(StandardCharsets.UTF_8), clientHttpRequestExecution)) {
            // Verify
            assertNotNull(actual);
            assertNotNull(logger);
        }
    }

    @Test
    void testInterceptShouldInterceptResponseException() throws IOException {
        // Given
        final String body = "Body";
        final HttpRequest httpRequest = mock(HttpRequest.class);
        final ClientHttpRequestExecution clientHttpRequestExecution = mock(ClientHttpRequestExecution.class);

        //When
        when(clientHttpRequestExecution.execute(httpRequest, body.getBytes(StandardCharsets.UTF_8))).thenThrow(
            new IOException("Unable to reach host"));

        final SystemException thrown = assertThrows(
            SystemException.class,
            () -> {
                // Execute
                try (ClientHttpResponse actual = loggingInterceptor.intercept(httpRequest,
                    body.getBytes(StandardCharsets.UTF_8), clientHttpRequestExecution)) {
                    // Verify
                    assertNotNull(actual);
                }
            },
            "Exception occurred"
        );
        assertEquals("Exception occurred", thrown.getMessage());

    }

}
