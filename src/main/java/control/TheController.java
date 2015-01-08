package control;

import logic.classification.ClassificatorFactory;
import logic.classification.IClassificator;
import logic.recognition.CharacterRecognizerFactory;
import logic.recognition.ICharacterRecognizer;
import model.Receipt;
import model.ReceiptClass;
import model.ReceiptData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

//import org.lightcouch.CouchDbClient;

/**
 * TODO: try catch here, pdd rotate, cut
 */

@Controller
public class TheController {

    //@Autowired
    //private CouchDbClient dbClient;
    private static final Logger logger = LoggerFactory.getLogger(TheController.class);
    private IClassificator classificator = ClassificatorFactory.getClassificator();

    /**
     * route index page
     * @return index template to render
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dispatchIndex() {
        return "index";
    }

    /**
     * process search POST request
     * @param file uploaded image to process
     * @param model modelmap with template data to rende
     * @return result template to render
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String uploadFileHandler(@RequestParam("file") MultipartFile file, ModelMap model) {
        ReceiptData data = ReceiptData.loremIpsum();
        try {
            if (file.isEmpty())
                throw new IOException("File is empty.");
            Receipt receipt = new Receipt(file);
            // find logo
            BufferedImage logo = receipt.separateLogo();
            // classify logo
            ReceiptClass receiptClass = classificator.classify(logo);
            // ocr
            ICharacterRecognizer ocr = CharacterRecognizerFactory.dispatch(receiptClass);
            data = ocr.scan(receipt);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errmess", "this is fail");
            return "error";
        }
        model.addAttribute("data", data);
        return "result";
    }

    // ES, JEST
    //@RequestMapping("/testes")
    //public String dispatchTestEs(ModelMap model) throws Exception {
//
    //    JestClientFactory factory = new JestClientFactory();
    //    factory.setHttpClientConfig(new HttpClientConfig
    //            .Builder("http://95.80.240.24:9200")
    //            .multiThreaded(true)
    //            .build());
    //    JestClient client = factory.getObject();
//
    //    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    //    searchSourceBuilder.query(QueryBuilders.matchQuery("name", "Amit"));
//
    //    Search search = new Search.Builder(searchSourceBuilder.toString())
    //            .addIndex("es-cdb-cache")
    //            .build();
    //    SearchResult result = client.execute(search);
    //    System.out.println(result.getJsonString());
    //    return "browse";
    //}
//
    //// Lightcouch
    //// http://maythesource.com/?p=148
    //// http://bit.ly/1yDyo5H
    //@RequestMapping("/testcdb")
    //public String dispatchTestCdb(ModelMap model) throws MalformedURLException {
//
    //    CouchDbInfo dbInfo = dbClient.context().info();
    //    JsonObject object = new JsonObject();
    //    object.addProperty("name","test");
    //    Response resp;
//
    //    try {
    //        resp = dbClient.save(object);
    //    } catch (org.lightcouch.DocumentConflictException e) {
    //        //if we insert something that already exists
    //        //we get Exception in thread "main" org.lightcouch.DocumentConflictException: << Status: 409 (Conflict)
    //    }
//
    //    model.addAttribute("res", dbInfo.getDbName());
    //    return "browse";
    //}

}