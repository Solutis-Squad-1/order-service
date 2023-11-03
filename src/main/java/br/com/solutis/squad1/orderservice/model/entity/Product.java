package br.com.solutis.squad1.orderservice.model.entity;

import br.com.solutis.squad1.orderservice.dto.product.ProductPostDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long sellerId;

    @NotEmpty
    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<OrderProduct> items = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    public Product(Product product, List<Category> categories, Image image){
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.sellerId = product.getSellerId();
        this.categories = categories;
        this.image = image;
    }

    public Product(ProductPostDto product, List<Category> categories, Image image){
        this.id = product.id();
        this.name = product.name();
        this.description = product.description();
        this.price = product.price();
        this.sellerId = product.sellerId();
        this.categories = categories;
        this.image = image;
    }

}
