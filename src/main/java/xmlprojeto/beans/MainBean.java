package xmlprojeto.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped

public class MainBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String selectedEvent;
    private Object currentBean;

    @ManagedProperty("#{consulta2010Bean}")
    private Consulta2010Bean consulta2010Bean;

    @ManagedProperty("#{consultaBean}")
    private ConsultaBean consultaBean;
    
    @ManagedProperty("#{consulta009Bean}")
    private Consulta009Bean consulta009Bean;


    /**
     * Método para carregar o bean correto com base no evento selecionado.
     */

	@PostConstruct
	public void init() {
		loadBean();
	}

	public void loadBean() {
        if (selectedEvent == null || selectedEvent.isEmpty()) {
            currentBean = null;
            return;
        };

        switch (selectedEvent) {
            case "DDF021":
                currentBean = consulta2010Bean;
                break;
            case "DDF009":
            	currentBean = consulta009Bean;
            	break;
             case "DDF025":
                currentBean = consultaBean;
                break;
            default:
                currentBean = null;
        }
    }

    /**
     * Método para carregar os dados e aplicar o filtro.
     */
    public void loadAndFilterData() {
        // Primeiro, carregar o bean correto
        loadBean();

        // Depois, aplicar o filtro com base no evento selecionado
        if ("DDF021".equals(selectedEvent) && consulta2010Bean != null) {
            consulta2010Bean.filterData();
            
        } else if (("DDF009".equals(selectedEvent)) && consulta009Bean != null) {
            consulta009Bean.filterData();
            
        } else if (("DDF025".equals(selectedEvent)) && consultaBean != null) {
        	consultaBean.filterData();
        }
    }
    
    public void loadAndExecuteProgram() {
 
    	loadBean();

        // aplica o filtro com base no evento selecionado
        if ("DDF021".equals(selectedEvent) && consulta2010Bean != null) {
        	consulta2010Bean.executeProgram();
        	
        } else if (("DDF009".equals(selectedEvent)) && consulta009Bean != null) {
        	consulta009Bean.executeProgram();
        	
        }else if (("DDF025".equals(selectedEvent)) && consultaBean != null) {
        	consultaBean.executeProgram();
        }
    }
    
    public String getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(String selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Object getCurrentBean() {
        return currentBean;
    }

    public void setConsulta2010Bean(Consulta2010Bean consulta2010Bean) {
        this.consulta2010Bean = consulta2010Bean;
    }

    public void setConsultaBean(ConsultaBean consultaBean) {
        this.consultaBean = consultaBean;
    }
    
    public void setConsulta009Bean(Consulta009Bean consulta009Bean) {
        this.consulta009Bean = consulta009Bean;
    }

}
