package com.practice.springframework.web.context;

import com.shop.springframework.config.DataAccessConfig;
import com.shop.springframework.config.DataSourceConfig;
import com.shop.springframework.config.WebMvcConfig;
import com.shop.springframework.domain.products.Product;
import com.shop.springframework.domain.products.ProductUtil;
import com.shop.springframework.web.MainController;
import com.shop.springframework.web.ProductController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        WebMvcConfig.class,
        DataAccessConfig.class,
        DataSourceConfig.class
})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MvcIntegrationTest {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    @Sql("classpath:dropTables.sql")
    public void tearDown() {

    }

    @Test
    public void testShowIndexPage() throws Exception {
        mockMvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(view().name(MainController.INDEX_VIEW))
                .andExpect(model().attribute("cart", notNullValue()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @Sql("classpath:testData.sql")
    public void testShowProductsByType() throws Exception {
        mockMvc.perform(get("/products?type={type}", "laptop"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.PRODUCT_LIST_VIEW))
                .andExpect(model().attribute("cart", notNullValue()))
                .andExpect(model().attribute("products", hasSize(1)))
                .andExpect(model().attribute("products", hasItem(
                        allOf(
                                hasProperty("id", is(100L)),
                                hasProperty("type", is(Product.Type.LAPTOP)),
                                hasProperty("description", is("laptop description")),
                                hasProperty("imgUrl", is("laptop.jpg"))
                        )
                )));
    }
}
