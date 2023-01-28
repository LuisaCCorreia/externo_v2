package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.service.cartaocredito.CobrancaService;
import com.scb.externo.shared.NovaCobrancaDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CobrancaServiceTests {

    @Autowired
    CobrancaService cobrancaService;

    @Autowired
    CobrancaRepository cobrancaRepository;

    //Testes realizar cobrança
    
    @Test
    void realizar_cobranca_valida() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor((float)10.00);

        ResponseEntity<DadosCobranca> respostaRecebida = cobrancaService.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());       
    }

    //Testes resgatar cobrança
    @Test
    void resgatar_cobranca_por_id() throws JSONException, IOException, InterruptedException {

        ResponseEntity<String> respostaRecebida = cobrancaService.resgatarCobranca( "cus_000005095826");

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    //Teste colocar cobrança na fila
    @Test
    void colocar_cobranca_na_fila() {

        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor((float)10.00);

        ResponseEntity<DadosCobranca> resposta = cobrancaService.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
    
}
