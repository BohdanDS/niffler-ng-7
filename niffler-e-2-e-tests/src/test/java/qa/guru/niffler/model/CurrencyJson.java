package qa.guru.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyJson(
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("currencyRate")
    Double currencyRate) {

}
