package br.com.solutis.squad1.orderservice.mapper;

import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * Mapper interface for OderItem.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    OrderItem postDtoToEntity(OrderItemPostDto orderItemPostDto);

    @Mapping(target = "id", ignore = true)
    Set<OrderItem> postDtoToEntity(List<OrderItemPostDto> orderItemPostDto);

    List<OrderItemResponseDto> toResponseDto(List<OrderItem> orderItems);
}
