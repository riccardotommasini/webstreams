package it.polimi.deib.rsp.rsp;

import it.polimi.jasper.engine.Jasper;
import it.polimi.jasper.spe.operators.r2s.formatter.ResponseFormatterFactory;
import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.yasper.core.engine.EngineConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.QueryConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.execution.ContinuousQueryExecution;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Riccardo on 03/08/16.
 */
public class GELDTJoinExample extends GELDTExample {


    public static void main(String[] args) throws InterruptedException, IOException, ConfigurationException {

        URL resource = GELDTJoinExample.class.getResource("/geldt/csparqlGELDT.properties");
        QueryConfiguration config = new QueryConfiguration(resource.getPath());
        EngineConfiguration ec = EngineConfiguration.loadConfig("/geldt/csparqlGELDT.properties");

        sr = new Jasper(0, ec);

        String type1 = "image";
        GELDTGraphStream dti = new GELDTGraphStream(2, "Donald Trump", type1);


        String type2 = "article";
        GELDTGraphStream dta = new GELDTGraphStream(3, "Donald Trump", type2);


        RegisteredEPLStream dtir = sr.register(dti);
        RegisteredEPLStream dtar = sr.register(dta);

        dta.setWritable(dtar);
        dti.setWritable(dtir);

        ContinuousQueryExecution cqe = sr.register(getQuery("TwoStreams", ".rspql", "join"), config);

        new Thread(dti).start();

        if (cqe.getContinuousQuery().isConstructType()) {
            cqe.add(ResponseFormatterFactory.getConstructResponseSysOutFormatter("JSON-LD", true));
        } else if (cqe.getContinuousQuery().isSelectType()) {
            cqe.add(ResponseFormatterFactory.getSelectResponseSysOutFormatter("CSV", true));
        }
    }

}
