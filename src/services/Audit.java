package services;

import java.sql.Timestamp;

public final class Audit {
    private static Audit audit = null;

    // Get Audit instance
    public static Audit getAuditInstance(){
        if (audit == null)
            audit = new Audit();
        return audit;
    }

    private Audit(){}

    protected void auditService(String action){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String [] data = {action, timestamp.toString()};
        Database.writeDataToCsv("auditLog.csv", data);
    }

}
