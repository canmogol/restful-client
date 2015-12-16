package com.restful.client.example;

import com.fererlab.image.resource.ImageResource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * blocking image upload and download example
 */
public class BlockingImageUploadDownload implements Runnable {

    private Log log = LogFactory.getLog(BlockingImageUploadDownload.class);

    private final String url;

    public BlockingImageUploadDownload(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        try {
            log.info(">>> " + getClass().getSimpleName() + " BEGIN");
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(url);

            // get a resource to call
            ImageResource resource = target.proxy(ImageResource.class);

            // file content to post
            MultipartFormDataOutput multipartFormDataOutput = new MultipartFormDataOutput();
            String filePath = this.getClass().getClassLoader().getResource("yoda.png").getFile();
            multipartFormDataOutput.addFormData("file", new FileInputStream(new File(filePath)), MediaType.APPLICATION_OCTET_STREAM_TYPE);

            // file name at the server
            String fileName = "file" + (new Random().nextDouble()) + ".png";

            // upload image
            log.info("will upload file");
            resource.upload(fileName, multipartFormDataOutput);
            log.info("file uploaded");


            // download image
            log.info("will download file");
            Response responseImage = resource.download(fileName);
            // read response
            InputStream inputStreamImage = responseImage.readEntity(InputStream.class);
            byte[] bytesImage = IOUtils.toByteArray(inputStreamImage);
            FileOutputStream fileOutputStreamImage = new FileOutputStream(new File("/tmp/image-" + fileName));
            fileOutputStreamImage.write(bytesImage);
            fileOutputStreamImage.close();
            log.info("file downloaded");


            // image stream
            log.info("will get stream");
            Response responseImageStream = resource.downloadStream(fileName);
            // read response
            InputStream inputStreamImageStream = responseImageStream.readEntity(InputStream.class);
            byte[] bytesImageStream = IOUtils.toByteArray(inputStreamImageStream);
            FileOutputStream fileOutputStreamImageStream = new FileOutputStream(new File("/tmp/image-stream-" + fileName));
            fileOutputStreamImageStream.write(bytesImageStream);
            fileOutputStreamImageStream.close();
            log.info("stream end");


            log.info("<<< " + getClass().getSimpleName() + " END");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
