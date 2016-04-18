package com.shop.springframework.domain.products;

import com.shop.springframework.domain.BaseEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Entity
public class Product extends BaseEntity {

    public enum Type {
        LAPTOP("laptop"), PHONE("phone"), ACCESSORIES("accessories");

        private final String label;

        Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public Product() {
    }

    public Product(Type type, String name, String description, String imgUrl, BigDecimal price, Integer inventory) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.price = price;
        this.inventory = inventory;
    }

    public Product(Type type, String name, String description,
                   Map<String, String> technicalCharacteristics,
                   String imgUrl, BigDecimal price, Integer inventory) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.technicalCharacteristics = technicalCharacteristics;
        this.imgUrl = imgUrl;
        this.price = price;
        this.inventory = inventory;
    }

    @NotNull
    @Enumerated(STRING)
    private Type type;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false, length = 2500)
    private String description;

    @NotEmpty
    @ElementCollection(fetch = EAGER)
    @MapKeyColumn(name = "technical_property") // to be renamed
    @Column(name = "properties")
    @CollectionTable(name = "product_technical_characteristics")
    private Map<String,String> technicalCharacteristics;

    @Column(name = "img_url")
    private String imgUrl = "demo.jpg";

    @Digits(fraction = 2, integer = 5, message = "o'rly?")
    @Column(nullable = false)
    private BigDecimal price;

    @Column
    @Digits(fraction = 0, integer = 5)
    private Integer inventory;

    @Transient
    public boolean isAvailable() {
        return inventory > 0;
    }

    @PrePersist
    public void init() {
    }

    public void put(String key, String value) {
        technicalCharacteristics.put(key, value);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getTechnicalCharacteristics() {
        return technicalCharacteristics;
    }

    public void setTechnicalCharacteristics(Map<String, String> technicalCharacteristics) {
        this.technicalCharacteristics = technicalCharacteristics;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (String c : technicalCharacteristics.values()) {
            sb.append(c);
            sb.append(" / ");
        }
        return sb.toString();
    }
}