package com.scb.externo.controller.cartaocredito;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scb.externo.models.exceptions.ResourceInvalidCreditCardDataException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.NovaCobrancaDTO;
import com.scb.externo.shared.NovoCartaoDTO;

@RestController
@RequestMapping("/api/externo/cartao")
public class CartaoCreditoController {
    @Autowired
    CartaoCreditoService cartaoService;

    public boolean validarCVV(String cvv) {
        return cvv.matches("\\d{3,4}");
    }

    public boolean validarNumero(String numero) {
        return numero.matches("\\d+");
    }

    public boolean validarDataValidade(String data) {
        LocalDate dataAtual = LocalDate.now();

        try {
            LocalDate dataInserida = LocalDate.parse(data);
            return dataAtual.isBefore(dataInserida);
        } catch (Exception e) {
            return false;
        }       
    }
    
    @PostMapping("/validacaocartao")
    public ResponseEntity<APICartaoTokenResponse> autenticarCartao(@RequestHeader MultiValueMap<String, String> headers, @RequestBody NovoCartaoDTO novoCartao) {
       if(!validarCVV(novoCartao.getCvv()) || !validarNumero(novoCartao.getNumero()) || !validarDataValidade(novoCartao.getValidade())) {
            throw new ResourceInvalidCreditCardDataException("Dados inválidos.");
       }
        return cartaoService.autenticarCartao(headers, novoCartao);
    }

    @PostMapping("/cobranca")
    public ResponseEntity<DadosCobranca> realizarCobranca(@RequestHeader MultiValueMap<String, String> headers, @RequestBody NovaCobrancaDTO novaCobranca) {
        return cartaoService.realizarCobranca(headers, novaCobranca);
    }
}
