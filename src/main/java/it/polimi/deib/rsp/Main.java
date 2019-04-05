package it.polimi.deib.rsp;

import it.polimi.deib.rsp.webstreams.dbl.DBPediaChangesWave;
import it.polimi.deib.rsp.webstreams.gdelt.NewsWave;
import it.polimi.deib.rsp.webstreams.wes.WikiWave;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        PropertiesReader propertiesReader = new PropertiesReader();

        Properties properties = propertiesReader.getProperties();

        String host = properties.getProperty("host");
        int sgraphport = Integer.parseInt(properties.getProperty("sgraphport"));
        int streamport = Integer.parseInt(properties.getProperty("streamport"));

        String streamToCreate = properties.getProperty("streamToCreate");

        String gdeltLastUpdateUrl = properties.getProperty("gdeltLastUpdateUrl");
        String wikimediaStreamUrl = properties.getProperty("wikimediaStreamUrl");
        String DBPediaLastUpdateUrl = properties.getProperty("DBPediaLastUpdateUrl");

        String streamHeader = properties.getProperty("stream_header");
        String streamMappingPath = properties.getProperty("stream_mapping_path");
        String streamSGraphPath = properties.getProperty("stream_sgraph_path");

        switch (streamToCreate) {
            case "gdelt_mentions":
            case "gdelt_events":
            case "gdelt_gkg": {

                String[] stream_name_parts = streamToCreate.split("_");
                String stream_name_suffix = stream_name_parts[1];

                NewsWave.startGdelt(host, sgraphport, streamport, gdeltLastUpdateUrl, stream_name_suffix, streamHeader, streamMappingPath, streamSGraphPath);
                break;
            }
            case "wikimedia": {
                WikiWave.startWikimedia(sgraphport, streamport, wikimediaStreamUrl, streamMappingPath);
                break;
            }
            case "dbpedia": {
                new DBPediaChangesWave(DBPediaLastUpdateUrl).run();
                break;
            }
        }

    }

}