package com.scb.externo.repository.cartaocredito;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.scb.externo.models.BancoDeDados;

@Repository
public class CartaoCreditoRepository {
    ArrayList<BancoDeDados> bancoDeDados = new ArrayList<>();

    public void gravar(BancoDeDados dadosCartao) {
        bancoDeDados.add(dadosCartao);
    }

    public BancoDeDados  mostrarItemPorId(String id) {
        for(int i = 0; i < bancoDeDados.size(); i++) {
            if(bancoDeDados.get(i).getCustomer().equals(id)) {
                return bancoDeDados.get(i);
            }
        }
        return null;
    }
}
