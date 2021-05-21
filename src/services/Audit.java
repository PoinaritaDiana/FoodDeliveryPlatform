package services;

import java.sql.Timestamp;

public final class Audit {
    private static Audit audit = null;

    public static Audit getAuditInstance(){
        if (audit == null)
            audit = new Audit();
        return audit;
    }

    private Audit(){}

    public void auditService(String action){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String [] data = {action, timestamp.toString(),Thread.currentThread().getName()};
        Database.writeDataToCsv("auditLog.csv", data);
    }

}
