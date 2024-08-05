package com.audition.web;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditionController {

    public static final Pattern NUMERIC_PATTERN = Pattern.compile(".*\\d.*");
    private static final Logger LOG = LoggerFactory.getLogger(AuditionController.class);

    @Autowired
    private AuditionService auditionService;

    @Autowired
    private AuditionLogger logger;


    public AuditionService getAuditionService() {
        return this.auditionService;
    }

    public void setAuditionService(final AuditionService auditionService) {
        this.auditionService = auditionService;
    }

    public AuditionLogger getLogger() {
        return this.logger;
    }


    public void setLogger(final AuditionLogger logger) {
        this.logger = logger;
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam("userId") final String userId,
        @RequestParam("keyword") final String keyword) {

        if (LOG.isDebugEnabled()) {
            logger.debug(LOG,
                format("Filtering Posts by userId %1$s and keyword %2$s query params", userId, keyword));
        }

        return auditionService.getPosts().stream()
            .filter(p -> parseInt(userId) == p.getUserId() &&
                (p.getTitle().contains(keyword) || p.getBody().contains(keyword)))
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPosts(@PathVariable("id") final String postId) {

        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, format("Retrieving Post by id %1$s.", postId));
        }

        if (postId != null && !NUMERIC_PATTERN.matcher(postId).matches()) {
            if (LOG.isDebugEnabled()) {
                logger.debug(LOG, format("Invalid post id. Should be a number %1$s.", postId));
            }
            throw new SystemException(format("Invalid post id. Should be a number %1$s.", postId));
        }

        return auditionService.getPostById(postId);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionComment> getComments(@RequestParam("postId") final String postId) {
        if (LOG.isDebugEnabled()) {
            logger.debug(LOG, format("Retrieving Comments by postId %1$s.", postId));
        }

        return auditionService.getCommentsByPostId(postId).stream().filter(c -> parseInt(postId) == c.getPostId())
            .collect(Collectors.toList());
    }

}
