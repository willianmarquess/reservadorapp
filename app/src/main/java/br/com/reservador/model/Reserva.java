package br.com.reservador.model;

import java.util.List;

public class Reserva {

    private String idReserva;
    private String dataReserva;
    private String quantidadeReserva;
    private String idItem;
    private String nomeItem;
    private String observacaoItem;

    public Reserva(String idReserva, String dataReserva, String quantidadeReserva, String idItem, String nomeItem, String observacaoItem) {
        this.idReserva = idReserva;
        this.dataReserva = dataReserva;
        this.quantidadeReserva = quantidadeReserva;
        this.idItem = idItem;
        this.nomeItem = nomeItem;
        this.observacaoItem = observacaoItem;
    }

    public Reserva(){}

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getQuantidadeReserva() {
        return quantidadeReserva;
    }

    public void setQuantidadeReserva(String quantidadeReserva) {
        this.quantidadeReserva = quantidadeReserva;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public String getObservacaoItem() {
        return observacaoItem;
    }

    public void setObservacaoItem(String observacaoItem) {
        this.observacaoItem = observacaoItem;
    }

    @Override
    public String toString() {
        return "Data Reserva :"+dataReserva+"\nNome Item : "+nomeItem+"\nObservação Item: "+observacaoItem+"\nQuantidade Reservada : "+quantidadeReserva;
    }
    }
