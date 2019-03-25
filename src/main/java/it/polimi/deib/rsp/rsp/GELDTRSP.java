package it.polimi.deib.rsp.rsp;

import it.polimi.jasper.engine.Jasper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Riccardo on 03/08/16.
 */
public abstract class GELDTRSP {

    protected static Jasper sr;

    public static String getQuery(String nameQuery, String suffix, String type, String dir) throws IOException {
        URL resource = GELDTRSP.class.getResource("/" + dir + "/" + type + "/" + nameQuery + suffix);
        System.out.println(resource.getPath());
        File file = new File(resource.getPath());
        return FileUtils.readFileToString(file);
    }

}
