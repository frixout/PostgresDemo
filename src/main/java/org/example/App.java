package org.example;

import java.io.StringReader;
import java.sql.*;

public class App 
{
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5400/test";
    static final String USER = "postgres";
    static final String PASS = "P@ssw0rd";
    public static void main( String[] args ) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        String query1 = "SELECT t.*\n" +
                "FROM titles t\n" +
                "JOIN books b ON b.id = t.book_id JOIN authors a ON a.id = b.author_id\n" +
                "WHERE t.page % 2 = 0 \n" +
                "AND ((SELECT EXTRACT(YEAR FROM a.birth_date::timestamp) BETWEEN 1950 AND 2000) \n" +
                "\t  OR (SELECT EXTRACT (YEAR FROM a.death_date::timestamp) BETWEEN 1950 AND 2000));";
        String query2 = "SELECT a.*\n" +
                "FROM authors a\n" +
                "WHERE (SELECT EXTRACT(MONTH FROM a.birth_date::timestamp)) IN (7, 9, 10)\n" +
                "ORDER BY (death_date - birth_date)\n" +
                ";";
        String query3 = "SELECT CONCAT(a.first_name, ' ', a. last_name) AS full_name, COUNT(a.*) AS total_translate\n" +
                "FROM authors a\n" +
                "JOIN books b ON a.id = b.author_id\n" +
                "JOIN books_and_translaters b_t ON b.id = b_t.book_id\n" +
                "GROUP BY full_name\n" +
                "ORDER BY count(*) DESC\n" +
                "LIMIT 1\n" +
                ";";
        String query4 = "SELECT a.*, tr.*\n" +
                "FROM Translaters tr\n" +
                "JOIN Books_And_Translaters b_t ON b_t.translater_id = tr.id \n" +
                "JOIN Books b ON b.id = b_t.book_id\n" +
                "JOIN Authors a ON a.id = b.author_id\n" +
                "WHERE CONCAT(tr.first_name, ' ', tr.last_name) = CONCAT(a.first_name, ' ', a.last_name)\n" +
                ";";
        String query5 = "SELECT a.*, tr.* \n" +
                "FROM Translaters tr\n" +
                "JOIN Books_And_Translaters b_t ON b_t.translater_id = tr.id \n" +
                "JOIN Books b ON b.id = b_t.book_id\n" +
                "JOIN Authors a ON a.id = b.author_id\n" +
                "WHERE tr.first_name = 'Jack' AND a.first_name = 'Jack'\n" +
                ";";
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pst = con.prepareStatement(query1);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Получить все Title, у которых страница четная и книга, в которой они находятся, написана автором, в промежутке между 1950 и 2000 годом:");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ": " +
                        rs.getInt(2) + ", " + rs.getString(3) + ", " +
                        rs.getInt(4) + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pst = con.prepareStatement(query2);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Получить авторов, родившихся в июне,сентябре или октябре и отфильтровать их по продолжительности жизни:");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ": " +
                        rs.getString(2) + ", " + rs.getString(3) + ", " +
                        rs.getDate(4) + ", " +
                        rs.getDate(5) + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pst = con.prepareStatement(query3);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Получить автора, книги которого перевели больше всего:");
            while (rs.next()) {
                System.out.print("Книги " + rs.getString(1) + " перевели больше всего: " +
                        rs.getInt(2) + " раза\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pst = con.prepareStatement(query4);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Получить авторов и переводчиков, у которых одинаковые имя с фамилией:");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ", " +
                        rs.getString(2) + ", " +
                        rs.getString(3) + ", " +
                        rs.getDate(4) + ", " +
                        rs.getDate(5) + ", " +
                        rs.getInt(6) + ", " +
                        rs.getString(7)+ ", " +
                        rs.getString(8) + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pst = con.prepareStatement(query5);
             ResultSet rs = pst.executeQuery()) {
            System.out.println("Получить авторов и переводчиков, у который имя равняется заданному (какое хотите):");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + ", " +
                        rs.getString(2) + ", " +
                        rs.getString(3) + ", " +
                        rs.getDate(4) + ", " +
                        rs.getDate(5) + ", " +
                        rs.getInt(6) + ", " +
                        rs.getString(7)+ ", " +
                        rs.getString(8) + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
