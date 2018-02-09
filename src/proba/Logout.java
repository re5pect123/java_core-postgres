package proba;

import java.sql.*;

public class Logout {

    static void insert(int userId, String cookie, String url) throws ClassNotFoundException, SQLException {

        Statement stmt = ConnectionDb.con().createStatement();
        String query = "SELECT * FROM logout_tmp";
        String insertInLogoutTmpSession = "INSERT INTO logout_tmp_session (user_id, cookie, date) VALUES (?,?, NOW());";
        String deleteFromLogoutTmpSession ="DELETE FROM logout_tmp_session WHERE user_id = ? AND date = (SELECT MIN(date) FROM logout_tmp_session WHERE user_id = ?) ;";
        String countFromLogoutTmpSession = "SELECT count(user_id) from logout_tmp_session WHERE user_id = ";

        ResultSet rsLogoutTmp = stmt.executeQuery(query);
        int userIdDb;

        while (rsLogoutTmp.next()) {
            // uzima userId iz baze
            userIdDb = rsLogoutTmp.getInt(2);
            System.out.println("UserIdDb " + userIdDb);
            // poredi userIdDB iz baze i userId parametar
            if (userIdDb == userId) {
                System.out.println("if userIdDb " + userIdDb + " == " + "userId " + userId);
                // izvlacimo najvevci broj iz baze za taj user id
                rsLogoutTmp = stmt.executeQuery(countFromLogoutTmpSession + userId);
                int najveciBroj;
                while (rsLogoutTmp.next()){
                    najveciBroj = rsLogoutTmp.getInt(1);
                    System.out.println("NAJVECI BROJ " + najveciBroj);
                    najveciBroj++;
                    if (najveciBroj <= 3){
                        System.out.println("Manji broj korisnika od 3");
                        PreparedStatement ps2 = ConnectionDb.con().prepareStatement(insertInLogoutTmpSession);
                        ps2.setInt(1, userId);
                        ps2.setString(2, cookie);
                        ps2.execute();
                    }
                    //brisemo korisnika sa brojem 1
                    System.out.println("Najveci broj " + najveciBroj);
                    if (najveciBroj == 4){
                        System.out.println("Izbacivanje 3 korisnika");
                        PreparedStatement ps2 = ConnectionDb.con().prepareStatement(deleteFromLogoutTmpSession);
                        ps2.setInt(1, userId);
                        ps2.setInt(2, userId);
                        ps2.execute();
                        System.out.println("Unos novog korisnika");
                        //Ubaci
                        PreparedStatement ps21 = ConnectionDb.con().prepareStatement(insertInLogoutTmpSession);
                        ps21.setInt(1, userId);
                        ps21.setString(2, cookie);
                        ps21.execute();
                    }
                }
                return;
            }
        }
        //INSERT USER IN DATABASE logout_tmp IF NOT EXIST
        System.out.println("INSERT ");
        String insertInTable = "INSERT INTO logout_tmp (user_id, url) VALUES (?,?)";

        PreparedStatement ps = ConnectionDb.con().prepareStatement(insertInTable);
        ps.setInt(1, userId);
        ps.setString(2, url);
        ps.execute();
        PreparedStatement ps1 = ConnectionDb.con().prepareStatement(insertInLogoutTmpSession);
        ps1.setInt(1, userId);
        ps1.setString(2, cookie);
        ps1.execute();


    }



}
