package com.scb.externo.service.cartaocredito;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.shared.APICartaoDeCreditoResponseBody;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.CartaoCreditoAsaas;
import com.scb.externo.shared.NovaTokenizacao;
import com.scb.externo.shared.NovoCartaoDTO;
import com.scb.externo.shared.NovoCliente;

@Service
public class AutenticarDadosService {

  @Autowired
  DadosCartaoRepository cartaoRepository;

  private String codigoUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  private CartaoCreditoAsaas gerarCartaoAsaas(NovoCartaoDTO novoCartao, String holderName) {

    LocalDate data = LocalDate.parse(novoCartao.getValidade());

    return  new CartaoCreditoAsaas(holderName, novoCartao.getNumero(), Integer.toString(data.getMonthValue()), Integer.toString(data.getYear()), novoCartao.getCvv());
  }

  private NovaTokenizacao gerarDadosParaTokenizacao(String clienteId, CartaoCreditoAsaas cartaoAsaas) {
    return new NovaTokenizacao(clienteId, cartaoAsaas);
  }

  private void registrarDadosAutenticacao(String clienteId, String tokenCartao) {
    DadosToken dadosToken = new DadosToken(codigoUUID(),clienteId, tokenCartao);
    cartaoRepository.save(dadosToken);
  }

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
      
      CartaoCreditoAsaas cartaoAsaas = gerarCartaoAsaas(novoCartao, novoCliente.getName());
      NovaTokenizacao autenticacao = gerarDadosParaTokenizacao(novoCliente.getId(), cartaoAsaas);
      String autenticarCartaoURL = "https://sandbox.asaas.com/api/v3/creditCard/tokenize";
      HttpEntity<NovaTokenizacao> entity = new HttpEntity<>(autenticacao, headers);

      ResponseEntity <APICartaoTokenResponse> respostaTokenizacao = new RestTemplate().postForEntity(autenticarCartaoURL, entity, APICartaoTokenResponse.class);
      APICartaoTokenResponse bodyTokenizacao = respostaTokenizacao.getBody();

      if(bodyTokenizacao != null) {

        registrarDadosAutenticacao(novoCliente.getId(), bodyTokenizacao.getCreditCardToken());         

        return respostaTokenizacao;
      } else {
        throw new ResourceNotFoundException("Não encontrado.");  
      }     
    }
    throw new ResourceNotFoundException("Não encontrado.");  
  }
}
