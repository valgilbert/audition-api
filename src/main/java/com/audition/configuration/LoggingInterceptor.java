package com.audition.configuration;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;


@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Autowired
    private AuditionLogger logger;

    public AuditionLogger getLogger() {
        return this.logger;
    }

    public void setLogger(final AuditionLogger logger) {
        this.logger = logger;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] requestBody,
        final ClientHttpRequestExecution execution) {
        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, "Request body: " + new String(requestBody, UTF_8));
        }
        final ClientHttpResponse clientHttpResponse;
        try {
            clientHttpResponse = execution.execute(request, requestBody);
        } catch (IOException e) {
            throw new SystemException("Exception occurred", "Request Error", e);
        }
        return clientHttpResponse;
    }
}
