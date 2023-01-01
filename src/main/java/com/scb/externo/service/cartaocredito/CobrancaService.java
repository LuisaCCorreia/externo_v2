package com.scb.externo.service.cartaocredito;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.scb.externo.models.BancoDeDados;
import com.scb.externo.repository.cartaocredito.CartaoCreditoRepository;
import com.scb.externo.shared.NovaCobrancaAsaas;
import com.scb.externo.shared.NovaCobrancaDTO;

@Service
public class CobrancaService {

    @Autowired
    CartaoCreditoRepository cartaoRepository;
    
    public void realizarCobranca(MultiValueMap<String, String> headers, NovaCobrancaDTO novaCobranca) {
        String fazerCobrancaURL = "https://sandbox.asaas.com/api/v3/payments";
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);  

        NovaCobrancaAsaas novaCobrancaAsaas = new NovaCobrancaAsaas();

        novaCobrancaAsaas.setCustomer(novaCobranca.getCiclista());
        novaCobrancaAsaas.setDueDate(strDate);
        novaCobrancaAsaas.setValue(novaCobranca.getValor());

        BancoDeDados bancoDeDados = cartaoRepository.mostrarItemPorId(novaCobranca.getCiclista());

        System.out.println("Oi");
        System.out.println(bancoDeDados.getToken());
        
    }
}
