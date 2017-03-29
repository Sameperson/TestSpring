package com.opinta.mapper;

import com.opinta.dto.CounterpartyDto;
import com.opinta.entity.Counterparty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CounterpartyMapper extends BaseMapper<CounterpartyDto, Counterparty> {
    
    @Override
    @Mappings({
            @Mapping(source = "postcodePoolId", target = "postcodePool.id")})
    Counterparty toEntity(CounterpartyDto dto);
    
    @Override
    @Mappings({
            @Mapping(source = "postcodePool.id", target = "postcodePoolId"),
            @Mapping(source = "user.token", target = "token")})
    CounterpartyDto toDto(Counterparty entity);
}
