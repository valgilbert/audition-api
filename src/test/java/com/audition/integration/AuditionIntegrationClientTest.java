package com.audition.integration;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AuditionIntegrationClientTest {

    public static final String EXPECTED_GET_POSTS_TO_THROW_EXCEPTION_BUT_IT_DIDN_T = "Expected getPosts() to throw exception, but it didn't";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;


    @Test
    void testGetPostShouldReturnAuditionPosts() {
        // Given
        final AuditionPost[] auditionPostArr = new AuditionPost[2];
        final AuditionPost auditionPost1 = buildAuditionPost(1, "body 1", "Title", 1);
        auditionPostArr[0] = auditionPost1;
        final AuditionPost auditionPost2 = buildAuditionPost(2, "test body", "Test Title", 2);
        auditionPostArr[1] = auditionPost2;

        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionIntegrationClient> clazz = AuditionIntegrationClient.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenReturn(auditionPostArr);

        // Execute
        final List<AuditionPost> actual = auditionIntegrationClient.getPosts();

        // Verify
        assertEquals(2, actual.size());
        assertEquals(auditionPost1.getBody(), actual.get(0).getBody());
        assertEquals(auditionPost1.getId(), actual.get(0).getId());
        assertEquals(auditionPost2.getBody(), actual.get(1).getBody());
        assertEquals(auditionPost2.getId(), actual.get(1).getId());
        verify(restTemplate, times(1)).getForObject(anyString(), any());
    }

    @Test
    void testGetPostShouldHandleNotFoundException() {
        // Given
        final HttpClientErrorException ex = new HttpClientErrorException(NOT_FOUND);
        // When
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenThrow(ex);
        // Execute
        final SystemException thrown = assertThrows(
            SystemException.class,
            () -> auditionIntegrationClient.getPosts(),
            EXPECTED_GET_POSTS_TO_THROW_EXCEPTION_BUT_IT_DIDN_T
        );
        // Verify
        assertEquals("Cannot find a Posts, Resource Not Found", thrown.getMessage());
    }


    @Test
    void testGetPostByIdShouldReturnAuditionPost() {
        // Given
        final String id = "1";
        final AuditionPost auditionPost = buildAuditionPost(Integer.parseInt(id), "body", "Title", 1);

        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionIntegrationClient> clazz = AuditionIntegrationClient.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenReturn(auditionPost);

        // Execute
        final AuditionPost actual = auditionIntegrationClient.getPostById(id);

        // Verify
        assertEquals(auditionPost.getBody(), actual.getBody());
        assertEquals(auditionPost.getTitle(), actual.getTitle());
        assertEquals(auditionPost.getId(), actual.getId());
        assertEquals(auditionPost.getUserId(), actual.getUserId());
    }


    @Test
    void testGetPostByIdShouldHandleNotFoundException() {
        // Given
        final String id = "1";
        final HttpClientErrorException ex = new HttpClientErrorException(NOT_FOUND);
        // When
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenThrow(ex);
        // Execute
        final SystemException thrown = assertThrows(
            SystemException.class,
            () -> auditionIntegrationClient.getPostById(id),
            EXPECTED_GET_POSTS_TO_THROW_EXCEPTION_BUT_IT_DIDN_T
        );
        // Verify
        assertEquals("Cannot find a Post with id " + id, thrown.getMessage());
    }


    @Test
    void testGetCommentByPostIdShouldReturnAuditionComment() {
        // Given
        final String postId = "1";
        final AuditionComment[] auditionCommentArr = new AuditionComment[2];
        final AuditionComment auditionComment1 = buildAuditionComment(1, "body a", "name", "email@test.com",
            Integer.parseInt(postId));
        final AuditionComment auditionComment2 = buildAuditionComment(2, "body2", "name2", "test@test.com",
            Integer.parseInt(postId));
        auditionCommentArr[0] = auditionComment1;
        auditionCommentArr[1] = auditionComment2;

        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionIntegrationClient> clazz = AuditionIntegrationClient.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenReturn(
            auditionCommentArr);

        // Execute
        final List<AuditionComment> actual = auditionIntegrationClient.getCommentByPostId(postId);

        // Verify
        assertEquals(2, actual.size());
        assertEquals(auditionComment1.getId(), actual.get(0).getId());
        assertEquals(auditionComment1.getBody(), actual.get(0).getBody());
        assertEquals(auditionComment1.getName(), actual.get(0).getName());
        assertEquals(auditionComment2.getId(), actual.get(1).getId());
        assertEquals(auditionComment2.getBody(), actual.get(1).getBody());
        assertEquals(auditionComment2.getName(), actual.get(1).getName());
    }


    @Test
    void testGetCommentByPostIdShouldHandleNotFoundException() {
        // Given
        final String postId = "1";
        final HttpClientErrorException ex = new HttpClientErrorException(NOT_FOUND);
        // When
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenThrow(ex);
        // Execute
        final SystemException thrown = assertThrows(
            SystemException.class,
            () -> auditionIntegrationClient.getCommentByPostId(postId),
            "Expected getCommentByPostId(id) to throw exception, but it didn't"
        );
        // Verify
        assertEquals(format("Cannot find a Comments by postId %1$s", postId), thrown.getMessage());
    }


    @Test
    void testGetCommentByPostIdQueryParamShouldReturnAuditionComment() {
        // Given
        final String postId = "1";
        final AuditionComment[] auditionCommentArr = new AuditionComment[2];
        final AuditionComment auditionComment1 = buildAuditionComment(1, "body r", "name", "email@test.com",
            Integer.parseInt(postId));
        final AuditionComment auditionComment2 = buildAuditionComment(2, "body2", "name2", "test@test.com",
            Integer.parseInt(postId));
        auditionCommentArr[0] = auditionComment1;
        auditionCommentArr[1] = auditionComment2;

        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionIntegrationClient> clazz = AuditionIntegrationClient.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenReturn(
            auditionCommentArr);

        // Execute
        final List<AuditionComment> actual = auditionIntegrationClient.getCommentByPostIdQueryParam(postId);

        // Verify
        assertEquals(2, actual.size());
        assertEquals(auditionComment1.getId(), actual.get(0).getId());
        assertEquals(auditionComment1.getBody(), actual.get(0).getBody());
        assertEquals(auditionComment1.getName(), actual.get(0).getName());
        assertEquals(auditionComment2.getId(), actual.get(1).getId());
        assertEquals(auditionComment2.getBody(), actual.get(1).getBody());
        assertEquals(auditionComment2.getName(), actual.get(1).getName());
    }

    @Test
    void testGetCommentByPostIdQueryParamShouldHandleNotFoundException() {
        // Given
        final String postId = "1";
        final HttpClientErrorException ex = new HttpClientErrorException(NOT_FOUND);
        // When
        when(auditionIntegrationClient.getRestTemplate().getForObject(anyString(), any())).thenThrow(ex);
        // Execute
        final SystemException thrown = assertThrows(
            SystemException.class,
            () -> auditionIntegrationClient.getCommentByPostIdQueryParam(postId),
            "Expected getCommentByPostId(id) to throw exception, but it didn't"
        );
        // Verify
        assertEquals(format("Cannot find a Comments by postId %1$s query param ", postId), thrown.getMessage());
    }


    private static AuditionPost buildAuditionPost(final int id, final String body, final String title,
        final int userId) {
        final AuditionPost auditionPost = new AuditionPost();
        auditionPost.setBody(body);
        auditionPost.setId(id);
        auditionPost.setTitle(title);
        auditionPost.setUserId(userId);
        return auditionPost;
    }

    private static AuditionComment buildAuditionComment(final int id, final String body, final String name,
        final String email, final int postId) {
        final AuditionComment auditionComment = new AuditionComment();
        auditionComment.setId(id);
        auditionComment.setBody(body);
        auditionComment.setName(name);
        auditionComment.setEmail(email);
        auditionComment.setPostId(postId);
        return auditionComment;
    }
}
