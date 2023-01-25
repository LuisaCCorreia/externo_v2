package com.scb.externo.controller.cartaocredito;

import java.io.IOException;
import java.time.LocalDate;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
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

    private boolean verificarValor(float valor) {
        return valor >= 5;
    }
    
    @PostMapping("/validacaocartao")
    public ResponseEntity<String> autenticarCartao(@RequestBody NovoCartaoDTO novoCartao) throws IOException, InterruptedException, JSONException {
       if(!validarNome(novoCartao.getNomeTitular()) || !validarCVV(novoCartao.getCvv()) || !validarNumero(novoCartao.getNumero()) || !validarDataValidade(novoCartao.getValidade())) {
            throw new ResourceInvalidException(MENSAGEM422);
       }
        return cartaoService.autenticarCartao(novoCartao);
    }

    @PostMapping("/cobranca")
    public ResponseEntity<DadosCobranca> realizarCobranca(@RequestBody NovaCobrancaDTO novaCobranca) throws JSONException, IOException, InterruptedException {
        if( !verificarValor(novaCobranca.getValor())) {
            throw new ResourceInvalidException(MENSAGEM422);
        }
        
        return cartaoService.realizarCobranca(novaCobranca);
    }

    @GetMapping("/cobranca/{idCobranca}")
    public ResponseEntity<String>  resgatarCobranca(@PathVariable String idCobranca) throws JSONException, IOException, InterruptedException {
        return cartaoService.resgatarCobranca(idCobranca);
    }

    @PostMapping("/filaCobranca")
    public ResponseEntity<DadosCobranca> colocarCobrancaFila(@RequestBody NovaCobrancaDTO novaCobranca) {
        if( !verificarValor(novaCobranca.getValor())) {
            throw new ResourceInvalidException(MENSAGEM422);
        }
        return cartaoService.colocarCobrancaFila(novaCobranca);
    }
}
