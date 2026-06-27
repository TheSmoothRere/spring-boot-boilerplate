package io.github.thesmoothrere.boilerplate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringBootBoilerplateApplicationTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoad() {
        assertThat(context).isNotNull();

        assertThat(context.containsBean("springBootBoilerplateApplication")).isTrue();
    }
}