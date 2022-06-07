package com.esercizio.rest.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
 
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
 
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
 
public class TestDownloadFileService {
 
    public static final String DOWNLOAD_FILE_LOCATION = "D:/Demo/test/";
 
    public static void main(String []args) throws IOException {
 
        String httpURL = "<a class='vglnk' href='http://localhost:8080/Jersey-UP-DOWN-PDF-File/rest/fileservice/download/pdf' rel='nofollow'><span>http</span><span>://</span><span>localhost</span><span>:</span><span>8080</span><span>/</span><span>Jersey</span><span>-</span><span>UP</span><span>-</span><span>DOWN</span><span>-</span><span>PDF</span><span>-</span><span>File</span><span>/</span><span>rest</span><span>/</span><span>fileservice</span><span>/</span><span>download</span><span>/</span><span>pdf</span></a>";
        String responseString = testDownloadService(httpURL);
        System.out.println("responseString : " + responseString);
    }
 
    /**
     * downloads pdf file using the input HTTP URL
     * @param httpURL
     * @return
     * @throws IOException
     */
    public static String testDownloadService(String httpURL) throws IOException {
 
        // local variables
        ClientConfig clientConfig = null;
        Client client = null;
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int responseCode;
        String responseMessageFromServer = null;
        String responseString = null;
        String qualifiedDownloadFilePath = null;
 
        try{
            // invoke service after setting necessary parameters
            clientConfig = new ClientConfig();
            clientConfig.register(MultiPartFeature.class);
            client =  ClientBuilder.newClient(clientConfig);
            client.property("accept", "application/pdf");
            webTarget = client.target(httpURL);
 
            // invoke service
            invocationBuilder = webTarget.request();
            //          invocationBuilder.header("Authorization", "Basic " + authorization);
            response = invocationBuilder.get();
 
            // get response code
            responseCode = response.getStatus();
            System.out.println("Response code: " + responseCode);
 
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }
 
            // get response message
            responseMessageFromServer = response.getStatusInfo().getReasonPhrase();
            System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);
 
            // read response string
            inputStream = response.readEntity(InputStream.class);
            qualifiedDownloadFilePath = DOWNLOAD_FILE_LOCATION + "MyJerseyPdfFile.pdf";
            outputStream = new FileOutputStream(qualifiedDownloadFilePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            // set download SUCCES message to return
            responseString = "downloaded successfully at " + qualifiedDownloadFilePath;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally{
            // release resources, if any
            outputStream.close();
            response.close();
            client.close();
        }
        return responseString;
    }
}
