package com.practice.shop.web.context;

import com.practice.shop.config.DataAccessConfig;
import com.practice.shop.config.DataSourceConfig;
import com.practice.shop.config.WebMvcConfig;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Configuration common for all integration tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        DataAccessConfig.class,
        WebMvcConfig.class
})
@TestPropertySource("classpath:test.properties")
@Transactional
@DirtiesContext
public abstract class AbstractIntegrationTest {
}
