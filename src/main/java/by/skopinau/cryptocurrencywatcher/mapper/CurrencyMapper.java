package by.skopinau.cryptocurrencywatcher.mapper;

import by.skopinau.cryptocurrencywatcher.dal.entity.Currency;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyRequest;
import by.skopinau.cryptocurrencywatcher.dto.CurrencyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Object mapper for {@link CurrencyRequest} and
 * {@link CurrencyResponse} dto.
 */
@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    /**
     * Maps {@link CurrencyRequest} dto to
     * {@link Currency} entity.
     * @param currencyRequest is used to define dto to map
     * @return {@link Currency}
     */
    Currency currencyRequestToCurrencyEntity(CurrencyRequest currencyRequest);

    /**
     * Maps {@link Currency} entity to
     * {@link CurrencyResponse} dto.
     * @param currency is used to define entity to map
     * @return {@link CurrencyResponse}
     */
    CurrencyResponse currencyEntityToCurrencyResponse(Currency currency);
}
