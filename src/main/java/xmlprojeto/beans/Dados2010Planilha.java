package xmlprojeto.beans;

import java.util.Date;

public class Dados2010Planilha {
    private String cnpj;
    private String nf;
    private Date dtFG;
    private double vlrNf;
    private double vlrBaseAgreg;
    private double vlrAgreg;
    private boolean executado;
    
    
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNf() {
		return nf;
	}

	public void setNf(String nf) {
		this.nf = nf;
	}

	public Date getDtFG() {
		return dtFG;
	}

	public void setDtFG(Date dtFG) {
		this.dtFG = dtFG;
	}

	public double getVlrNf() {
		return vlrNf;
	}

	public void setVlrNf(double vlrNf) {
		this.vlrNf = vlrNf;
	}

	public double getVlrBaseAgreg() {
		return vlrBaseAgreg;
	}

	public void setVlrBaseAgreg(double vlrBaseAgreg) {
		this.vlrBaseAgreg = vlrBaseAgreg;
	}

	public double getVlrAgreg() {
		return vlrAgreg;
	}

	public void setVlrAgreg(double vlrAgreg) {
		this.vlrAgreg = vlrAgreg;
	}

	public boolean isExecutado() {
		return executado;
	}

	public void setExecutado(boolean executado) {
		this.executado = executado;
	}

    
}

