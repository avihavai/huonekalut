package tikape.huonekalut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");

        Spark.get("*", (req, res) -> {
 
            List<String> huonekalut = new ArrayList<>();

            // avaa yhteys tietokantaan
            Connection conn
                    = DriverManager.getConnection("jdbc:sqlite:huonekalut.db");
            // tee kysely
            PreparedStatement stmt
                    = conn.prepareStatement("SELECT nimi FROM Huonekalu");
            ResultSet tulos = stmt.executeQuery();

            // käsittele kyselyn tulokset
            while (tulos.next()) {
                String nimi = tulos.getString("nimi");
                huonekalut.add(nimi);
            }
            // sulje yhteys tietokantaan
            conn.close();

            HashMap map = new HashMap<>();

            map.put("lista", huonekalut);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.post("*", (req, res) -> {
            System.out.println("Hei maailma!");
            System.out.println("Saatiin: "
                    + req.queryParams("huonekalu"));
            
            // avaa yhteys tietokantaan
            Connection conn
                    = DriverManager.getConnection("jdbc:sqlite:huonekalut.db");
            
            // tee kysely
            PreparedStatement stmt
                    = conn.prepareStatement("INSERT INTO Huonekalu (nimi) VALUES (?)");
            stmt.setString(1, req.queryParams("huonekalu"));
            
            stmt.executeUpdate();
            
            // sulje yhteys tietokantaan
            conn.close();

            res.redirect("/");
            return "";
        });
    }

}
