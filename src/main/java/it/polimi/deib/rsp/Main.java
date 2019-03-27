package it.polimi.deib.rsp;

import it.polimi.deib.rsp.webstreams.DBPediaChangesWave;
import it.polimi.deib.rsp.webstreams.geldt.NewsWave;
import it.polimi.deib.rsp.webstreams.wikimedia.WikiWave;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        PropertiesReader propertiesReader = new PropertiesReader();

        Properties properties = propertiesReader.getProperties();

        int endpointport = Integer.parseInt(properties.getProperty("endpointport"));
        int streamport = Integer.parseInt(properties.getProperty("streamport"));

        String streamToCreate = properties.getProperty("streamToCreate");

        String geldtLastUpdateUrl = properties.getProperty("geldtLastUpdateUrl");
        String wikimediaStreamUrl = properties.getProperty("wikimediaStreamUrl");
        String DBPediaLastUpdateUrl = properties.getProperty("DBPediaLastUpdateUrl");

        switch (streamToCreate) {
            case "geldt": {
                NewsWave.startGeldt(endpointport, streamport, geldtLastUpdateUrl);
                break;
            }
            case "wikimedia": {
                WikiWave.startWikimedia(endpointport, streamport, wikimediaStreamUrl);
                break;
            }
            case "dbpedia": {
                try {
                    DBPediaChangesWave.startDBPediaChanges(DBPediaLastUpdateUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

}