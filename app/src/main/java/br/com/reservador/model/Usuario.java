package br.com.reservador.model;

public class Usuario {

    private String idUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private String nomeUsuario;
    private String cpfUsuario;
    private String tipoUsuario;
    private Empresa empresaUsuario;

    public Usuario(String idUsuario, String emailUsuario, String senhaUsuario, String nomeUsuario, String cpfUsuario) {
        this.idUsuario = idUsuario;
        this.emailUsuario = emailUsuario;
        this.senhaUsuario = senhaUsuario;
        this.nomeUsuario = nomeUsuario;
        this.cpfUsuario = cpfUsuario;
        this.tipoUsuario = tipoUsuario;
        this.empresaUsuario = empresaUsuario;
    }
    public Usuario(){}

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Empresa getEmpresaUsuario() {
        return empresaUsuario;
    }

    public void setEmpresaUsuario(Empresa empresaUsuario) {
        this.empresaUsuario = empresaUsuario;
    }
}
