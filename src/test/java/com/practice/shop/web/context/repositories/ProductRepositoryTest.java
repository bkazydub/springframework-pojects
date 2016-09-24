package com.practice.shop.web.context.repositories;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.practice.shop.domain.products.Product;
import com.practice.shop.domain.products.ProductUtil;
import com.practice.shop.repositories.ProductRepository;
import com.practice.shop.web.context.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@TestExecutionListeners(value = {
        TransactionDbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class
})
public class ProductRepositoryTest extends AbstractIntegrationTest {

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
        List<Product> results = (List<Product>) repository.findByNameLikeIgnoreCase("%pavillion%");
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
        Product phone = repository.findByNameLikeIgnoreCase("%galaxy s III%").iterator().next();
        assertThat(phone).isNotNull();

        phone.setName("Samsung Galaxy S3");
        phone = repository.save(phone);

        Product updated = repository.findByNameLikeIgnoreCase("Samsung Galaxy S3%").iterator().next();
        assertThat(updated).isEqualTo(phone);
    }

    @Test
    public void findMany_ShouldReturn2Entries() {
        Collection<Product> products = repository.findByNameLikeIgnoreCase("%");
        assertThat(products.size()).isEqualTo(2);
        Product first = ((List<Product>) products).get(0);
        Product second = ((List<Product>) products).get(1);
        assertThat(first.getId()).isEqualTo(100L);
        assertThat(second.getId()).isEqualTo(101L);
    }
}
