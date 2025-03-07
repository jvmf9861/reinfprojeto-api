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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import xmlprojeto.XMLTagSubstitution;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class Consulta2010Bean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Consulta2010Bean.class.getName());
	private boolean showDataTable = false;
	private List<Dados2010Planilha> dataList;
	private List<String> events;
	private List<Dados2010Planilha> filteredData;
	private String selectedEvent = "DDF021";
		
	private final String filePath = "C:\\projetoxml\\planilhateste17.xlsx";

	public Consulta2010Bean() {
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

				Dados2010Planilha dados = new Dados2010Planilha();
				dados.setCnpj(getCellValueAsString(row, 7));
				dados.setNf(getCellValueAsString(row, 0));
				dados.setDtFG(row.getCell(2).getDateCellValue());
				dados.setVlrNf(row.getCell(3).getNumericCellValue());
				dados.setVlrBaseAgreg(row.getCell(4).getNumericCellValue());
				dados.setVlrAgreg(row.getCell(6).getNumericCellValue());

				dados.setExecutado(false);

				dataList.add(dados);

				String evento = "DDF021";
				if (!events.contains(evento)) {
					events.add(evento);
				}
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Falha ao carregar dados!", e);
		}
	}

	private String getCellValueAsString(Row row, int cellIndex) {
		return row.getCell(cellIndex) != null ? row.getCell(cellIndex).toString() : "";
	}

	public void filterData() {
		filteredData.clear();

		// Se o evento selecionado for "DDF021", traz todos os registros
		if ("DDF021".equals(selectedEvent)) {
			
			for (Dados2010Planilha item10 : dataList) {
				filteredData.add(item10);
			}
		}
		
	}

	// Método para executar o programa de acordo com o evento selecionado
	public void executeProgram() {
		try {
			
			// Verifica se algum registro já foi executado
			for (Dados2010Planilha item10 : filteredData) {
				if (item10.isExecutado()) {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção! O Evento " + selectedEvent + " já foi enviado.", "");
					FacesContext.getCurrentInstance().addMessage("growl", message);
					return; // Bloqueia a execução se algum registro já foi executado
				}
			}

			// Executa o programa de acordo com o evento selecionado
			if (selectedEvent.equals("DDF021")) {	
				R2010cnpj32243347000151.main(null);
				XMLTagSubstitution.main(null);
			}

			// Marca os registros como executados
			for (Dados2010Planilha item10 : filteredData) {
				if ("DDF021".equals(selectedEvent)) {
					item10.setExecutado(true);
				}
			}

			filterData(); // Aplica o filtro novamente após a execução

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,	"Parabéns! A execução do evento " + selectedEvent + " foi um sucesso!", "");
			FacesContext.getCurrentInstance().addMessage("growl", message);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante a execução.",
					"Erro!");
			FacesContext.getCurrentInstance().addMessage("growl", message);
		}
	}

	public List<Dados2010Planilha> getDataList() {
		return dataList;
	}

	public List<String> getEvents() {
		return events;
	}

	public List<Dados2010Planilha> getfilteredData() {
		return filteredData;
	}

	public String getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(String selectedEvent) {
		this.selectedEvent = selectedEvent;
	}

	public boolean isShowDataTable() {
		return showDataTable;
	}

	public void setShowDataTable(boolean showDataTable) {
		this.showDataTable = showDataTable;
	}
}
