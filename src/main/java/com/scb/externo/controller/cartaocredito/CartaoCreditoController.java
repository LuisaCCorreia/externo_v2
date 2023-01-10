package com.scb.externo.controller.cartaocredito;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.AsaasCobrancaResponseDTO;
import com.scb.externo.shared.NovaCobrancaDTO;
import com.scb.externo.shared.NovoCartaoDTO;

@RestController
@RequestMapping("/api/externo/cartao")
public class CartaoCreditoController {
    @Autowired
    CartaoCreditoService cartaoService;

    private static final String MENSAGEM422 = "Dados Inválidos";

    private boolean validarCVV(String cvv) {
        return cvv.matches("\\d{3,4}");
    }

    private boolean validarNumero(String numero) {
        return numero.matches("\\d+");
    }

    private boolean validarDataValidade(String data) {
        LocalDate dataAtual = LocalDate.now();

        try {
            LocalDate dataInserida = LocalDate.parse(data);
            return dataAtual.isBefore(dataInserida);
        } catch (Exception e) {
            return false;
        }       
    }

    private boolean validarNome(String nome) {
        return nome != null;
    }

    private boolean verificarIdCiclista(String idCiclista) {
        try {
            UUID.fromString(idCiclista);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean verificarValor(float valor) {
        return valor >= 5;
    }
    
    @PostMapping("/validacaocartao")
    public ResponseEntity<APICartaoTokenResponse> autenticarCartao(@RequestHeader MultiValueMap<String, String> headers, @RequestBody NovoCartaoDTO novoCartao) {
       if(!validarNome(novoCartao.getNomeTitular()) || !validarCVV(novoCartao.getCvv()) || !validarNumero(novoCartao.getNumero()) || !validarDataValidade(novoCartao.getValidade())) {
            throw new ResourceInvalidException(MENSAGEM422);
       }
        return cartaoService.autenticarCartao(headers, novoCartao);
    }

    @PostMapping("/cobranca")
    public ResponseEntity<DadosCobranca> realizarCobranca(@RequestHeader MultiValueMap<String, String> headers, @RequestBody NovaCobrancaDTO novaCobranca) {
        if(!verificarIdCiclista(novaCobranca.getCiclista()) || !verificarValor(novaCobranca.getValor())) {
            throw new ResourceInvalidException(MENSAGEM422);
        }
        
        return cartaoService.realizarCobranca(headers, novaCobranca);
    }

    @GetMapping("/cobranca/{idCobranca}")
    public ResponseEntity<AsaasCobrancaResponseDTO>  resgatarCobranca(@RequestHeader MultiValueMap<String, String> headers, @PathVariable String idCobranca) {
        return cartaoService.resgatarCobranca(headers, idCobranca);
    }

    @PostMapping("/filaCobranca")
    public ResponseEntity<DadosCobranca> colocarCobrancaFila(@RequestBody NovaCobrancaDTO novaCobranca) {
        if(!verificarIdCiclista(novaCobranca.getCiclista()) || !verificarValor(novaCobranca.getValor())) {
            throw new ResourceInvalidException(MENSAGEM422);
        }
        return cartaoService.colocarCobrancaFila(novaCobranca);
    }
}
