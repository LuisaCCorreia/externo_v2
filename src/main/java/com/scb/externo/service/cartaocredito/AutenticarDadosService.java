package com.scb.externo.service.cartaocredito;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.scb.externo.models.BancoDeDados;
import com.scb.externo.models.exceptions.ResourceNotFoundCreditCardException;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.CartaoRepositoryInterface;
import com.scb.externo.shared.APICartaoDeCreditoResponseBody;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.CartaoCreditoAsaas;
import com.scb.externo.shared.NovaTokenizacao;
import com.scb.externo.shared.NovoCartaoDTO;
import com.scb.externo.shared.NovoCliente;

@Service
public class AutenticarDadosService {

  @Autowired
  CartaoRepositoryInterface cartaoRepository;

  public ResponseEntity<APICartaoDeCreditoResponseBody> criarCliente(MultiValueMap<String, String> headers, String nomeTitular) {
    String criarClienteURL = "https://sandbox.asaas.com/api/v3/customers";
    NovoCliente novoCliente = new NovoCliente();
    novoCliente.setName(nomeTitular);
    HttpEntity<NovoCliente> entity = new HttpEntity<>(novoCliente, headers);
    return new RestTemplate().postForEntity(criarClienteURL, entity, APICartaoDeCreditoResponseBody.class);
  }

  public ResponseEntity<APICartaoTokenResponse> autenticarCartao(MultiValueMap<String, String> headers, NovoCartaoDTO novoCartao) {
    APICartaoDeCreditoResponseBody novoCliente = criarCliente(headers, novoCartao.getNomeTitular()).getBody();

    if(novoCliente != null) {
      LocalDate data = LocalDate.parse(novoCartao.getValidade());
      CartaoCreditoAsaas cartaoAsaas = new CartaoCreditoAsaas();
      cartaoAsaas.setExpiryMonth(Integer.toString(data.getMonthValue()));
      cartaoAsaas.setExpiryYear(Integer.toString(data.getYear()));
      cartaoAsaas.setHolderName(novoCliente.getName());
      cartaoAsaas.setNumber(novoCartao.getNumero());
      cartaoAsaas.setCcv(novoCartao.getCvv());
  
      NovaTokenizacao autenticacao = new NovaTokenizacao();
      autenticacao.setCustomer(novoCliente.getId());
      autenticacao.setCreditCard(cartaoAsaas);
  
      String autenticarCartaoURL = "https://sandbox.asaas.com/api/v3/creditCard/tokenize";
      HttpEntity<NovaTokenizacao> entity = new HttpEntity<>(autenticacao, headers);

      ResponseEntity <APICartaoTokenResponse> respostaTokenizacao = new RestTemplate().postForEntity(autenticarCartaoURL, entity, APICartaoTokenResponse.class);
      APICartaoTokenResponse bodyTokenizacao = respostaTokenizacao.getBody();

      if(bodyTokenizacao != null) {
        BancoDeDados bancoDeDados = new BancoDeDados();
        bancoDeDados.setCustomer(novoCliente.getId());
        bancoDeDados.setToken(bodyTokenizacao.getCreditCardToken());

        DadosToken dadosToken = new DadosToken("1", novoCliente.getId(), bodyTokenizacao.getCreditCardToken());

        cartaoRepository.save(dadosToken);

        return respostaTokenizacao;
      } else {
        throw new ResourceNotFoundCreditCardException("Não encontrado.");  
      }     
    }
    throw new ResourceNotFoundCreditCardException("Não encontrado.");  
  }
}
