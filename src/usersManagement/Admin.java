package usersManagement;

public final class Admin {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin0123";
    private static Admin admin = null;

    public static Admin getAdminInstance(){
        if (admin == null)
            admin = new Admin();
        return admin;
    }

    private Admin(){};

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }
}
