package xmlprojeto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class R1000cnpj05424540000116 {
	
	public static String tipoInscricao;
	public static String anomesdiahora;
	
    public static void main(String[] args) {
    	
        String outputFolder = "C:\\projetoxml\\";
    	
        try {
            // Cria��o do documento XML

            tipoInscricao = "1";
            anomesdiahora = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        	
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
                        
            Element reinfElement = doc.createElementNS("http://www.reinf.esocial.gov.br/schemas/envioLoteEventosAssincrono/v1_00_00", "Reinf");
            doc.appendChild(reinfElement);

            Element envioLoteEventosElement = doc.createElement("envioLoteEventos");
            reinfElement.appendChild(envioLoteEventosElement);

            Element ideContribuinteElement = doc.createElement("ideContribuinte");
            envioLoteEventosElement.appendChild(ideContribuinteElement);

            Element tpInscElement = doc.createElement("tpInsc");
            tpInscElement.appendChild(doc.createTextNode("1"));
            ideContribuinteElement.appendChild(tpInscElement);

            Element nrInscElement = doc.createElement("nrInsc");
            nrInscElement.appendChild(doc.createTextNode("05424540000116"));
            ideContribuinteElement.appendChild(nrInscElement);

            Element eventosElement = doc.createElement("eventos");
            envioLoteEventosElement.appendChild(eventosElement);

            Element eventoElement = doc.createElement("evento");
            eventoElement.setAttribute("Id", "ID" + tipoInscricao + "05424540000116" +anomesdiahora+"00001");
            eventosElement.appendChild(eventoElement);

            Element reinfInnerElement = doc.createElementNS("http://www.reinf.esocial.gov.br/schemas/evtInfoContribuinte/v2_01_02", "Reinf");
            eventoElement.appendChild(reinfInnerElement);

            Element evtInfoContriElement = doc.createElement("evtInfoContri");
            evtInfoContriElement.setAttribute("id", "ID" + tipoInscricao + "05424540000116" +anomesdiahora+"00001");
            reinfInnerElement.appendChild(evtInfoContriElement);
            
            Element ideEventoElement = doc.createElement("ideEvento");
            evtInfoContriElement.appendChild(ideEventoElement);
            
            Element tpAmbElement = doc.createElement("tpAmb");
            tpAmbElement.appendChild(doc.createTextNode("2"));
            ideEventoElement.appendChild(tpAmbElement);
       
            Element procEmiElement = doc.createElement("procEmi");
            procEmiElement.appendChild(doc.createTextNode("1"));
            ideEventoElement.appendChild(procEmiElement);
            
            Element verProcElement = doc.createElement("verProc");
            verProcElement.appendChild(doc.createTextNode("REINF.Web"));
            ideEventoElement.appendChild(verProcElement);

            Element ideContriElement = doc.createElement("ideContri");
            evtInfoContriElement.appendChild(ideContriElement);

            Element tpInsc1Element = doc.createElement("tpInsc");
            tpInsc1Element.appendChild(doc.createTextNode("1"));
            ideContriElement.appendChild(tpInsc1Element);

            Element nrInsc1Element = doc.createElement("nrInsc");
            nrInsc1Element.appendChild(doc.createTextNode("05424540000116"));
            ideContriElement.appendChild(nrInsc1Element);

            Element infoContriElement = doc.createElement("infoContri");
            evtInfoContriElement.appendChild(infoContriElement);

            Element inclusaoElement = doc.createElement("alteracao");
            infoContriElement.appendChild(inclusaoElement);

            Element idePeriodoElement = doc.createElement("idePeriodo");
            inclusaoElement.appendChild(idePeriodoElement);

            Element iniValidElement = doc.createElement("iniValid");
            iniValidElement.appendChild(doc.createTextNode("2022-10"));
            idePeriodoElement.appendChild(iniValidElement);

           // Element fimValidElement = doc.createElement("fimValid");
          //  fimValidElement.appendChild(doc.createTextNode("2023-09"));
          //  idePeriodoElement.appendChild(fimValidElement);

            Element infoCadastroElement = doc.createElement("infoCadastro");
            inclusaoElement.appendChild(infoCadastroElement);

            Element classTribElement = doc.createElement("classTrib");
            classTribElement.appendChild(doc.createTextNode("85"));
            infoCadastroElement.appendChild(classTribElement);

            Element indEscrituracaoElement = doc.createElement("indEscrituracao");
            indEscrituracaoElement.appendChild(doc.createTextNode("1"));
            infoCadastroElement.appendChild(indEscrituracaoElement);
            
            Element indDesoneracaoElement = doc.createElement("indDesoneracao");
            indDesoneracaoElement.appendChild(doc.createTextNode("0"));
            infoCadastroElement.appendChild(indDesoneracaoElement);

            Element indAcordoIsenMultaElement = doc.createElement("indAcordoIsenMulta");
            indAcordoIsenMultaElement.appendChild(doc.createTextNode("0"));
            infoCadastroElement.appendChild(indAcordoIsenMultaElement);

            Element indSitPJElement = doc.createElement("indSitPJ");
            indSitPJElement.appendChild(doc.createTextNode("0"));
            infoCadastroElement.appendChild(indSitPJElement);
            
            Element indUniaoElement = doc.createElement("indUniao");
            indUniaoElement.appendChild(doc.createTextNode("0"));
            infoCadastroElement.appendChild(indUniaoElement);
      
            
            Element contatoElement = doc.createElement("contato");
            infoCadastroElement.appendChild(contatoElement);
            
            Element nmCttElement = doc.createElement("nmCtt");
            nmCttElement.appendChild(doc.createTextNode("MARTHA KINACH RODRIGUES LIMA"));
            contatoElement.appendChild(nmCttElement);

            Element cpfCttElement = doc.createElement("cpfCtt");
            cpfCttElement.appendChild(doc.createTextNode("01837085714"));
            contatoElement.appendChild(cpfCttElement);

            Element foneFixoElement = doc.createElement("foneFixo");
            foneFixoElement.appendChild(doc.createTextNode("2132189730"));
            contatoElement.appendChild(foneFixoElement);

            Element foneCelElement = doc.createElement("foneCel");
            foneCelElement.appendChild(doc.createTextNode("21986491981"));
            contatoElement.appendChild(foneCelElement);
     
            Element emailElement = doc.createElement("email");
            emailElement.appendChild(doc.createTextNode("tssof@jfrj.jus.br"));
            contatoElement.appendChild(emailElement);
        
            Element infoEFRElement = doc.createElement("infoEFR");
            infoCadastroElement.appendChild(infoEFRElement);

            Element ideEFRElement = doc.createElement("ideEFR");
            ideEFRElement.appendChild(doc.createTextNode("N"));
            infoEFRElement.appendChild(ideEFRElement);

            Element cnpjEFRElement = doc.createElement("cnpjEFR");
            cnpjEFRElement.appendChild(doc.createTextNode("41414323000109"));
            infoEFRElement.appendChild(cnpjEFRElement);


            // Transforma��o do documento em arquivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);

            // Escreve o conte�do no arquivo XML
            File outputFile = new File(outputFolder +  "Evento1000" + "-Reinf-consloteevt.xml");
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);

            System.out.println("Arquivo XML gerado com sucesso" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
