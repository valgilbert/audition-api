package com.audition.web.advice;

import static com.audition.web.advice.ExceptionControllerAdvice.DEFAULT_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.audition.common.exception.SystemException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
class ExceptionControllerAdviceTest {

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;


    @Test
    void testHandleHttpClientException() {
        // Given
        final HttpClientErrorException ex = new HttpClientErrorException(BAD_REQUEST);
        final ProblemDetail problemDetail = createProblemDetail(ex, ex.getStatusCode(), ex.getMessage());
        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<ExceptionControllerAdvice> clazz = ExceptionControllerAdvice.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final ProblemDetail actual = exceptionControllerAdvice.handleHttpClientException(ex);
        // Verify
        assertEquals(problemDetail, actual);
    }

    @Test
    void testHandleMainException() {
        // Given
        final Exception ex = new Exception();
        final ProblemDetail problemDetail = createProblemDetail(ex, INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE);
        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<ExceptionControllerAdvice> clazz = ExceptionControllerAdvice.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final ProblemDetail actual = exceptionControllerAdvice.handleMainException(ex);
        // Verify
        assertEquals(problemDetail, actual);
    }

    @Test
    void testHandleMainExceptionHttpClientException() {
        // Given
        final HttpClientErrorException ex = new HttpClientErrorException(BAD_REQUEST);
        final ProblemDetail problemDetail = createProblemDetail(ex, BAD_REQUEST, "400 BAD_REQUEST");
        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<ExceptionControllerAdvice> clazz = ExceptionControllerAdvice.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final ProblemDetail actual = exceptionControllerAdvice.handleMainException(ex);
        // Verify
        assertEquals(problemDetail, actual);
    }

    @Test
    void testHandleMainExceptionHHttpRequestMethodNotSupportedException() {
        // Given
        final HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("Method");
        final ProblemDetail problemDetail = createProblemDetail(ex, METHOD_NOT_ALLOWED,
            "Request method 'Method' is not supported");
        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<ExceptionControllerAdvice> clazz = ExceptionControllerAdvice.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final ProblemDetail actual = exceptionControllerAdvice.handleMainException(ex);
        // Verify
        assertEquals(problemDetail, actual);
    }

    @Test
    void testHandleSystemException() {
        // Given
        final SystemException ex = new SystemException(DEFAULT_MESSAGE, INTERNAL_SERVER_ERROR.value());
        final ProblemDetail problemDetail = createProblemDetail(ex, INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE);
        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<ExceptionControllerAdvice> clazz = ExceptionControllerAdvice.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final ProblemDetail actual = exceptionControllerAdvice.handleSystemException(ex);
        // Verify
        assertEquals(problemDetail, actual);
    }

    @Test
    void testHandleSystemExceptionInvalidCode() {
        // Given
        final SystemException ex = new SystemException(DEFAULT_MESSAGE, 99);
        final ProblemDetail problemDetail = createProblemDetail(ex, INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE);
        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<ExceptionControllerAdvice> clazz = ExceptionControllerAdvice.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final ProblemDetail actual = exceptionControllerAdvice.handleSystemException(ex);
        // Verify
        assertEquals(problemDetail, actual);
    }


    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode, final String detail) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(detail);
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(ExceptionControllerAdvice.DEFAULT_TITLE);
        }
        return problemDetail;
    }
}
