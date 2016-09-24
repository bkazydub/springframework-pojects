package com.practice.shop.web.standalone;

import com.practice.shop.utils.TestUtils;
import com.practice.shop.domain.ShoppingCart;
import com.practice.shop.domain.products.Product;
import com.practice.shop.domain.products.ProductUtil;
import com.practice.shop.repositories.ProductRepository;
import com.practice.shop.web.MainController;
import com.practice.shop.web.ProductController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class MainControllerTest {

    @Mock
    ProductRepository productRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(
                new MainController(productRepository)).build();
    }

    @Test
    public void testShowIndexPage() throws Exception {
        mockMvc.perform(get("").sessionAttr("cart", new ShoppingCart()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name(MainController.INDEX_VIEW));
    }

    @Test
    public void testShowProductsByType_MalformedType_ShowsAllProducts() throws Exception {
        String type = TestUtils.generateString(8);
        when(productRepository.findAll()).thenReturn(sampleProductList());

        mockMvc.perform(get("/products").param("type", type))
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.PRODUCT_LIST_VIEW))
                .andExpect(model().attribute("products", hasSize(3)));

        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testShowProductsByType_LaptopType_ShowsOnlyLaptops() throws Exception {
        when(productRepository.findByType(Product.Type.LAPTOP)).thenReturn(sampleLaptopList());

        mockMvc.perform(get("/products").param("type", "laptop"))
                .andExpect(status().isOk())
                .andExpect(view().name(ProductController.PRODUCT_LIST_VIEW))
                .andExpect(model().attribute("products", hasSize(1)))
                .andExpect(model().attribute("products", hasItem(hasProperty("name", is("laptop")))));

        verify(productRepository).findByType(Product.Type.LAPTOP);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void testShowCart() throws Exception {
        mockMvc.perform(get("/cart").sessionAttr("cart", new ShoppingCart()))
                .andExpect(status().isOk())
                .andExpect(view().name(MainController.CART_VIEW))
                .andExpect(model().attributeExists("cart"));

        verifyZeroInteractions(productRepository);
    }

    private List<Product> sampleProductList() {
        Product p1 = new Product(Product.Type.LAPTOP, "laptop", "laptop description",
                "laptop.jpg", new BigDecimal(360.90), 1);
        p1.setTechnicalCharacteristics(ProductUtil.createCharacteristicsMap(ProductUtil.LAPTOP_REQUIRED_PROPERTIES));

        Product p2 = new Product(Product.Type.LAPTOP, "laptop2", "laptop2 description",
                "laptop2.jpg", new BigDecimal(480), 3);
        p2.setTechnicalCharacteristics(ProductUtil.createCharacteristicsMap(ProductUtil.LAPTOP_REQUIRED_PROPERTIES));

        Product p3 = new Product(Product.Type.PHONE, "phone", "phone description",
                "phone.jpg", new BigDecimal(210.12), 10);
        p3.setTechnicalCharacteristics(ProductUtil.createCharacteristicsMap(ProductUtil.MOBILE_REQUIRED_PROPERTIES));

        return Arrays.asList(p1, p2, p3);
    }

    private List<Product> sampleLaptopList() {
        Product p1 = new Product(Product.Type.LAPTOP, "laptop", "laptop description",
                "laptop.jpg", new BigDecimal(25), 12);
        p1.setTechnicalCharacteristics(ProductUtil.createCharacteristicsMap(ProductUtil.LAPTOP_REQUIRED_PROPERTIES));

        return Collections.singletonList(p1);
    }
}
