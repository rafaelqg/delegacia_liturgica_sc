/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.qg.loja.modelo;

import java.util.Date;

import br.qg.loja.integracao.DAOFactory;

/**
 *
 * @author Rafael Queiroz Gonçalves
 */
public class Obreiro {

    private int numero = -1;
    private int ime;
    private String nome;
    private String nomePai;
    private String nomeMae;
    private String rg;
    private String cpf;
    private Date dtNascimento;
    private String localNascimento;
    private String enderecoResidencial;
    private String enderecoComercial;
    private String profissao;
    private String cargo;
    private double rendimento;
    private String outrasInformacoes;
    private String nomeEsposa;
    private Date dtNascimentoEsposa;
    private String lojaSimbolicaAtual;
    private String email;
    private int cim;
    private Date dtIniciacao = null;
    private Date dtElevacao = null;
    private Date dtExaltacao = null;
    private String lojaExaltacao;
    private String lojaElevacao;
    private String lojaIniciacao;
    private boolean mestreInstalado;

    
    @Override
    public String toString(){
        String description="";
        int grau=DAOFactory.getDAOGrau().getGrauAtual(this).getNumero();
        description = this.getNome() + " (ime "+ime+", grau "+ grau+ ")";
        return description;
    }
      
    public boolean isMestreInstalado() {
        return mestreInstalado;
    }

    public void setMestreInstalado(boolean mestreInstalado) {
        this.mestreInstalado = mestreInstalado;
    }

    
    public Date getDtIniciacao() {
        return dtIniciacao;
    }

    public void setDtIniciacao(Date dtIniciacao) {
        this.dtIniciacao = dtIniciacao;
    }

    public Date getDtElevacao() {
        return dtElevacao;
    }

    public void setDtElevacao(Date dtElevacao) {
        this.dtElevacao = dtElevacao;
    }

    public Date getDtExaltacao() {
        return dtExaltacao;
    }

    public void setDtExaltacao(Date dtExaltacao) {
        this.dtExaltacao = dtExaltacao;
    }

    public String getLojaExaltacao() {
        return lojaExaltacao;
    }

    public void setLojaExaltacao(String lojaExaltacao) {
        this.lojaExaltacao = lojaExaltacao;
    }

    public String getLojaElevacao() {
        return lojaElevacao;
    }

    public void setLojaElevacao(String lojaElevacao) {
        this.lojaElevacao = lojaElevacao;
    }

    public String getLojaIniciacao() {
        return lojaIniciacao;
    }

    public void setLojaIniciacao(String lojaIniciacao) {
        this.lojaIniciacao = lojaIniciacao;
    }

    public int getCim() {
        return cim;
    }

    public void setCim(int cim) {
        this.cim = cim;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getIme() {
        return ime;
    }

    public void setIme(int ime) {
        this.ime = ime;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getLocalNascimento() {
        return localNascimento;
    }

    public void setLocalNascimento(String localNascimento) {
        this.localNascimento = localNascimento;
    }

    public String getEnderecoResidencial() {
        return enderecoResidencial;
    }

    public void setEnderecoResidencial(String enderecoResidencial) {
        this.enderecoResidencial = enderecoResidencial;
    }

    public String getEnderecoComercial() {
        return enderecoComercial;
    }

    public void setEnderecoComercial(String enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getRendimento() {
        return rendimento;
    }

    public void setRendimento(double rendimento) {
        this.rendimento = rendimento;
    }

    public String getOutrasInformacoes() {
        return outrasInformacoes;
    }

    public void setOutrasInformacoes(String outrasInformacoes) {
        this.outrasInformacoes = outrasInformacoes;
    }

    public String getNomeEsposa() {
        return nomeEsposa;
    }

    public void setNomeEsposa(String nomeEsposa) {
        this.nomeEsposa = nomeEsposa;
    }

    public Date getDtNascimentoEsposa() {
        return dtNascimentoEsposa;
    }

    public void setDtNascimentoEsposa(Date dtNascimentoEsposa) {
        this.dtNascimentoEsposa = dtNascimentoEsposa;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getLojaSimbolicaAtual() {
        return lojaSimbolicaAtual;
    }

    public void setLojaSimbolicaAtual(String lojaSimbolicaAtual) {
        this.lojaSimbolicaAtual = lojaSimbolicaAtual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * 
     * @return retorna a última loja que o obreiro recebeu grau
     */
    public Loja getLojaAtual(){
    	return DAOFactory.getDAOObreiro().buscaLojaAtual(this);
    }
    
}
