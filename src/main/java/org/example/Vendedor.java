package org.example;

public class Vendedor {
    private int id;
    private String nome;
    private String cpf_cnpj;
    private String email;
    private String telefone;

    public Vendedor() {}

    public Vendedor(int id, String nome, String cpf_cnpj, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf_cnpj = cpf_cnpj;
        this.email = email;
        this.telefone = telefone;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String dadosParaArquivo()
    {
        return(id+","+nome+","+cpf_cnpj+","+email+","+telefone);
    }
}

