package com.scb.externo.cartaocredito.service;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.service.cartaocredito.CobrancaService;

@SpringBootTest
public class ScheduledTest {

    @Autowired
    CobrancaRepository cobrancaRepository;

    @SpyBean
    CobrancaService cobrancaService;
    
    //Teste processa cobranças na fila
  /*  @Test
    void processa_colocar_cobranca_na_fila() throws InterruptedException {
    
        Thread.sleep(180000);
        List<DadosCobranca> cobrancasPendentes = cobrancaRepository.findByStatus(CobrancaStatus.PENDENTE.getStatus());
        assertEquals(0, cobrancasPendentes.size());
    }*/

    @Test
    void processa_cobranca_fila_awaitility() {
        Awaitility.await().atMost(5, TimeUnit.MINUTES)
        .untilAsserted(()->verify(cobrancaService, atLeast(1)).processaCobrancasEmFila());

    }
}
