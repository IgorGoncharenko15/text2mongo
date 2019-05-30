/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkgMain;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.*;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;



/**
 *
 * @author igor
 */
public class parse_jsoup {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        
      //  Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
//Elements newsHeadlines = doc.select("#mp-itn b a");

        Mongo mongoClient = new Mongo();
        
        DB db = mongoClient.getDB("mydb");
        
   //      DBCollection coll = db.getCollection("testData");
        
              DBCollection coll = db.getCollection("cian");
        
         //DBCursor all_element = coll.find();
         
         BasicDBObject query = new BasicDBObject();
         //System.out.println(query.put("x", 3).toString());
         
         DBCursor cursor = coll.find();
         
        // while(cursor.hasNext()) {System.out.println(cursor.next().get("name"));}
            
         boolean  no_enter = true;
   
         if (!no_enter)
         {
                BasicDBObject doc = new BasicDBObject("number_in_list", "-1.")
                     .append("address", "Москва, Кремль")
                     .append("appartment_type", "комн")
                      .append("sqm_all", "20ыаи")
                        .append("price", "20млн")
                        .append("house_type", "П")
                        .append("tel", "+7 101-01-10")
                        .append("comment", "buy");
                        
                     
                coll.insert(doc);

             
             
         }
         
         //before page 335 we had comment only if the base had ID
         for (int k = 2; k<=3100;k++){
              //for (int k = 2; k<=2;k++){
      //all  Document doc = Jsoup.connect("http://www.cian.ru/cat.php?deal_type=2&obl_id=1&city[0]=1&room0=1&room1=1&room2=1&room3=1&room4=1&room5=1&room6=1&p=" + k).timeout(10000).get();
      //1 day  Document doc = Jsoup.connect("http://www.cian.ru/cat.php?deal_type=2&obl_id=1&city[0]=1&room1=1&room2=1&room3=1&room4=1&room5=1&totime=86400&p=" + k).timeout(10000).get();
        
             //5 days432000&p=3400
             Document doc = Jsoup.connect("http://www.cian.ru/cat.php?deal_type=2&obl_id=1&city[0]=1&room1=1&room2=1&room3=1&room4=1&room5=1&totime=432000&p=" + k).timeout(30000).get();
             
        Element table = doc.select("table").get(4); //select the first table.
Elements rows = table.select("tr");
Elements id_row = table.select("trid");

System.out.println(id_row.toString());

for (int i = 1; i < rows.size(); i++) {
//for (int i = 12*3+1; i < 12*4+1; i++) {//first row is the col names so skip it.
    Element row = rows.get(i);
    Elements cols = row.select("td");
    
    if(cols.size()>1){
        BasicDBObject appartment = new BasicDBObject();
    for (int j =0;j<cols.size();j++){
           // System.out.println(cols.get(j).text() + " |end_block| j = " + (j+1) + " i = " + (i+1));
            
            //if (!no_enter){
            String text_to_write = cols.get(j).text();
            
            
             if (j==0)
             {
                 appartment.append("number_in_list", text_to_write);
             }
             if (j==1)
             {
                    appartment.append("address", text_to_write);
             }
             if (j==2)
             {
                    appartment.append("appartment_type", text_to_write);
             }
             if (j==3)
             {
                    appartment.append("sqm_all", text_to_write);
              }
             if (text_to_write.contains(" $) "))
             {
                    appartment.append("price", text_to_write);
                    appartment.append("house_type", cols.get(j+1).text() + " " + cols.get(j+2).text());
             }
                    
             if (text_to_write.contains("если объекта нет, сообщите нам"))
             {
                            appartment.append("tel", text_to_write);
                            appartment.append("comment", cols.get(j+1).text());
              }
             
             appartment.append("date","2015-03-29");
             //if (text_to_write.contains("ID:")){appartment.append("comment", text_to_write);}
             
                     
            
    }
            
    
   coll.insert(appartment); 
    }
}
System.out.println("page " + k);
 Thread.sleep(2000);  
         }

    }
 }
