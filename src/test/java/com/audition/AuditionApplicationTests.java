package com.audition;

import static org.assertj.core.api.Assertions.assertThat;

import com.audition.web.AuditionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditionApplicationTests {

    @Autowired
    private AuditionController auditionController;

    public AuditionController getAuditionController() {
        return this.auditionController;
    }

    public void setAuditionController(final AuditionController auditionController) {
        this.auditionController = auditionController;
    }

    @Test
    void contextLoads() {
        assertThat(getAuditionController()).isNotNull();
    }

}
