package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuditionServiceTest {

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private AuditionService auditionService;


    @Test
    void testGetPostShouldReturnAuditionPosts() {
        // Given
        final List<AuditionPost> auditionList = new ArrayList<>();
        final AuditionPost auditionPost1 = buildAuditionPost(1, "body", "Title", 1);
        auditionList.add(auditionPost1);
        final AuditionPost auditionPost2 = buildAuditionPost(2, "test body", "Test Title", 2);
        auditionList.add(auditionPost2);

        // When
        when(auditionIntegrationClient.getPosts()).thenReturn(auditionList);

        // Execute
        final List<AuditionPost> actual = auditionService.getPosts();

        // Verify
        assertEquals(2, actual.size());
        assertEquals(auditionPost1, actual.get(0));
        assertEquals(auditionPost2, actual.get(1));
    }

    @Test
    void testGetPostByIdShouldReturnAuditionPost() {
        // Given
        final String postId = "1";
        final AuditionPost auditionPost = buildAuditionPost(Integer.parseInt(postId), "body", "Title", 1);

        // When
        when(auditionService.getAuditionIntegrationClient().getPostById(postId)).thenReturn(auditionPost);

        // Execute
        final AuditionPost actual = auditionService.getPostById(postId);

        // Verify
        assertEquals(auditionPost, actual);
    }


    @Test
    void testGetCommentsByPostIdShouldReturnAuditionComments() {
        // Given
        final String postId = "1";
        final List<AuditionComment> auditionComments = new ArrayList<>();
        final AuditionComment auditionComment1 = buildAuditionComment(1, "body", "name", "email@test.com",
            Integer.parseInt(postId));
        final AuditionComment auditionComment2 = buildAuditionComment(2, "body2", "name2", "test@test.com",
            Integer.parseInt(postId));
        auditionComments.add(auditionComment1);
        auditionComments.add(auditionComment2);

        // When
        when(auditionService.getAuditionIntegrationClient().getCommentByPostId(postId)).thenReturn(auditionComments);

        // Execute
        final List<AuditionComment> actual = auditionService.getCommentsByPostId(postId);

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
