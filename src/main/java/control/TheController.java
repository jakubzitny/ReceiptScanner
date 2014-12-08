package control;

import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import logic.classification.IClassificator;
import logic.classification.SurfSimpleIClassificator;
import logic.recognition.ICharacterRecognizer;
import logic.recognition.StarbucksCharacterRecognizer;
import model.Receipt;
import model.ReceiptClass;
import model.ReceiptData;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbInfo;
import org.lightcouch.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

@Controller
public class TheController {

    @Autowired
    private CouchDbClient dbClient;
    private static final Logger logger = LoggerFactory.getLogger(TheController.class);
    private IClassificator IClassificator = SurfSimpleIClassificator.create();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dispatchIndex() {
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam("file") MultipartFile file, ModelMap model) {
        ReceiptData data = ReceiptData.loremIpsum();
        try {
            if (file.isEmpty())
                throw new IOException("File is empty.");
            Receipt receipt = new Receipt(file);
            BufferedImage logo = receipt.separateLogo();
            ReceiptClass receiptClass = IClassificator.classify(logo);
            //ICharacterRecognizer ocr = CharacterRecognizerWrapper.dispatch(receiptClass);
            ICharacterRecognizer ocr = new StarbucksCharacterRecognizer();
            data = ocr.scan(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("data", data);
        return "result";
    }

    // ES, JEST
    @RequestMapping("/testes")
    public String dispatchTestEs(ModelMap model) throws Exception {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("http://95.80.240.24:9200")
                .multiThreaded(true)
                .build());
        JestClient client = factory.getObject();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "Amit"));

        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex("es-cdb-cache")
                .build();
        SearchResult result = client.execute(search);
        System.out.println(result.getJsonString());
        return "browse";
    }

    // Lightcouch
    // http://maythesource.com/?p=148
    // http://bit.ly/1yDyo5H
    @RequestMapping("/testcdb")
    public String dispatchTestCdb(ModelMap model) throws MalformedURLException {

        CouchDbInfo dbInfo = dbClient.context().info();
        JsonObject object = new JsonObject();
        object.addProperty("name","test");
        Response resp;

        try {
            resp = dbClient.save(object);
        } catch (org.lightcouch.DocumentConflictException e) {
            //if we insert something that already exists
            //we get Exception in thread "main" org.lightcouch.DocumentConflictException: << Status: 409 (Conflict)
        }

        model.addAttribute("res", dbInfo.getDbName());
        return "browse";
    }

}