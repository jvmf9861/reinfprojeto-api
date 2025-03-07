package xmlprojeto.beans;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class ConsultaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ConsultaBean.class.getName());

    private List<DadosPlanilha> dataList;
    private List<String> events;
    private List<DadosPlanilha> filteredData;
    private String selectedEvent;
    private final String filePath = "C:\\projetoxml\\Extração SIAFI-Web Dedução DDF025.xlsx";

    public ConsultaBean() {
        dataList = new ArrayList<>();
        filteredData = new ArrayList<>();
        events = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        try (InputStream input = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(input)) {
            Sheet sheet = workbook.getSheetAt(0);
            dataList.clear();
            events.clear();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Ignora o cabeçalho
                }

                DadosPlanilha dados = new DadosPlanilha();
                dados.setEvento(row.getCell(5).getStringCellValue());
                dados.setCnpj(row.getCell(6).getStringCellValue());
                dados.setNatRend(row.getCell(8).getStringCellValue());
                dados.setDtFG(row.getCell(7).getDateCellValue());
                dados.setVlrBruto(row.getCell(9).getNumericCellValue());
                dados.setVlrBaseAgreg(row.getCell(9).getNumericCellValue());
                dados.setVlrAgreg(row.getCell(10).getNumericCellValue());
                dados.setCodDarf(row.getCell(14).getStringCellValue());

                dataList.add(dados);

                // Adiciona eventos à lista de seleção
                String evento = dados.getEvento();
                if (!events.contains(evento)) {
                    events.add(evento);
                } 
                
            }
            
            filterData(); // Aplica filtro após carregar os dados
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Dados carregados com sucesso!"));
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao carregar dados!", e.getMessage()));
            logger.log(Level.SEVERE, "Falha ao carregar dados!", e);
        }
    }

    public void filterData() {
        filteredData.clear();

        if (selectedEvent == null || selectedEvent.isEmpty()) {
            filteredData.addAll(dataList); // Mostrar todos os dados se nenhum evento estiver selecionado
        } else {
            for (DadosPlanilha item : dataList) {
                if (item.getEvento().equals(selectedEvent)) {
                    filteredData.add(item);
                }
            }
        }
    }


    public List<DadosPlanilha> getDataList() {
        return dataList;
    }

    public List<String> getEvents() {
        return events;
    }

    public List<DadosPlanilha> getFilteredData() {
        return filteredData;
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(String selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}
