package com.audition.integration;

import static java.lang.String.format;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuditionIntegrationClient {

    private static final String BASE_API_PATH = "https://jsonplaceholder.typicode.com";
    private static final String LOG_REQUEST_FORMAT = "Sending request to url %1$s.";
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown Error message: ";
    private static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuditionLogger logger;

    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }


    public void setLogger(final AuditionLogger logger) {
        this.logger = logger;
    }

    public AuditionLogger getLogger() {
        return this.logger;
    }

    public List<AuditionPost> getPosts() {
        final String url = format("%1$s/posts", BASE_API_PATH);
        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, format(LOG_REQUEST_FORMAT, url));
        }

        try {
            final AuditionPost[] auditionPostArr = restTemplate.getForObject(url, AuditionPost[].class);
            return auditionPostArr != null ? Arrays.asList(auditionPostArr) : new ArrayList<>();
        } catch (final HttpClientErrorException e) {
            throw new SystemException("Cannot find a Posts, Resource Not Found",
                e.getMessage(), e.getStatusCode().value(), e);
        }
    }

    public AuditionPost getPostById(final String id) {
        final String url = format("%1$s/posts/%2$s", BASE_API_PATH, id);
        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, format(LOG_REQUEST_FORMAT, url));
        }

        try {
            return restTemplate.getForObject(url, AuditionPost.class);
        } catch (final HttpClientErrorException e) {
            throw new SystemException(format("Cannot find a Post with id %1$s", id),
                e.getMessage(), e.getStatusCode().value(), e);
        }
    }

    public List<AuditionComment> getCommentByPostId(final String postId) {
        final String url = format("%1$s/posts/%2$s/comments", BASE_API_PATH, postId);
        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, format(LOG_REQUEST_FORMAT, url));
        }

        try {
            final AuditionComment[] auditionCommentsArr = restTemplate.getForObject(url, AuditionComment[].class);
            return auditionCommentsArr != null ? Arrays.asList(auditionCommentsArr) : new ArrayList<>();
        } catch (final HttpClientErrorException e) {
            throw new SystemException(format("Cannot find a Comments by postId %1$s", postId),
                e.getMessage(), e.getStatusCode().value(), e);
        }
    }

    public List<AuditionComment> getCommentByPostIdQueryParam(final String postId) {
        final String url = format("%1$s/comments?postId=%2$s", BASE_API_PATH, postId);
        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, format(LOG_REQUEST_FORMAT, url));
        }

        try {
            final AuditionComment[] auditionCommentsArr = restTemplate.getForObject(url, AuditionComment[].class);
            return auditionCommentsArr != null ? Arrays.asList(auditionCommentsArr) : new ArrayList<>();
        } catch (final HttpClientErrorException e) {
            throw new SystemException(format("Cannot find a Comments by postId %1$s query param ", postId),
                e.getMessage(), e.getStatusCode().value(), e);
        }
    }
}
