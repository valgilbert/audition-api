package com.audition.common.logging;

import static com.audition.web.advice.ExceptionControllerAdvice.DEFAULT_MESSAGE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionPost;
import com.audition.web.advice.ExceptionControllerAdvice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

@ExtendWith(MockitoExtension.class)
class AuditionLoggerTest {

    public static final String LOG_ERROR = "Log error";
    private AuditionLogger auditionLogger = new AuditionLogger();

    @Test
    void testInfoShouldLogMessage() {
        // Given
        final String message = "Log info";
        final Logger logger = mock(Logger.class);

        // When
        when(logger.isInfoEnabled()).thenReturn(true);

        // Execute
        auditionLogger.info(logger, message);

        // Verify
        verify(logger, times(1)).info(message);
    }

    @Test
    void testInfoShouldLogMessageAndObject() {
        // Given
        final String message = "Log info";
        final Logger logger = mock(Logger.class);
        final AuditionPost auditionPost = new AuditionPost();

        // When
        when(logger.isInfoEnabled()).thenReturn(true);

        // Execute
        auditionLogger.info(logger, message, auditionPost);

        // Verify
        verify(logger, times(1)).info(message, auditionPost);
    }


    @Test
    void testDebugShouldLogMessage() {
        // Given
        final String message = "Log debug";
        final Logger logger = mock(Logger.class);

        // When
        when(logger.isDebugEnabled()).thenReturn(true);

        // Execute
        auditionLogger.debug(logger, message);

        // Verify
        verify(logger, times(1)).debug(message);
    }

    @Test
    void testWarnShouldLogMessage() {
        // Given
        final String message = "Log warn";
        final Logger logger = mock(Logger.class);

        // When
        when(logger.isWarnEnabled()).thenReturn(true);

        // Execute
        auditionLogger.warn(logger, message);

        // Verify
        verify(logger, times(1)).warn(message);
    }


    @Test
    void testErrorShouldLogMessage() {
        // Given
        final String message = LOG_ERROR;
        final Logger logger = mock(Logger.class);

        // When
        when(logger.isErrorEnabled()).thenReturn(true);

        // Execute
        auditionLogger.error(logger, message);

        // Verify
        verify(logger, times(1)).error(message);
    }

    @Test
    void testLogErrorWithExceptionShouldLogMessage() {
        // Given
        final String message = LOG_ERROR;
        final Logger logger = mock(Logger.class);
        final Exception ex = new Exception();

        // When
        when(logger.isErrorEnabled()).thenReturn(true);

        // Execute
        auditionLogger.logErrorWithException(logger, message, ex);

        // Verify
        verify(logger, times(1)).error(message, ex);
    }

    @Test
    void testLogStandardProblemDetailShouldLogMessage() {
        // Given
        final Logger logger = mock(Logger.class);
        final Exception ex = new Exception();
        final ProblemDetail problemDetail = createProblemDetail(ex, INTERNAL_SERVER_ERROR, DEFAULT_MESSAGE);

        // When
        when(logger.isErrorEnabled()).thenReturn(true);

        // Execute
        auditionLogger.logStandardProblemDetail(logger, problemDetail, ex);

        // Verify
        verify(logger, times(1)).error(String.format("Status: %1$s, Detail: %2$s. Type: %3$s. Title: %4$s.",
            problemDetail.getStatus(),
            problemDetail.getDetail(),
            problemDetail.getType(),
            problemDetail.getTitle()), ex);
    }


    @Test
    void testLogHttpStatusCodeErrorShouldLogMessage() {
        // Given
        final String message = LOG_ERROR;
        final Logger logger = mock(Logger.class);
        final Integer statusCode = INTERNAL_SERVER_ERROR.value();

        // When
        when(logger.isErrorEnabled()).thenReturn(true);

        // Execute
        auditionLogger.logHttpStatusCodeError(logger, message, statusCode);

        // Verify
        verify(logger, times(1)).error(String.format("ErrorCode: %1$s, Message: %2$s.", statusCode, message) + "\n");

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
