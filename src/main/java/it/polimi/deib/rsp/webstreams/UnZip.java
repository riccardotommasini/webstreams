package geldt;

import it.polimi.deib.rsp.geldt.GELDTArticleExample;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {


    public static void main(String[] args) {
        URL source = GELDTArticleExample.class.getResource("/geldt/csv/zip/20190303184500.gkg.csv.zip");
        URL dest = GELDTArticleExample.class.getResource("/geldt/csv/");


        unzip(source.getPath(), dest.getPath());
    }

    public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        try {
            readzip(destDir, new FileInputStream(zipFilePath), new byte[1024]);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readzip(String destDir, InputStream fis, byte[] buffer) throws IOException {
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        while(ze != null){
            String fileName = ze.getName();
            File newFile = new File(destDir + File.separator + fileName);
            System.out.println("Unzipping to "+newFile.getAbsolutePath());
            //create directories for sub directories in zip
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            //close this ZipEntry
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        //close last ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();
    }
}

