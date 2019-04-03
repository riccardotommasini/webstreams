package it.polimi.deib.rsp.webstreams.dbl;

import it.polimi.jasper.engine.Jasper;
import it.polimi.jasper.spe.operators.r2s.formatter.ResponseFormatterFactory;
import it.polimi.jasper.streams.RegisteredEPLStream;
import it.polimi.yasper.core.engine.EngineConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.QueryConfiguration;
import it.polimi.yasper.core.spe.operators.r2r.execution.ContinuousQueryExecution;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Riccardo on 03/08/16.
 */
public class DBLPub {

    protected static Jasper sr;

    public static void main(String[] args) throws Exception {
        URL resource = DBLPub.class.getResource("/rsp/jasper.properties");
        QueryConfiguration config = new QueryConfiguration(resource.getPath());
        EngineConfiguration ec = EngineConfiguration.loadConfig("/rsp/jasper.properties");

        long t0 = System.currentTimeMillis();

        System.out.println(t0);
        sr = new Jasper(t0, ec);


        DBPediaChangesWave dt1 = new DBPediaChangesWave("http://live.dbpedia.org/changesets/added");
        DBPediaChangesWave dt2 = new DBPediaChangesWave("http://live.dbpedia.org/changesets/removed");

        RegisteredEPLStream dtr1 = sr.register(dt1);
        RegisteredEPLStream dtr2 = sr.register(dt2);

        dt1.setWritable(dtr1);
        dt2.setWritable(dtr2);

        ContinuousQueryExecution cqe = sr.register(getQuery("dbl", ".rspql", "rsp/queries"), config);

        if (cqe.getContinuousQuery().isConstructType()) {
            cqe.add(ResponseFormatterFactory.getConstructResponseSysOutFormatter("JSON-LD", true));
        } else if (cqe.getContinuousQuery().isSelectType()) {
            cqe.add(ResponseFormatterFactory.getSelectResponseSysOutFormatter("CSV", true));
        }

        new Thread(dt1).start();
        new Thread(dt2).start();
    }


    public static String getQuery(String nameQuery, String suffix, String dir) throws IOException {
        URL resource = DBLPub.class.getResource("/" + dir + "/" + nameQuery + suffix);
        System.out.println(resource.getPath());
        File file = new File(resource.getPath());
        return FileUtils.readFileToString(file);
    }
}
