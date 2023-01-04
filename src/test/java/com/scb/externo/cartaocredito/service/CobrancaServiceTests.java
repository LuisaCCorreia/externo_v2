package com.scb.externo.cartaocredito.service;

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
import org.springframework.web.client.RestTemplate;

import com.scb.externo.service.cartaocredito.CobrancaService;
import com.scb.externo.shared.NovaCobrancaAsaas;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CobrancaServiceTests {
    @Mock
    RestTemplate mockedRestTemplate;

    @InjectMocks
    CobrancaService cobrancaService;

    @Test
    void realizar_cobranca() {

        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);  

        NovaCobrancaAsaas novaCobrancaAsaas = new NovaCobrancaAsaas(null, strDate, null, null);

    }
    
}
