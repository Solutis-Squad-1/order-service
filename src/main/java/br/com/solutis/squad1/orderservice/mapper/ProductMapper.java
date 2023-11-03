package br.com.solutis.squad1.orderservice.mapper;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.product.ImageResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductPostDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toResponseDto(Product product);

    Product postDtoToEntity(ProductPostDto productPostDto);

    Image imagePostDtoToEntity(ImageResponseDto imageResponseDto);
}
