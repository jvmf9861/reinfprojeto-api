package xmlprojeto.beans;

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

public class R4010cnpj32243347000151 {

	public static String tipoInscricao;
	public static String anomesdiahora;
	public static String anomesdia;
	public static String horaminutoseg;
	public static String tipoIndice;
	public static String vnatur;
	public static String dtStr2;
	private static String cnpj8;
	public static SimpleDateFormat dateFormat;
	public static int sequencia=0;
	private static String cnpj1="32243347000151";
	
    public static void main(String[] args, Object selectedEvent) {
    	
    	R4010cnpj32243347000151.sequencia = 0;
    	
        String excelFilePath = "C:\\projetoxml\\Extração SIAFI-Web Dedução DDF025.xlsx";
        String outputFolder = "C:\\projetoxml\\";
        
        try {
            FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            Map<String, List<NaturezaRend11>> notasPorCNPJ = new HashMap<>();

            // Ler o arquivo Excel e agrupar as notas fiscais por CNPJ
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 0) continue; // Ignorar cabeçalho
                
                String situacao = getStringValue(currentRow.getCell(5)).trim();
                String cnpj = getStringValue(currentRow.getCell(6));

                //if (situacao.equals("DDF009")) {
    			if (situacao.equals(selectedEvent)) {

                    NaturezaRend11 nota = new NaturezaRend11(
                        getStringValue(currentRow.getCell(6)),    
                        getStringValue(currentRow.getCell(8)),  
                        getDateValue(currentRow.getCell(7)), 
                        getNumericValue(currentRow.getCell(9)), 
                        getNumericValue(currentRow.getCell(9)), 
                        getNumericValue(currentRow.getCell(10)),
                        getStringValue(currentRow.getCell(13))
                    );

                    if (notasPorCNPJ.containsKey(cnpj)) {
                        notasPorCNPJ.get(cnpj).add(nota);
                    } else {
                        List<NaturezaRend11> notas = new ArrayList<>();
                        notas.add(nota);
                        notasPorCNPJ.put(cnpj, notas);
                    }
                }
            }

            workbook.close();
            inputStream.close();

            // Gerar XML para o lote de eventos
            generateLoteXML(notasPorCNPJ, outputFolder);

            System.out.println("Arquivos XMLs do Evento R-4010, gerados com sucesso.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getStringValue(Cell cell) {
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

    private static void generateLoteXML(Map<String, List<NaturezaRend11>> notasPorCNPJ, String outputFolder) {
    	
    	try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
  
            // Elementos XML
            tipoInscricao = "1";
            anomesdiahora = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            anomesdia = new SimpleDateFormat("yyyyMMdd").format(new Date());
            horaminutoseg = new SimpleDateFormat("HHmmss").format(new Date());

            
            Document doc = docBuilder.newDocument();
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
            nrInsc1.appendChild(doc.createTextNode("32243347000151"));
            ideContribuinte.appendChild(nrInsc1);

        	Element eventos = doc.createElement("eventos");
        	envioLoteEventos.appendChild(eventos);

         for (Entry<String, List<NaturezaRend11>> entry : notasPorCNPJ.entrySet()) {
                String cnpj = entry.getKey();
                List<NaturezaRend11> notas = entry.getValue();

          for (NaturezaRend11 nota : notas) {    
        	   
           if(cnpj != cnpj8) {
              	
            	cnpj8=nota.cnpj10(); 
            	
            	sequencia++;
        	
            Element evento = doc.createElement("evento");
            evento.setAttribute("Id", "ID" + tipoInscricao + "32243347000151" +anomesdiahora+String.format("%05d", sequencia));
            eventos.appendChild(evento);

            Element reinf = doc.createElement("Reinf");
            reinf.setAttribute("xmlns", "http://www.reinf.esocial.gov.br/schemas/evt4010PagtoBeneficiarioPF/v2_01_02");
            evento.appendChild(reinf);
            
            Element evtRetPF = doc.createElement("evtRetPF");
            evtRetPF.setAttribute("id", "ID" + tipoInscricao + "32243347000151" +anomesdiahora+String.format("%05d", sequencia));
            reinf.appendChild(evtRetPF);
       
        	Element ideEvento = doc.createElement("ideEvento");
        	evtRetPF.appendChild(ideEvento);

            Element indRetif = doc.createElement("indRetif");
            indRetif.appendChild(doc.createTextNode("1"));
            ideEvento.appendChild(indRetif);
            
            for (NaturezaRend11 nota1 : notas) {
                dtStr2 = nota1.getdtFG();

            }    

            dtStr2 = nota.getdtFG();
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
                tpAmb.appendChild(doc.createTextNode("2"));
                ideEvento.appendChild(tpAmb);

                Element procEmi = doc.createElement("procEmi");
                procEmi.appendChild(doc.createTextNode("1"));
                ideEvento.appendChild(procEmi);

                Element verProc = doc.createElement("verProc");
                verProc.appendChild(doc.createTextNode("v2_01_02"));
                ideEvento.appendChild(verProc);

                Element ideContri = doc.createElement("ideContri");
                evtRetPF.appendChild(ideContri);

                Element tpInsc = doc.createElement("tpInsc");
                tpInsc.appendChild(doc.createTextNode("1"));
                ideContri.appendChild(tpInsc);

                Element nrInsc = doc.createElement("nrInsc");
                nrInsc.appendChild(doc.createTextNode("32243347000151"));
                ideContri.appendChild(nrInsc);
     
                Element ideEstab = doc.createElement("ideEstab");
                evtRetPF.appendChild(ideEstab);

                Element tpInscEstab = doc.createElement("tpInscEstab");
                tpInscEstab.appendChild(doc.createTextNode("1"));
                ideEstab.appendChild(tpInscEstab);

                Element nrInscEstab = doc.createElement("nrInscEstab");
                nrInscEstab.appendChild(doc.createTextNode("32243347000151"));
                ideEstab.appendChild(nrInscEstab);
         
                Element ideBenef = doc.createElement("ideBenef");
                ideEstab.appendChild(ideBenef);

                Element cpfBenef = doc.createElement("cpfBenef");
                cpfBenef.appendChild(doc.createTextNode(cnpj));
                ideBenef.appendChild(cpfBenef);
            
                Element idePgto = doc.createElement("idePgto");
                ideBenef.appendChild(idePgto);
  
                for (NaturezaRend11 nota1 : notas) {
                    vnatur = nota1.getnatRend();
                }    
             
                Element natRend = doc.createElement("natRend");
           	    natRend.appendChild(doc.createTextNode(vnatur));
                idePgto.appendChild(natRend);
                
                
                // Adiciona as notas fiscais
               for (NaturezaRend11 nota1 : notas) {
                
    	            Element infoPgto = doc.createElement("infoPgto");
    	            idePgto.appendChild(infoPgto);
    	        
                    dtStr2 = nota1.getdtFG();
                    Element dtFG = doc.createElement("dtFG");
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date data1 = null;
					   try {
						   data1 = dateFormatddmmaaaa.parse(dtStr2);
					   } catch (ParseException e) {
				        	e.printStackTrace();
				       }
                    dtFG.appendChild(doc.createTextNode(dateFormat1.format(data1)));
                    infoPgto.appendChild(dtFG);
    	            DecimalFormat decimalFormat = new DecimalFormat("##0.00");
 
    	            Element vlrRendBruto = doc.createElement("vlrRendBruto");
   	                String valorBrutoFormatado = decimalFormat.format(nota1.getVlrRendBruto());
    	            vlrRendBruto.appendChild(doc.createTextNode(valorBrutoFormatado));
    	            infoPgto.appendChild(vlrRendBruto);        
                 
    	            Element vlrRendTrib = doc.createElement("vlrRendTrib");
    	            String valorBaseFormatado = decimalFormat.format(nota1.getvlrRendTrib());
    	            vlrRendTrib.appendChild(doc.createTextNode(valorBaseFormatado));
    	            infoPgto.appendChild(vlrRendTrib);

                    Element vlrIR = doc.createElement("vlrIR");
                    String valorBaseFormatado1 = decimalFormat.format(nota1.getvlrIR());
                    vlrIR.appendChild(doc.createTextNode(valorBaseFormatado1));
                    infoPgto.appendChild(vlrIR);
                
         	        Element indJud = doc.createElement("indJud");
         	        indJud.appendChild(doc.createTextNode("N"));
         	        infoPgto.appendChild(indJud);
         	        
         	        Element observ = doc.createElement("observ");
					observ.appendChild(doc.createTextNode(nota1.getobsDoc()));
					infoPgto.appendChild(observ);
                
               }
             }
          }
         } 
            // Escreve o conteudo no arquivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File outputFile = new File(outputFolder + cnpj1 + anomesdia + "T" + horaminutoseg +"-reinf-loteevt.xml");
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);
            
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
       }
    }
}


class NaturezaRend11{
	
	private String cnpj10;
    private String natRend;
    private String dtFG;
    private double vlrRendBruto;
    private double vlrRendTrib;
    private double vlrIR;
    private String obsDoc;

    public NaturezaRend11(String cnpj10, String natRend, String dtFG, double vlrRendBruto, double vlrRendTrib, double vlrIR, String obsDoc) {
    	this.cnpj10 = cnpj10;
    	this.natRend = natRend;
    	this.dtFG = dtFG;
        this.vlrRendBruto = vlrRendBruto;
        this.vlrRendTrib = vlrRendTrib;
        this.vlrIR = vlrIR;
        this.obsDoc = obsDoc;
    }

    public String cnpj10() {
        return cnpj10;
    }
    
    public String getnatRend() {
        return natRend;
    }
   
    
    public String getdtFG() {
        return dtFG;
    }

    public double getVlrRendBruto() {
        return vlrRendBruto;
    }


    public double getvlrRendTrib() {
        return vlrRendTrib;
    }

    public double getvlrIR() {
        return vlrIR;
    }
    
	public String getobsDoc() {
		return obsDoc;
	}
	
	
}

