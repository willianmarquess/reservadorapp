package br.com.reservador.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Empresa {
    private String idEmpresa;
    private String nomeEmpresa;
    private String cnpjEmpresa;
    private String tipoEmpresa;
    private Usuario usuarioAdministrador;
    private Map<String, Object> itens = new HashMap<>();

    public Empresa(String idEmpresa, String nomeEmpresa, String cnpjEmpresa, String tipoEmpresa, Usuario usuarioAdministrador) {
        this.idEmpresa = idEmpresa;
        this.nomeEmpresa = nomeEmpresa;
        this.cnpjEmpresa = cnpjEmpresa;
        this.tipoEmpresa = tipoEmpresa;
        this.usuarioAdministrador = usuarioAdministrador;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idEmpresa", idEmpresa);
        result.put("nomeEmpresa", nomeEmpresa);
        result.put("cnpjEmpresa", cnpjEmpresa);
        result.put("tipoEmpresa", tipoEmpresa);
        result.put("usuarioAdministrador", usuarioAdministrador);
        result.put("itens", itens);

        return result;
    }

    public Empresa(){
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public Usuario getUsuarioAdministrador() {
        return usuarioAdministrador;
    }

    public void setUsuarioAdministrador(Usuario usuarioAdministrador) {
        this.usuarioAdministrador = usuarioAdministrador;
    }



}
