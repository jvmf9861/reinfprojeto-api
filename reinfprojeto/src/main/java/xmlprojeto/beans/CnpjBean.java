package xmlprojeto.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class CnpjBean {
    private String selectedCnpj;
    private List<String> cnpjs;

    public CnpjBean() {
        cnpjs = new ArrayList<>();
        cnpjs.add("05424540000116");
        cnpjs.add("32243347000151");
        // Adicione outros CNPJs conforme necessário
    }

    public String getSelectedCnpj() {
        return selectedCnpj;
    }

    public void setSelectedCnpj(String selectedCnpj) {
        this.selectedCnpj = selectedCnpj;
    }

    public List<String> getCnpjs() {
        return cnpjs;
    }

    public void setCnpjs(List<String> cnpjs) {
        this.cnpjs = cnpjs;
    }
}
