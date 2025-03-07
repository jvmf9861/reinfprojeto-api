package xmlprojeto.beans;

import java.util.Date;

public class DadosPlanilha {
    private String evento; 
	private String cnpj;
    private String natRend;
    private Date dtFG;
    private double vlrBruto;
    private double vlrBaseAgreg;
    private double vlrAgreg;
    private String codDarf;

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNatRend() {
        return natRend;
    }

    public void setNatRend(String natRend) {
        this.natRend = natRend;
    }

    public Date getDtFG() {
        return dtFG;
    }

    public void setDtFG(Date dtFG) {
        this.dtFG = dtFG;
    }

    public double getVlrBruto() {
        return vlrBruto;
    }

    public void setVlrBruto(double vlrBruto) {
        this.vlrBruto = vlrBruto;
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

    public String getCodDarf() {
        return codDarf;
    }

    public void setCodDarf(String codDarf) {
        this.codDarf = codDarf;
    }
}
