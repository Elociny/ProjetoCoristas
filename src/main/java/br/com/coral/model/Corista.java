package br.com.coral.model;

import jakarta.persistence.*;

@Entity
@Table(name = "corista")
public class Corista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;
    private String email;

    private boolean temPendencias;
    private int faltasNosUltimosDoisEnsaios;

    public Corista() { }

    public Corista(String nome, String email, boolean temPendencias) {
        this.nome = nome;
        this.email = email;
        this.temPendencias = temPendencias;
        this.faltasNosUltimosDoisEnsaios = 0;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTemPendencias() {
        return temPendencias;
    }

    public void setTemPendencias(boolean temPendencias) {
        this.temPendencias = temPendencias;
    }

    public int getFaltasNosUltimosDoisEnsaios() {
        return faltasNosUltimosDoisEnsaios;
    }

    public void setFaltasNosUltimosDoisEnsaios(int faltasNosUltimosDoisEnsaios) {
        this.faltasNosUltimosDoisEnsaios = faltasNosUltimosDoisEnsaios;
    }
}