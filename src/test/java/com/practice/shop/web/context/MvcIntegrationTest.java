package com.practice.shop.web.context;

import com.practice.shop.domain.products.Product;
import com.practice.shop.web.MainController;
import com.practice.shop.web.ProductController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MvcIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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
    public void testShowProductsByType() throws Exception {
        mockMvc.perform(get("/products?type={type}", "laptop"))
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
