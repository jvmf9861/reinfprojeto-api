package xmlprojeto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class R2010cnpj05424540000116 {
	
	public static String tipoInscricao;
	public static String anomesdiahora;
	public static String anomesdia;
	public static String horaminutoseg;
	public static String dtStr2;
	public static String tipoIndice;
	public static String vIndObra;
	public static String vIndCprb;
	private static int sequencia=0;
	private static String cnpj8;
	private static String cnpj1="05424540000116";

	
	public static void main(String[] args) {
        String excelFilePath = "C:\\projetoxml\\planilhateste17.xlsx";
        String outputFolder = "C:\\projetoxml\\";
        
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            Map<String, List<NotaFiscal10>> notasPorCNPJ = new HashMap<String, List<NotaFiscal10>>();

            // L� o arquivo Excel e agrupa as notas fiscais por CNPJ
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 0) continue; 

                String cnpj = getStringValue(currentRow.getCell(7));

                NotaFiscal10 nota = new NotaFiscal10(
                		getStringValue(currentRow.getCell(7)),
                        getStringValue(currentRow.getCell(5)),  
                        getStringValue(currentRow.getCell(1)), 
                        getStringValue(currentRow.getCell(0)), 
                        getDateValue(currentRow.getCell(2)),
                        getNumericValue(currentRow.getCell(3)), 
                        getStringValue(currentRow.getCell(8)), 
                        getNumericValue(currentRow.getCell(4)), 
                        getNumericValue(currentRow.getCell(6))
                        
                );

                if (notasPorCNPJ.containsKey(cnpj)) {
                    notasPorCNPJ.get(cnpj).add(nota);
                } else {
                    List<NotaFiscal10> notas = new ArrayList<NotaFiscal10>();
                    notas.add(nota);
                    notasPorCNPJ.put(cnpj, notas);
                }
                
            }
            
            workbook.close();
            inputStream.close();
            
            // Gera XML para o lote de eventos
            generateLoteXML(notasPorCNPJ, outputFolder);

            System.out.println("Arquivos XML gerados com sucesso.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private static String getStringValue(Cell cell) {
        if (cell == null) {
            return "";
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long)cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }

    private static double getNumericValue(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return 0;
        }
        return cell.getNumericCellValue();
    }

    private static String getDateValue(Cell cell) {
        if (cell == null) {
            return "";
        } else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            SimpleDateFormat dateFormatddmmaaaa = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormatddmmaaaa.format(date);
        } else {
            return cell.getStringCellValue();
        }
    }  
    
    
	private static void generateLoteXML(Map<String, List<NotaFiscal10>> notasPorCNPJ, String outputFolder) {

		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			tipoInscricao = "1";
			anomesdiahora = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			anomesdia = new SimpleDateFormat("yyyyMMdd").format(new Date());
			horaminutoseg = new SimpleDateFormat("HHmmss").format(new Date());

			Element reinf1 = doc.createElement("Reinf");
			reinf1.setAttribute("xmlns", "http://www.reinf.esocial.gov.br/schemas/envioLoteEventosAssincrono/v1_00_00");
			doc.appendChild(reinf1);

			Element envioLoteEventos = doc.createElement("envioLoteEventos");
			reinf1.appendChild(envioLoteEventos);

			Element ideContribuinte = doc.createElement("ideContribuinte");
			envioLoteEventos.appendChild(ideContribuinte);

			Element tpInsc1 = doc.createElement("tpInsc");
			tpInsc1.appendChild(doc.createTextNode("1"));
			ideContribuinte.appendChild(tpInsc1);

			Element nrInsc1 = doc.createElement("nrInsc");
			nrInsc1.appendChild(doc.createTextNode("05424540000116"));
			ideContribuinte.appendChild(nrInsc1);

			Element eventos = doc.createElement("eventos");
			envioLoteEventos.appendChild(eventos);

			for (Entry<String, List<NotaFiscal10>> entry : notasPorCNPJ.entrySet()) {
				String cnpj = entry.getKey();
				List<NotaFiscal10> notas = entry.getValue();

				for (NotaFiscal10 nota : notas) {

					if (cnpj != cnpj8) {

						cnpj8 = nota.cnpj10();

						sequencia++;

						Element evento = doc.createElement("evento");
						evento.setAttribute("Id", "ID" + tipoInscricao + "05424540000116" + anomesdiahora + String.format("%05d", sequencia));
						eventos.appendChild(evento);

						Element reinf = doc.createElement("Reinf");
						reinf.setAttribute("xmlns","http://www.reinf.esocial.gov.br/schemas/evtTomadorServicos/v2_01_02");
						evento.appendChild(reinf);

						Element evtServTom = doc.createElement("evtServTom");
						evtServTom.setAttribute("id", "ID" + tipoInscricao + "05424540000116" + anomesdiahora + String.format("%05d", sequencia));
						reinf.appendChild(evtServTom);

						Element ideEvento = doc.createElement("ideEvento");
						evtServTom.appendChild(ideEvento);

						Element indRetif = doc.createElement("indRetif");
						indRetif.appendChild(doc.createTextNode("1"));
						ideEvento.appendChild(indRetif);

						double totalBruto = 0, totalBaseRet = 0, totalRetPrinc = 0;

						for (NotaFiscal10 nota1 : notas) {
							totalBruto += nota1.getVlrBruto();
							totalBaseRet += nota1.getVlrBaseRet();
							totalRetPrinc += nota1.getVlrRetencao();
							vIndObra = nota1.getindObra();
							vIndCprb = nota1.getindObra();
							dtStr2 = nota1.getDtEmissaoNF();

						}

						dtStr2 = nota.getDtEmissaoNF();
						Element perApur = doc.createElement("perApur");
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
						SimpleDateFormat dateFormatddmmaaaa = new SimpleDateFormat("dd/MM/yyyy");
						Date data = null;
						try {
							data = dateFormatddmmaaaa.parse(dtStr2);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						perApur.appendChild(doc.createTextNode(dateFormat.format(data)));
						ideEvento.appendChild(perApur);

						Element tpAmb = doc.createElement("tpAmb");
						tpAmb.appendChild(doc.createTextNode("1"));
						ideEvento.appendChild(tpAmb);

						Element procEmi = doc.createElement("procEmi");
						procEmi.appendChild(doc.createTextNode("1"));
						ideEvento.appendChild(procEmi);

						Element verProc = doc.createElement("verProc");
						verProc.appendChild(doc.createTextNode("v2_01_02"));
						ideEvento.appendChild(verProc);

						Element ideContri = doc.createElement("ideContri");
						evtServTom.appendChild(ideContri);

						Element tpInsc = doc.createElement("tpInsc");
						tpInsc.appendChild(doc.createTextNode(tipoInscricao));
						ideContri.appendChild(tpInsc);

						Element nrInsc = doc.createElement("nrInsc");
						nrInsc.appendChild(doc.createTextNode("05424540000116"));
						ideContri.appendChild(nrInsc);

						Element infoServTom = doc.createElement("infoServTom");
						evtServTom.appendChild(infoServTom);

						Element ideEstabObra = doc.createElement("ideEstabObra");
						infoServTom.appendChild(ideEstabObra);

						Element tpInscEstab = doc.createElement("tpInscEstab");
						tpInscEstab.appendChild(doc.createTextNode("1"));
						ideEstabObra.appendChild(tpInscEstab);

						Element nrInscEstab = doc.createElement("nrInscEstab");
						nrInscEstab.appendChild(doc.createTextNode("05424540000116"));
						ideEstabObra.appendChild(nrInscEstab);

						Element indObra = doc.createElement("indObra");
						indObra.appendChild(doc.createTextNode("0"));
						ideEstabObra.appendChild(indObra);

						Element idePrestServ = doc.createElement("idePrestServ");
						ideEstabObra.appendChild(idePrestServ);

						Element cnpjPrestador = doc.createElement("cnpjPrestador");
						cnpjPrestador.appendChild(doc.createTextNode(nota.cnpj10()));
						idePrestServ.appendChild(cnpjPrestador);

						DecimalFormat decimalFormat = new DecimalFormat("##0.00");

						Element vlrTotalBruto = doc.createElement("vlrTotalBruto");
						String valorBaseFormatado = decimalFormat.format(totalBruto);
						vlrTotalBruto.appendChild(doc.createTextNode(valorBaseFormatado));
						idePrestServ.appendChild(vlrTotalBruto);

						Element vlrTotalBaseRet = doc.createElement("vlrTotalBaseRet");
						String valorBaseFormatado3 = decimalFormat.format(totalBaseRet);
						vlrTotalBaseRet.appendChild(doc.createTextNode(valorBaseFormatado3));
						idePrestServ.appendChild(vlrTotalBaseRet);

						Element vlrTotalRetPrinc = doc.createElement("vlrTotalRetPrinc");
						String valorBaseFormatado4 = decimalFormat.format(totalRetPrinc);
						vlrTotalRetPrinc.appendChild(doc.createTextNode(valorBaseFormatado4));
						idePrestServ.appendChild(vlrTotalRetPrinc);

						Element vlrTotalRetAdic = doc.createElement("vlrTotalRetAdic");
						vlrTotalRetAdic.appendChild(doc.createTextNode("0,00"));
						idePrestServ.appendChild(vlrTotalRetAdic);

						Element vlrTotalNRetPrinc = doc.createElement("vlrTotalNRetPrinc");
						vlrTotalNRetPrinc.appendChild(doc.createTextNode("0,00"));
						idePrestServ.appendChild(vlrTotalNRetPrinc);

						Element vlrTotalNRetAdic = doc.createElement("vlrTotalNRetAdic");
						vlrTotalNRetAdic.appendChild(doc.createTextNode("0,00"));
						idePrestServ.appendChild(vlrTotalNRetAdic);

						Element indCPRB = doc.createElement("indCPRB");
						indCPRB.appendChild(doc.createTextNode(vIndCprb.isEmpty() ? "0" : "1"));
						idePrestServ.appendChild(indCPRB);

						// Adiciona as notas fiscais
						for (NotaFiscal10 nota1 : notas) {

							Element nfs = doc.createElement("nfs");
							idePrestServ.appendChild(nfs);

							Element serie = doc.createElement("serie");
							serie.appendChild(doc.createTextNode(nota1.getSerie()));
							nfs.appendChild(serie);

							Element numDocto = doc.createElement("numDocto");
							numDocto.appendChild(doc.createTextNode(nota1.getNumDocto()));
							nfs.appendChild(numDocto);

							dtStr2 = nota1.getDtEmissaoNF();
							Element dtEmissaoNF = doc.createElement("dtEmissaoNF");
							SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
							Date data1 = null;
							try {
								data1 = dateFormatddmmaaaa.parse(dtStr2);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							dtEmissaoNF.appendChild(doc.createTextNode(dateFormat1.format(data1)));
							nfs.appendChild(dtEmissaoNF);

							Element vlrBruto = doc.createElement("vlrBruto");
							String valorBrutoFormatado = decimalFormat.format(nota1.getVlrBruto());
							vlrBruto.appendChild(doc.createTextNode(valorBrutoFormatado));
							nfs.appendChild(vlrBruto);

							Element infoTpServ = doc.createElement("infoTpServ");
							nfs.appendChild(infoTpServ);

							Element tpServico = doc.createElement("tpServico");
							tpServico.appendChild(doc.createTextNode(nota1.getTpServico()));
							infoTpServ.appendChild(tpServico);

							Element vlrBaseRet = doc.createElement("vlrBaseRet");
							String valorBaseFormatado2 = decimalFormat.format(nota1.getVlrBaseRet());
							vlrBaseRet.appendChild(doc.createTextNode(valorBaseFormatado2));
							infoTpServ.appendChild(vlrBaseRet);

							Element vlrRetencao = doc.createElement("vlrRetencao");
							String valorBaseFormatado1 = decimalFormat.format(nota1.getVlrRetencao());
							vlrRetencao.appendChild(doc.createTextNode(valorBaseFormatado1));
							infoTpServ.appendChild(vlrRetencao);

						}
					}
				}

			}
			// Escreve o conte�do no arquivo XML
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// File outputFile = new File(outputFolder + "xml_" + cnpj + ".xml");
			File outputFile = new File(outputFolder + cnpj1 + anomesdia + "T" + horaminutoseg + "-reinf-loteevt.xml");
			StreamResult result = new StreamResult(outputFile);

			transformer.transform(source, result);
		} catch (ParserConfigurationException | TransformerException pce) {
			pce.printStackTrace();
		}
	}

}

class NotaFiscal10 {
	private String cnpj10;
    private String indObra;
	private String serie;
    private String numDocto;
    private String dtEmissaoNF;
    private double vlrBruto;
    private String tpServico;
    private double vlrBaseRet;
    private double vlrRetencao;

    public NotaFiscal10(String cnpj10, String indObra, String serie, String numDocto, String dtEmissaoNF, double vlrBruto, String tpServico, double vlrBaseRet, double vlrRetencao) {
    	this.cnpj10 = cnpj10;
    	this.indObra = indObra;
    	this.serie = serie;
        this.numDocto = numDocto;
        this.dtEmissaoNF = dtEmissaoNF;
        this.vlrBruto = vlrBruto;
        this.tpServico = tpServico;
        this.vlrBaseRet = vlrBaseRet;
        this.vlrRetencao = vlrRetencao;
    }

    public String cnpj10() {
        return cnpj10;
    }
    
    public String getindObra() {
        return indObra;
    }
   
    public String getSerie() {
        return serie;
    }

    public String getNumDocto() {
        return numDocto;
    }

    public String getDtEmissaoNF() {
        return dtEmissaoNF;
    }

    public double getVlrBruto() {
        return vlrBruto;
    }

    public String getTpServico() {
        return tpServico;
    }

    public double getVlrBaseRet() {
        return vlrBaseRet;
    }

    public double getVlrRetencao() {
        return vlrRetencao;
    }
}
