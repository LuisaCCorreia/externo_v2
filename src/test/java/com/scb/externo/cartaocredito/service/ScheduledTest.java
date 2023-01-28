package com.scb.externo.cartaocredito.service;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.scb.externo.service.cartaocredito.CobrancaService;

@SpringBootTest
class ScheduledTest {

    @SpyBean
    CobrancaService cobrancaService;

    @Test
    void processa_cobranca_fila_awaitility() {
        Awaitility.await().atMost(12, TimeUnit.HOURS)
        .untilAsserted(()->verify(cobrancaService, atLeast(1)).processaCobrancasEmFila());

    }
}
