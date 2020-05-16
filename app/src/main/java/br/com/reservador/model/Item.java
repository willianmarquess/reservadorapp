package br.com.reservador.model;

public class Item {
    private String idItem;
    private String nomeItem;
    private String observacaoItem;
    private String statusItem;
    private String quantidadeitem;

    public Item(){}

    public Item(String idItem, String nomeItem, String observacaoItem, String statusItem, String quantidadeitem) {
        this.idItem = idItem;
        this.nomeItem = nomeItem;
        this.observacaoItem = observacaoItem;
        this.statusItem = statusItem;
        this.quantidadeitem = quantidadeitem;
    }

    @Override
    public String toString() {
        return "Nome: "+getNomeItem()+"\nObservação: "+getObservacaoItem()+"\nStatus: "+getStatusItem()+"\nQuantidade : "+getQuantidadeitem();
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

    public String getStatusItem() {
        return statusItem;
    }

    public void setStatusItem(String statusItem) {
        this.statusItem = statusItem;
    }

    public String getQuantidadeitem() {
        return quantidadeitem;
    }

    public void setQuantidadeitem(String quantidadeitem) {
        this.quantidadeitem = quantidadeitem;
    }

}
