package com.practice.springframework.test.config;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.shop.springframework.config.DataAccessConfig;
import com.shop.springframework.config.DataSourceConfig;
import com.shop.springframework.config.WebMvcConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DataAccessConfig.class,
        DataSourceConfig.class,
        WebMvcConfig.class
})
@TestExecutionListeners(
        listeners = {
                DbUnitTestExecutionListener.class,
                TransactionDbUnitTestExecutionListener.class
        },
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@WebAppConfiguration
@TestPropertySource("classpath:test.properties")
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
}
