package com.example.core.price.service;

import com.example.core.common.exception.price.NotFoundCitiesException;
import com.example.core.common.exception.price.NotFoundPriceException;
import com.example.core.common.exception.price.NotFoundSectorsException;
import com.example.core.common.parser.DataParser;
import com.example.core.price.domain.Price;
import com.example.core.price.formatter.OutPutFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Spy
    private DataParser dataParser;

    @Spy
    private OutPutFormatter outPutFormatter;

    @InjectMocks
    private PriceService priceService;

    private List<Price> mockPrices;

    @BeforeEach
    void setUp() {
        mockPrices = List.of(
                new Price(1L, "동두천시" , "가정용" ,1L, 1L, 20L, 690L, null),
                new Price(2L, "동두천시" , "가정용" ,2L, 21L, 30L, 1090L, null),
                new Price(3L, "파주시" , "가정용" ,1L, 31L, 999999L, 1530L, null),
                new Price(4L, "동두천시" , "일반용" ,4L, 1001L, 999999L, 1700L, null)
        );

        when(dataParser.prices()).thenReturn(mockPrices);

        priceService = new PriceService(dataParser, outPutFormatter);
    }

    @Test
    void cities_success() {
        List<String> cities = priceService.cities();

        assertEquals(2, cities.size());
        assertTrue(cities.contains("동두천시"));
        assertTrue(cities.contains("파주시"));
    }

    @Test
    void cities_fail() {
        when(dataParser.prices()).thenReturn(List.of());
        priceService = new PriceService(dataParser, outPutFormatter);

        assertThrows(NotFoundCitiesException.class,
                () -> priceService.cities());
    }

    @Test
    void sectors_success() {
        List<String> sectors = priceService.sectors("동두천시");

        assertEquals(2, sectors.size());
        assertTrue(sectors.contains("가정용"));
        assertTrue(sectors.contains("일반용"));
    }

    @Test
    void sectors_fail() {
        assertThrows(NotFoundSectorsException.class,
                () -> priceService.sectors("Daegu"));
    }

    @Test
    void price_success() {
        Price price = priceService.price("동두천시", "가정용");

        assertNotNull(price);
        assertEquals(690, price.getIntervalPrice());
    }

    @Test
    void price_fail() {
        assertThrows(NotFoundPriceException.class,
                () -> priceService.price("Seoul", "Unknown"));
    }

    @Test
    void billTotal_success() {
        when(outPutFormatter.format(any(), eq(50)))
                .thenReturn("1000원");

        String result = priceService.billTotal("Seoul", "Food", 50);

        assertEquals("1000원", result);
        verify(outPutFormatter, times(1)).format(any(), eq(50));
    }

    @Test
    void billTotal_no_price() {
        when(outPutFormatter.format(isNull(), eq(999)))
                .thenReturn("없음");

        String result = priceService.billTotal("Seoul", "Food", 999);

        assertEquals("없음", result);
        verify(outPutFormatter, times(1)).format(isNull(), eq(999));
    }
}