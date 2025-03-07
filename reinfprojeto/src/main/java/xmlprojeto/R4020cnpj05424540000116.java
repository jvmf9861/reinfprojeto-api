package xmlprojeto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

//import xmlprojeto.models.Record;

//import xmlprojeto.models.Record;

public class R4020cnpj05424540000116 {

	private static String tipoInscricao;
	public static String tipoInscricao1;
	private static String anomesdiahora;
	private static String anomesdia;
	private static String horaminutoseg;
	public static String tipoIndice;
	private static String dtStr2;
	private static String natureza;
	private static String cnpj8;
	private static String cnpjant;
	public static double valorBaseCsll, valorCsll, valorBaseCofins, valorCofins, valorBasePP, valorPP;
	public static double valorBaseIR, valorIR;
	private static int contador = 0;
	private static int sequencia = 0;
	private static String cnpj1 = "05424540000116";

	public static void main(String[] args) {

		String excelFilePath = "C:\\projetoxml\\Extração SIAFI-Web Dedução DDF025.xlsx";
		String outputFolder = "C:\\projetoxml\\";

		try {
			FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();

			Map<String, List<NaturezaRendimento10>> notasPorCNPJ = new HashMap<>();

			// Ler o arquivo Excel e agrupar as notas fiscais por CNPJ
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				if (currentRow.getRowNum() == 0)
					continue; // Ignorar cabe�alho

				String situacao = getStringValue(currentRow.getCell(5)).trim();
				String cnpj = getStringValue(currentRow.getCell(6)).trim();

				if (situacao.equals("DDF025")) {
					NaturezaRendimento10 nota = new NaturezaRendimento10(
							getStringValue(currentRow.getCell(6)),
							getStringValue(currentRow.getCell(8)),
							getDateValue(currentRow.getCell(7)),
							getNumericValue(currentRow.getCell(9)),
							getNumericValue(currentRow.getCell(9)),
							getNumericValue(currentRow.getCell(10)),
							getStringValue(currentRow.getCell(14)),
							getStringValue(currentRow.getCell(13))
					);
					

					if (notasPorCNPJ.containsKey(cnpj)) {
						notasPorCNPJ.get(cnpj).add(nota);
					} else {
						List<NaturezaRendimento10> notas = new ArrayList<>();
						notas.add(nota);
						notasPorCNPJ.put(cnpj, notas);
					}
					
					System.out.println(nota.cnpj10());
				}
			}

			// Ordena por cnpj + Natureza de Rendimento

			for (List<NaturezaRendimento10> notas : notasPorCNPJ.values()) {
				Collections.sort(notas, new Comparator<NaturezaRendimento10>() {
					@Override
					public int compare(NaturezaRendimento10 nota1, NaturezaRendimento10 nota2) {

						int compareCol7 = nota1.cnpj10().compareTo(nota2.cnpj10());
						if (compareCol7 != 0) {
							return compareCol7;
						}
						return nota1.getnatRend().compareTo(nota2.getnatRend());

					}
				});

			}

			workbook.close();
			inputStream.close();

			// Gerar XML para o lote de eventos
			generateLoteXML(notasPorCNPJ, outputFolder);

			System.out.println("Arquivo XML 4020, gerado com sucesso.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getStringValue(Cell cell) {
		if (cell == null) {
			return "";
		} else if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue());
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

	private static void generateLoteXML(Map<String, List<NaturezaRendimento10>> notasPorCNPJ, String outputFolder) {

		try {

			tipoInscricao = "1";
			anomesdiahora = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			anomesdia = new SimpleDateFormat("yyyyMMdd").format(new Date());
			horaminutoseg = new SimpleDateFormat("HHmmss").format(new Date());

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

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
			nrInsc1.appendChild(doc.createTextNode("05424540000116"));
			ideContribuinte.appendChild(nrInsc1);

			Element eventos = doc.createElement("eventos");
			envioLoteEventos.appendChild(eventos);

			for (Entry<String, List<NaturezaRendimento10>> entry : notasPorCNPJ.entrySet()) {
				String cnpj = entry.getKey();
				List<NaturezaRendimento10> notas = entry.getValue();

				for (NaturezaRendimento10 nota : notas) {

					if (cnpj8 == null || !cnpj.equals(cnpj8)) {

						cnpj8 = nota.cnpj10();

						sequencia++;

						Element evento = doc.createElement("evento");
						evento.setAttribute("Id", "ID" + tipoInscricao + "05424540000116" + anomesdiahora
								+ String.format("%05d", sequencia));

						eventos.appendChild(evento);

						Element reinf = doc.createElement("Reinf");
						reinf.setAttribute("xmlns",
								"http://www.reinf.esocial.gov.br/schemas/evt4020PagtoBeneficiarioPJ/v2_01_02");
						evento.appendChild(reinf);

						Element evtRetPJ = doc.createElement("evtRetPJ");
						evtRetPJ.setAttribute("id", "ID" + tipoInscricao + "05424540000116" + anomesdiahora
								+ String.format("%05d", sequencia));

						reinf.appendChild(evtRetPJ);

						Element ideEvento = doc.createElement("ideEvento");
						evtRetPJ.appendChild(ideEvento);

						Element indRetif = doc.createElement("indRetif");
						indRetif.appendChild(doc.createTextNode("1"));
						ideEvento.appendChild(indRetif);

						double valorBaseCsll = 0, valorCsll = 0, valorBaseCofins = 0, valorCofins = 0, valorBasePP = 0,
								valorPP = 0;
						double valorBaseIR = 0, valorIR = 0;

						dtStr2 = nota.getdtFG();

						for (NaturezaRendimento10 nota1 : notas) {

							if (nota1.getcodDarf().equals("6228")) {

								valorBaseCsll = nota1.getVlrBruto();
								valorCsll = nota1.getvlrAgreg();

							}

							if (nota1.getcodDarf().equals("6230")) {

								valorBasePP = nota1.getVlrBruto();
								valorPP = nota1.getvlrAgreg();

							}

							if (nota1.getcodDarf().equals("6243")) {

								valorBaseCofins = nota1.getVlrBruto();
								valorCofins = nota1.getvlrAgreg();

							}

							if (nota1.getcodDarf().equals("6256")) {

								valorBaseIR = nota1.getVlrBruto();
								valorIR = nota1.getvlrAgreg();

							}

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
						evtRetPJ.appendChild(ideContri);

						Element tpInsc = doc.createElement("tpInsc");
						tpInsc.appendChild(doc.createTextNode("1"));
						ideContri.appendChild(tpInsc);

						Element nrInsc = doc.createElement("nrInsc");
						nrInsc.appendChild(doc.createTextNode("05424540000116"));
						ideContri.appendChild(nrInsc);

						Element ideEstab = doc.createElement("ideEstab");
						evtRetPJ.appendChild(ideEstab);

						Element tpInscEstab = doc.createElement("tpInscEstab");
						tpInscEstab.appendChild(doc.createTextNode("1"));
						ideEstab.appendChild(tpInscEstab);

						Element nrInscEstab = doc.createElement("nrInscEstab");
						nrInscEstab.appendChild(doc.createTextNode("05424540000116"));
						ideEstab.appendChild(nrInscEstab);

						Element ideBenef = doc.createElement("ideBenef");
						ideEstab.appendChild(ideBenef);

						Element cnpjBenef = doc.createElement("cnpjBenef");
						cnpjBenef.appendChild(doc.createTextNode(cnpj));
						ideBenef.appendChild(cnpjBenef);

						// Adiciona as notas fiscais

						Element idePgto = null;

						for (NaturezaRendimento10 nota2 : notas) {

							if (cnpj.equals("33352394000104") || cnpj.equals("34028316000294")) {

								contador++;

								if (contador < 2) {

									idePgto = doc.createElement("idePgto");
									ideBenef.appendChild(idePgto);

									Element natRend = doc.createElement("natRend");
									natRend.appendChild(doc.createTextNode(nota2.getnatRend()));
									idePgto.appendChild(natRend);

									Element infoPgto = doc.createElement("infoPgto");
									idePgto.appendChild(infoPgto);

									dtStr2 = nota2.getdtFG();
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

									Element vlrBruto = doc.createElement("vlrBruto");
									String valorBrutoFormatado = decimalFormat.format(nota2.getVlrBruto());
									vlrBruto.appendChild(doc.createTextNode(valorBrutoFormatado));
									infoPgto.appendChild(vlrBruto);

									Element indJud = doc.createElement("indJud");
									indJud.appendChild(doc.createTextNode("N"));
									infoPgto.appendChild(indJud);
									
									Element observ = doc.createElement("observ");
									observ.appendChild(doc.createTextNode(nota2.getobsDoc()));
									infoPgto.appendChild(observ);

									Element retencoes = doc.createElement("retencoes");
									infoPgto.appendChild(retencoes);

									Element vlrBaseCSLL = doc.createElement("vlrBaseCSLL");
									String valorBaseFormatado = decimalFormat.format(valorBaseCsll);
									vlrBaseCSLL.appendChild(doc.createTextNode(valorBaseFormatado));
									retencoes.appendChild(vlrBaseCSLL);

									Element vlrCSLL = doc.createElement("vlrCSLL");
									String valorBaseFormatado1 = decimalFormat.format(valorCsll);
									vlrCSLL.appendChild(doc.createTextNode(valorBaseFormatado1));
									retencoes.appendChild(vlrCSLL);

									Element vlrBaseCofins = doc.createElement("vlrBaseCofins");
									String valorBaseFormatado2 = decimalFormat.format(valorBaseCofins);
									vlrBaseCofins.appendChild(doc.createTextNode(valorBaseFormatado2));
									retencoes.appendChild(vlrBaseCofins);

									Element vlrCofins = doc.createElement("vlrCofins");
									String valorBaseFormatado3 = decimalFormat.format(valorCofins);
									vlrCofins.appendChild(doc.createTextNode(valorBaseFormatado3));
									retencoes.appendChild(vlrCofins);

									Element vlrBasePP = doc.createElement("vlrBasePP");
									String valorBaseFormatado4 = decimalFormat.format(valorBasePP);
									vlrBasePP.appendChild(doc.createTextNode(valorBaseFormatado4));
									retencoes.appendChild(vlrBasePP);

									Element vlrPP = doc.createElement("vlrPP");
									String valorBaseFormatado5 = decimalFormat.format(valorPP);
									vlrPP.appendChild(doc.createTextNode(valorBaseFormatado5));
									retencoes.appendChild(vlrPP);

								}

							} else if (nota2.getcodDarf().equals("6228") || nota2.getcodDarf().equals("6256")) {

								if (cnpj != cnpjant) {

									idePgto = doc.createElement("idePgto");
									ideBenef.appendChild(idePgto);

									Element natRend = doc.createElement("natRend");
									natRend.appendChild(doc.createTextNode(nota2.getnatRend()));
									idePgto.appendChild(natRend);

									Element infoPgto = doc.createElement("infoPgto");
									idePgto.appendChild(infoPgto);

									Element dtFG = doc.createElement("dtFG");
									SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
									SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
									Date data1 = null;
									try {
										data1 = dateFormat2.parse(nota2.getdtFG());
									} catch (ParseException e) {

										e.printStackTrace();
									}
									dtFG.appendChild(doc.createTextNode(dateFormat1.format(data1)));
									infoPgto.appendChild(dtFG);

									DecimalFormat decimalFormat = new DecimalFormat("##0.00");

									Element vlrBruto = doc.createElement("vlrBruto");
									String valorBrutoFormatado = decimalFormat.format(nota2.getVlrBruto());
									vlrBruto.appendChild(doc.createTextNode(valorBrutoFormatado));
									infoPgto.appendChild(vlrBruto);

									Element indJud = doc.createElement("indJud");
									indJud.appendChild(doc.createTextNode("N"));
									infoPgto.appendChild(indJud);

									Element observ = doc.createElement("observ");
									observ.appendChild(doc.createTextNode(nota2.getobsDoc()));
									infoPgto.appendChild(observ);
									
									Element retencoes = doc.createElement("retencoes");
									infoPgto.appendChild(retencoes);

									Element vlrBaseIR = doc.createElement("vlrBaseIR");
									String valorBaseFormatado = decimalFormat.format(valorBaseIR);
									vlrBaseIR.appendChild(doc.createTextNode(valorBaseFormatado));
									retencoes.appendChild(vlrBaseIR);

									Element vlrIR = doc.createElement("vlrIR");
									String valorBaseFormatado1 = decimalFormat.format(valorIR);
									vlrIR.appendChild(doc.createTextNode(valorBaseFormatado1));
									retencoes.appendChild(vlrIR);

									Element vlrBaseCSLL = doc.createElement("vlrBaseCSLL");
									String valorBaseFormatado2 = decimalFormat.format(valorBaseCsll);
									vlrBaseCSLL.appendChild(doc.createTextNode(valorBaseFormatado2));
									retencoes.appendChild(vlrBaseCSLL);

									Element vlrCSLL = doc.createElement("vlrCSLL");
									String valorBaseFormatado3 = decimalFormat.format(valorCsll);
									vlrCSLL.appendChild(doc.createTextNode(valorBaseFormatado3));
									retencoes.appendChild(vlrCSLL);

									cnpjant = cnpj;
								}

							} else {

								if (cnpj != cnpjant) {

									natureza = "";
								}

								if (nota2.getnatRend() != natureza) {

									idePgto = doc.createElement("idePgto");
									ideBenef.appendChild(idePgto);

									Element natRend = doc.createElement("natRend");
									natRend.appendChild(doc.createTextNode(nota2.getnatRend()));
									idePgto.appendChild(natRend);

								}

								Element infoPgto = doc.createElement("infoPgto");

								if (idePgto != null) {

									idePgto.appendChild(infoPgto);

								}

								Element dtFG = doc.createElement("dtFG");
								SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
								SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
								Date data1 = null;
								try {
									data1 = dateFormat2.parse(nota2.getdtFG());
								} catch (ParseException e) {

									e.printStackTrace();
								}
								dtFG.appendChild(doc.createTextNode(dateFormat1.format(data1)));
								infoPgto.appendChild(dtFG);

								DecimalFormat decimalFormat = new DecimalFormat("##0.00");

								Element vlrBruto = doc.createElement("vlrBruto");
								String valorBrutoFormatado = decimalFormat.format(nota2.getVlrBruto());
								vlrBruto.appendChild(doc.createTextNode(valorBrutoFormatado));
								infoPgto.appendChild(vlrBruto);

								Element indJud = doc.createElement("indJud");
								indJud.appendChild(doc.createTextNode("N"));
								infoPgto.appendChild(indJud);
								
								Element observ = doc.createElement("observ");
								observ.appendChild(doc.createTextNode(nota2.getobsDoc()));
								infoPgto.appendChild(observ);

								Element retencoes = doc.createElement("retencoes");
								infoPgto.appendChild(retencoes);

								Element vlrBaseAgreg = doc.createElement("vlrBaseAgreg");
								String valorBaseFormatado = decimalFormat.format(nota2.getvlrBaseAgreg());
								vlrBaseAgreg.appendChild(doc.createTextNode(valorBaseFormatado));
								retencoes.appendChild(vlrBaseAgreg);

								Element vlrAgreg = doc.createElement("vlrAgreg");
								String valorBaseFormatado1 = decimalFormat.format(nota2.getvlrAgreg());
								vlrAgreg.appendChild(doc.createTextNode(valorBaseFormatado1));
								retencoes.appendChild(vlrAgreg);

								cnpjant = cnpj;
								natureza = nota2.getnatRend();

							}

						}

					}
				}

			}

			// Escrever o conte�do no arquivo XML
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			File outputFile = new File(outputFolder + cnpj1 + anomesdia + "T" + horaminutoseg + "-reinf-loteevt.xml");
			StreamResult result = new StreamResult(outputFile);
			transformer.transform(source, result);

		} catch (ParserConfigurationException | TransformerException pce) {
			pce.printStackTrace();
		}
	}

}

class NaturezaRendimento10 {
	private String cnpj10;
	private String natRend;
	private String dtFG;
	private double vlrBruto;
	private double vlrBaseAgreg;
	private double vlrAgreg;
	private String codDarf;
	private String obsDoc;

	public NaturezaRendimento10(String cnpj10, String natRend, String dtFG, double vlrBruto, double vlrBaseAgreg, double vlrAgreg, String codDarf, String obsDoc) {
		this.cnpj10 = cnpj10;
		this.natRend = natRend;
		this.dtFG = dtFG;
		this.vlrBruto = vlrBruto;
		this.vlrBaseAgreg = vlrBaseAgreg;
		this.vlrAgreg = vlrAgreg;
		this.codDarf = codDarf;
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
	
	public String getobsDoc() {
		return obsDoc;
	}


}
