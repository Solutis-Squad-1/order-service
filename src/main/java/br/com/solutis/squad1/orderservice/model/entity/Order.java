package br.com.solutis.squad1.orderservice.model.entity;

import br.com.solutis.squad1.orderservice.model.entity.enums.Payment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    @Column(nullable = false)
    private Instant moment;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_id", nullable = false)
    private Payment paymentId;

    @Column(nullable = false)
    private String summary;

    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private List<Product> products;

    @Column(nullable = false)
    private boolean canceled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private LocalDateTime canceledAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        moment = Instant.now();
    }

    public void delete(Order order){
        canceled = true;
        canceledAt = LocalDateTime.now();
    }
}
