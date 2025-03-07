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
public class Consulta009Bean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(Consulta009Bean.class.getName());
	private List<Dados009Planilha> dataList;
	private List<String> events;
	private List<Dados009Planilha> filteredData;
	private String selectedEvent = "DDF009";
	private final String filePath = "C:\\projetoxml\\Extração SIAFI-Web Dedução DDF025.xlsx";

	public Consulta009Bean() {
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

				Dados009Planilha dados = new Dados009Planilha();
				dados.setEvento(row.getCell(5).getStringCellValue());
				dados.setCnpj(row.getCell(6).getStringCellValue());
				dados.setNatRend(row.getCell(8).getStringCellValue());
				dados.setDtFG(row.getCell(7).getDateCellValue());
				dados.setVlrBruto(row.getCell(9).getNumericCellValue());
				dados.setVlrBaseAgreg(row.getCell(9).getNumericCellValue());
				dados.setVlrAgreg(row.getCell(10).getNumericCellValue());
				dados.setCodDarf(row.getCell(14).getStringCellValue());

				dados.setExecutado(false);

				dataList.add(dados);

				// Adiciona eventos à lista de seleção
				String evento = dados.getEvento();
				if (!events.contains(evento)) {
					events.add(evento);
				}

			}

			//filterData(); // Aplica filtro após carregar os dados
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao carregar dados!", e.getMessage()));
			logger.log(Level.SEVERE, "Falha ao carregar dados!", e);
		}
	}

	public void filterData() {
		filteredData.clear();

		if (selectedEvent == null || selectedEvent.isEmpty()) {
			filteredData.addAll(dataList); // Mostrar todos os dados se nenhum evento estiver selecionado
		} else {
			
			for (Dados009Planilha item : dataList) {

				if (item.getEvento().equals(selectedEvent)) {
					filteredData.add(item);
				}
				
			}

		}
	}

	public void executeProgram() {
		try {
			// Verifica se algum registro já foi executado (Executado marcado como true)
			for (Dados009Planilha item : filteredData) {
				if (item.isExecutado()) {
					// Exibe a mensagem de erro temporária
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atenção! O Evento " + selectedEvent + " já foi enviado.", "");
					FacesContext.getCurrentInstance().addMessage("growl", message); // Especifica o ID do growl
					return; // Bloqueia a execução se algum registro estiver executado
				}
			}

			// Se não houver nenhum item já executado, a execução prossegue
				R4010cnpj32243347000151.main(null, selectedEvent);
				XMLTagSubstitution.main(null);

			// Marca os registros do evento como executados

			for (Dados009Planilha item : filteredData) {
				item.setExecutado(true);
			}

			filterData();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Parabens! A execução do evento " + selectedEvent + " foi um sucesso!!", "");
			FacesContext.getCurrentInstance().addMessage("growl", message);

		} catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro durante a execução.",
					"Erro!");
			FacesContext.getCurrentInstance().addMessage("growl", message);
		}
	}

	public List<Dados009Planilha> getDataList() {
		return dataList;
	}

	public List<String> getEvents() {
		return events;
	}

	public List<Dados009Planilha> getFilteredData() {
		return filteredData;
	}

	public String getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(String selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
}
