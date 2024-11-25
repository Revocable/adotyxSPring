package br.com.adotyx.domain;

public enum Tipo {
    ADMIN("ADMIN"),
    USER("USER");
    
    private String descricao;

    Tipo (String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return this.descricao;
    }
}
