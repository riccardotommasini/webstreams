package it.polimi.deib.rsp.rsp.event;

import it.polimi.deib.rsp.rsp.GELDTRSP;

/**
 * Created by Riccardo on 03/08/16.
 */
public class GELDTEventExample extends GELDTRSP {


    public static void main(String[] args) throws Exception {
//
//        URL resource = GELDTEventExample.class.getResource("/geldt/csparqlGELDT.properties");
///        QueryConfiguration config = new QueryConfiguration(resource.getPath());
//        EngineConfiguration ec = EngineConfiguration.loadConfig("/geldt/csparqlGELDT.properties");
//
//        sr = new Jasper(0, ec);
//
//        String type = "event";

        new Thread(new WebSocketStream("ws://localhost:8080/gkg")).start();
        new Thread(new WebSocketStream("ws://localhost:8080/events")).start();
        new Thread(new WebSocketStream("ws://localhost:8080/mentions")).start();
//        System.out.println(dt);
//
//        RegisteredEPLStream dtr = sr.register(dt);
//
//        dt.setWritable(dtr);
//
//        ContinuousQueryExecution cqe = sr.register(getQuery("03", ".rspql", type, "events"), config);
//
//        if (cqe.getContinuousQuery().isConstructType()) {
//            cqe.add(ResponseFormatterFactory.getConstructResponseSysOutFormatter("JSON-LD", true));
//        } else if (cqe.getContinuousQuery().isSelectType()) {
//            cqe.add(ResponseFormatterFactory.getSelectResponseSysOutFormatter("CSV", true));
//        }
    }

}
