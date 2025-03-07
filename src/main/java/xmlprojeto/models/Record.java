package xmlprojeto.models;


public class Record {
	private String cnpj10;
	private String natRend;
	private String dtFG;
	private double vlrBruto;
	private double vlrBaseAgreg;
	private double vlrAgreg;
	private String codDarf;

	public Record(String cnpj10, String natRend, String dtFG, double vlrBruto, double vlrBaseAgreg, double vlrAgreg, String codDarf) {
    	this.cnpj10 = cnpj10;
    	this.natRend = natRend;
    	this.dtFG = dtFG;
        this.vlrBruto = vlrBruto;
        this.vlrBaseAgreg = vlrBaseAgreg;
        this.vlrAgreg = vlrAgreg;
        this.codDarf = codDarf;
    }

    public String getcnpj10() {
        return cnpj10;
    }
    
    public String getnatRend() {
        return natRend;
    }
    
    public String getdtFG() {
        return dtFG;
    }

    public double getVlrBruto() {
        return vlrBruto;
    }


    public double getvlrBaseAgreg() {
        return vlrBaseAgreg;
    }

    public double getvlrAgreg() {
        return vlrAgreg;
    }
	
    public String getcodDarf() {
        return codDarf;
    }




}
