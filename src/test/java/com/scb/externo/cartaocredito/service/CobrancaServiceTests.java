package com.scb.externo.cartaocredito.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.CobrancaService;
import com.scb.externo.shared.NovaCobrancaDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CobrancaServiceTests {

/*    @Mock
    RestTemplate mockedRestTemplate; */

    @Mock
    DadosCartaoRepository mockedCartaoRepository;

    @Mock
    CobrancaRepository mockedCobrancaRespository;

    @InjectMocks
    CobrancaService cobrancaService;

    @Test
    void realizar_cobranca() {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00 , "7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        DadosToken dadosToken = new DadosToken(novaCobranca.getCiclista(), "cus_000005077278", "4ae91611-5a92-42bf-ad17-a45124c11b19");

        when(mockedCartaoRepository.findByCiclista(novaCobranca.getCiclista())).thenReturn(dadosToken);
        when(mockedCobrancaRespository.save(any())).thenReturn(any());

        ResponseEntity<DadosCobranca> respostaRecebida = cobrancaService.realizarCobranca(headers, novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());       

    }
    
}
