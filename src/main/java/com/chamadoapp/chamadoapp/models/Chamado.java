package com.chamadoapp.chamadoapp.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Chamado implements Serializable {
    private static final long serialVersioUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dataRegistro;
    @NotNull
    @ManyToOne
    private Cliente cliente;
    @NotNull
    @ManyToOne
    private Funcionario funcionario;
    /**
     * T - Treinamento
     * S - Serviço
     * I - Instalação
     */
    @NotEmpty
    @Column(length = 2)
    private String tipo;
    /**
     * EA - Em andamento
     * NI - Nao Iniciado
     * FI - Finalizado
     */
    @Column(length = 2)
    private String status;
    @NotEmpty
    private String observacao;
    
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chamado other = (Chamado) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Chamado(){
        this.dataRegistro = new Date();
        this.status = "NI";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
