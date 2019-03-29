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

        int sgraphport = Integer.parseInt(properties.getProperty("sgraphport"));
        int streamport = Integer.parseInt(properties.getProperty("streamport"));

        String streamToCreate = properties.getProperty("streamToCreate");

        String geldtLastUpdateUrl = properties.getProperty("geldtLastUpdateUrl");
        String wikimediaStreamUrl = properties.getProperty("wikimediaStreamUrl");
        String DBPediaLastUpdateUrl = properties.getProperty("DBPediaLastUpdateUrl");

        String streamHeader = properties.getProperty("stream_header");
        String streamMappingPath = properties.getProperty("stream_mapping_path");
        String streamSGraphPath = properties.getProperty("stream_sgraph_path");

        switch (streamToCreate) {
            case "geldt_mentions":
            case "geldt_events":
            case "geldt_gkg" : {

                String[] stream_name_parts = streamToCreate.split("_");
                String stream_name_suffix = stream_name_parts[1];

                NewsWave.startGeldt(sgraphport, streamport, geldtLastUpdateUrl, stream_name_suffix, streamHeader, streamMappingPath, streamSGraphPath);
                break;
            }
            case "wikimedia": {
                WikiWave.startWikimedia(sgraphport, streamport, wikimediaStreamUrl, streamMappingPath);
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