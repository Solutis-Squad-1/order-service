package br.com.solutis.squad1.orderservice.mapper;

import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "id", ignore = true)
    Order postDtoToEntity(OrderPostDto orderPostDto);

    Order putDtoToEntity(OrderPutDto orderPutDto);
}
