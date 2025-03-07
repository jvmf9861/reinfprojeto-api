package xmlprojeto;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConsultaXml {
    public static void main(String[] args) {
    	
        String outputFolder = "C:\\projetoxml\\";
    	
        try {
            // Criação do documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();


            Element reinfElement = doc.createElement("Reinf");
            doc.appendChild(reinfElement);

            // Elemento <ConsultaLoteAssincrono> dentro de <Reinf>
            Element consultaLoteAssincronoElement = doc.createElement("ConsultaLoteAssincrono");
            reinfElement.appendChild(consultaLoteAssincronoElement);

            // Subelemento de <ConsultaLoteAssincrono>
            Element numeroProtocoloElement = doc.createElement("numeroProtocolo");
            numeroProtocoloElement.appendChild(doc.createTextNode("1.202405.93877241"));
            consultaLoteAssincronoElement.appendChild(numeroProtocoloElement);

            // Transformação do documento em arquivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);

            // Escreve o conteúdo no arquivo XML
            File outputFile = new File(outputFolder +  "ConsultaLoteAssincrono" +"-Reinf-consloteevt.xml");
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);

            System.out.println("Arquivo XML gerado com sucesso" );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
