package com.audition.web;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class AuditionControllerTest {

    @Mock
    private AuditionService auditionService;

    @InjectMocks
    private AuditionController auditionController;


    @Test
    void testGetPostByUserAndKeywordShouldReturnAuditionPosts() {
        // Given
        final String userId = "1";
        final String keyword = "apple";
        final List<AuditionPost> auditionPosts = new ArrayList<>();
        final AuditionPost auditionPost1 = buildAuditionPost(1, "monkey ate the apple", "Title",
            Integer.parseInt(userId));
        final AuditionPost auditionPost2 = buildAuditionPost(2, "monkey ate the banana", "Title",
            Integer.parseInt(userId));
        auditionPosts.add(auditionPost1);
        auditionPosts.add(auditionPost2);

        // When
        final Logger log = mock(Logger.class);
        when(auditionController.getAuditionService().getPosts()).thenReturn(auditionPosts);
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionController> clazz = AuditionController.class;
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final List<AuditionPost> actual = auditionController.getPosts(userId, keyword);

        // Verify
        assertEquals(1, actual.size());
        assertEquals(auditionPost1, actual.get(0));
    }

    @Test
    void testGetPostByIdShouldReturnAuditionPosts() {
        // Given
        final String postId = "1";
        final AuditionPost auditionPost = buildAuditionPost(Integer.parseInt(postId), "monkey ate the apple", "Title",
            1);

        // When
        when(auditionController.getAuditionService().getPostById(postId)).thenReturn(auditionPost);
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionController> clazz = AuditionController.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final AuditionPost actual = auditionController.getPosts(postId);

        // Verify
        assertEquals(auditionPost, actual);
    }

    @Test
    void testGetPostByIdWhenInvalidPostIdShouldThrowSystemException() {
        // Given
        final String postId = "aaa";

        // When
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionController> clazz = AuditionController.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }

        // Verify
        final String format = format("Invalid post id. Should be a number %1$s.", postId);
        final SystemException exception = assertThrows(
            SystemException.class,
            () -> auditionController.getPosts(postId),
            format
        );

        assertTrue(exception.getMessage().contains(format));
    }

    @Test
    void testGetCommentsByPostIdShouldReturnAuditionComments() {
        // Given
        final String postId = "1";
        final List<AuditionComment> auditionComments = new ArrayList<>();
        final AuditionComment auditionComment1 = buildAuditionComment(1, "body", "name", "email@test.com",
            Integer.parseInt(postId));
        final AuditionComment auditionComment2 = buildAuditionComment(2, "body2", "name2", "email@test.com",
            Integer.parseInt(postId));
        auditionComments.add(auditionComment1);
        auditionComments.add(auditionComment2);

        // When
        when(auditionService.getCommentsByPostId(postId)).thenReturn(auditionComments);
        try (MockedStatic<LoggerFactory> loggerStaticMock = mockStatic(LoggerFactory.class)) {
            final Class<AuditionController> clazz = AuditionController.class;
            final Logger log = LoggerFactory.getLogger(clazz);
            loggerStaticMock.when(() -> LoggerFactory.getLogger(clazz)).thenReturn(log);
        }
        // Execute
        final List<AuditionComment> actual = auditionController.getComments(postId);

        // Verify
        assertEquals(2, actual.size());
        assertEquals(auditionComment1, actual.get(0));
        assertEquals(auditionComment2, actual.get(1));
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
