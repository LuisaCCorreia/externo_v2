package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.scb.externo.consts.Key;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.CobrancaService;
import com.scb.externo.shared.NovaCobrancaDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CobrancaServiceTests {

/*    @Mock
    RestTemplate mockedRestTemplate; */

    @Mock
    DadosCartaoRepository mockedCartaoRepository;

    @InjectMocks
    CobrancaService cobrancaService;
/*
    @Test
    void realizar_cobranca() {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00 , "7b7476c7-60a7-46a3-b7fe-45d28eb18e99");

       verify(mockedCartaoRepository).save(any(), times(1));

        ResponseEntity<DadosCobranca> respostaRecebida = cobrancaService.realizarCobranca(headers, novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());       

    }
    */
}
