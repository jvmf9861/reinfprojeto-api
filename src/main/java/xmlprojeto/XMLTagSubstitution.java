package xmlprojeto;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLTagSubstitution {
    public static void main(String[] args) {
        try {
            // Carregar o primeiro arquivo XML
            File file1 = new File("C:\\RetornoXml\\ConsultaLoteAssincrono-Reinf-consloteevt.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc1 = dBuilder.parse(file1);
            doc1.getDocumentElement().normalize();

            // Carregar o segundo arquivo XML
            File file2 = new File("C:\\RetornoXml\\0035229400011020240404T173131-ret-reinf-loteevt.xml");
            Document doc2 = dBuilder.parse(file2);
            doc2.getDocumentElement().normalize();
           
            // Encontrar a tag a ser substituída no primeiro arquivo
            NodeList nodeList = doc1.getElementsByTagName("numeroProtocolo");
            Node node1 = nodeList.item(0);

            // Encontrar o conteúdo da tag para substituição no segundo arquivo
            NodeList nodeList2 = doc2.getElementsByTagName("protocoloEnvio");     
            Node node2 = nodeList2.item(0);
            String conteudoSubstituto = node2.getTextContent();

            // Substituir o conteúdo
            node1.setTextContent(conteudoSubstituto);

            // Escrever o resultado no arquivo de saída
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc1);
            StreamResult result = new StreamResult(new File("C:\\RetornoXml\\saida.xml"));
            transformer.transform(source, result);

            System.out.println("Conteúdo substituído com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
