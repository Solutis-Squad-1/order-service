package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderProductDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDetailsDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductPostDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDetailsDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderProduct;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderProductService {
    private final OrderService orderService;
    private final ProductService productService;
    private final OrderProductRepository orderProductRepository;
    private final OrderProductRepositoryCustom orderProductRepositoryCustom;

    public OrderResponseDetailsDto save(OrderProductDto orderProductDto) {
        try {
            Order order = orderService.save(new Order(orderProductDto));
            Product product = productService.saveProduct(new Product(orderProductDto.product(), orderProductDto.product().getCategories(), orderProductDto.product().getImage()));

            orderProductRepository.save(new OrderProduct(order, product, orderProductDto.quantity(), orderProductDto.totalPrice()));

            OrderResponseDetailsDto orderResponseDetailsDto = findProductsQuantitiesAndPriceByOrder(order);

            return orderResponseDetailsDto;
        } catch (Exception e) {
            throw e;
        }
    }

    //Procurar a lista de produtos, quantidades e preços pelo id de um pedido
    public OrderResponseDetailsDto findProductsQuantitiesAndPriceByOrder(Order order) {
        Map<Long, Integer> quantities = findQuantitiesByOrderId(order.getId());
        Map<Long, BigDecimal> prices = findPricesByOrderId(order.getId());
        List<Product> products = findProductsByOrderId(order.getId());
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<ProductResponseDetailsDto> productResponseDetailsDtos = new ArrayList<>();

        for (Product product : products) {
            Long id = product.getId();

            if (quantities.containsKey(id) && prices.containsKey(id)) {
                ProductResponseDetailsDto productDto = new ProductResponseDetailsDto(product, quantities.get(id), prices.get(id));
                productResponseDetailsDtos.add(productDto);
                totalPrice = totalPrice.add(prices.get(id));
            }
        }
        return new OrderResponseDetailsDto(order, productResponseDetailsDtos, totalPrice);
    }

    public List<Product> findProductsByOrderId(Long id) {
        try {
            return orderProductRepositoryCustom.findProductsByOrderId(id);
        } catch (Exception e) {
            throw e;
        }
    }

    private Map<Long, Integer> findQuantitiesByOrderId(Long id) {
        try {
            return orderProductRepositoryCustom.findQuantitiesByOrderId(id);
        } catch (Exception e) {
            throw e;
        }
    }

    private Map<Long, BigDecimal> findPricesByOrderId(Long id) {
        try {
            return orderProductRepositoryCustom.findPricesByOrderId(id);
        } catch (Exception e) {
            throw e;
        }
    }
}
