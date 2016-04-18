package com.practice.springframework.test.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.shop.springframework.config.DataAccessConfig;
import com.shop.springframework.config.DataSourceConfig;
import com.shop.springframework.config.WebMvcConfig;
import com.shop.springframework.domain.products.ProductUtil;
import com.shop.springframework.repositories.ProductRepository;
import com.shop.springframework.domain.products.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
        DataAccessConfig.class,
        DataSourceConfig.class,
        WebMvcConfig.class
})
@TestExecutionListeners(value = {
        DbUnitTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
        ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        SqlScriptsTestExecutionListener.class
})
@ActiveProfiles("test")
@DbUnitConfiguration(databaseConnection = {"testDataSource"})
@TestPropertySource("classpath:test.properties")
@Transactional
@Sql("classpath:testData.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // default
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected int countRowsInTable(String tableName) {
        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
    }

    protected void assertNumProducts(int expected) {
        assertEquals("Number of rows in the [Product] table.", expected, countRowsInTable("Product"));
    }

    @Test
    public void findByNameLike_TitleOfFirstProductEntryContainsGivenNameTerm_ShouldReturnOneProductEntry() {
        assertNumProducts(2);
        List<Product> results = (List<Product>) repository.findByNameLike("laptop%");
        assertThat(results).hasSize(1);
    }

    @Test
    public void saveProduct() {
        Product product = new Product();
        product.setType(Product.Type.ACCESSORIES);
        product.setInventory(2);
        product.setDescription("a mere description");
        product.setImgUrl("demo.jpg");
        product.setName("accessoriesDefault");
        Map<String,String> chars = new HashMap<>();
        for (String ch : ProductUtil.ACCESSORIES_REQUIRED_PROPERTIES)
            chars.put(ch, "demo");
        product.setTechnicalCharacteristics(chars);
        product.setPrice(new BigDecimal(25));
        Product saved = repository.save(product);
        assertThat(saved.getId()).isNotNull();
        List<Product> results = repository.findAll();
        assertThat(results).hasSize(3);
        assertThat(saved).isEqualTo(product);
    }

    @Test
    public void findByType_ShouldReturnOneProduct() {
        List<Product> results = (List<Product>) repository.findByType(Product.Type.LAPTOP);
        assertThat(results).hasSize(1);
    }

    @Test
    public void findByType_ShouldReturnZeroProducts() {
        List<Product> results = (List<Product>) repository.findByType(Product.Type.ACCESSORIES);
        assertThat(results).hasSize(0);
    }

    @Test
    public void updateProduct_ShouldUpdateProductCorrectly() {
        Product phone = repository.findByNameLike("phoneDefault").iterator().next();
        assertThat(phone).isNotNull();

        phone.setName("Nokia Lumia");
        phone = repository.save(phone);

        Product updated = repository.findByNameLike("Nokia%").iterator().next();
        assertThat(updated).isEqualTo(phone);
    }

    @Test
    public void findMany() {
        Product product = ((List<Product>) repository.findByNameLike("%")).get(0);
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(100L);
    }
}