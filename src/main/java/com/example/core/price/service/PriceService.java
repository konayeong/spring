package com.example.core.price.service;

import com.example.core.common.exception.price.NotFoundCitiesException;
import com.example.core.common.exception.price.NotFoundPriceException;
import com.example.core.common.exception.price.NotFoundSectorsException;
import com.example.core.common.parser.DataParser;
import com.example.core.price.domain.Price;
import com.example.core.price.formatter.OutPutFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class PriceService {

    private final DataParser dataParser;
    private final OutPutFormatter outPutFormatter;
    private final Map<Long, Price> priceMap = new HashMap<>();

    public PriceService(DataParser dataParser, OutPutFormatter outPutFormatter) {
        this.dataParser = dataParser;
        this.outPutFormatter = outPutFormatter;
        loadPrices();
    }

    // 사용 전 map에 저장
    public void loadPrices() {
        List<Price> prices = dataParser.prices();
        for(Price price : prices) {
            priceMap.put(price.getId(), price);
        }

        log.info("Price Save Count : {}", priceMap.size());
    }

    public List<String> cities() {
        List<String> cities = priceMap.values().stream()
                .map(Price::getCity)
                .distinct()
                .toList();

        if(cities.isEmpty()) {
            log.debug("지자체 존재하지 않음");
            throw new NotFoundCitiesException();
        }

        return cities;
    }

    public List<String> sectors(String city) {
        List<String> sectors = priceMap.values().stream()
                .filter(price -> price.getCity().equals(city))
                .map(Price::getSector)
                .distinct()
                .toList();

        if(sectors.isEmpty()) {
            log.debug("업종 목록 존재하지 않음");
            throw new NotFoundSectorsException(city);
        }

        return sectors;
    }

    public Price price(String city, String sector) {
        Optional<Price> price = priceMap.values().stream()
                .filter(p -> p.getCity().equals(city) && p.getSector().equals(sector))
                .findFirst();

        if(price.isEmpty()) {
            log.debug("구간금액을 찾을 수 없음");
            throw new NotFoundPriceException(city, sector);
        }

        return price.get();
    }

    public String billTotal(String city, String sector, int use) {
        Optional<Price> opPrice = priceMap.values().stream()
                .filter(p -> p.getCity().equals(city) && p.getSector().equals(sector))
                .filter(p -> use >= p.getIntervalStart() && use <= p.getIntervalEnd())
                .findFirst();

        if(opPrice.isEmpty()) {
            log.debug("bill-total : 정보 없음, city:{}, sector{}, interval:{}", city, sector, use);
        }

        // opPrice.get() :  null일 경우 오류 발생
        return outPutFormatter.format(opPrice.orElse(null), use);
    }

}
