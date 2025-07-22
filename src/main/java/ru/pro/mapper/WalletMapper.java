package ru.pro.mapper;

import org.mapstruct.Mapper;
import ru.pro.model.dto.WalletDto;
import ru.pro.model.entity.Wallet;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletDto walletToWalletDto(Wallet wallet);
}
