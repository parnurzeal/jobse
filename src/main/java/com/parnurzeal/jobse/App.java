package com.parnurzeal.jobse;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.*;
import org.apache.commons.io.IOUtils;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.LinkedList;
/**
 * Inverted Index App
 */
public class App 
{
    public static String convertStreamToString(java.io.InputStream is){
      java.util.Scanner s = new Scanner(is).useDelimiter("\\A");
      return s.hasNext()?s.next():"";
    }
    public static void main( String[] args )
    {
        InputStream is = App.class.getResourceAsStream("/rakuten1.job");
        String text = convertStreamToString(is);
        JSONParser parser = new JSONParser();

        InvertedIndex inverted_index = new InvertedIndex();
        try{ 
          Object obj = parser.parse(text);
          System.out.println(obj);
          inverted_index.add(obj);
          List<String> query = new LinkedList<String>();
          query.add("identifying");
          inverted_index.search(query);
        }catch(ParseException pe){
          System.out.println(pe);
        }
    }
}


class InvertedIndex{
  List<String> stopwords = Arrays.asList("a", "able", "about",
      "across", "after", "all", "almost", "also", "am", "among", "an",
      "and", "any", "are", "as", "at", "be", "because", "been", "but",
      "by", "can", "cannot", "could", "dear", "did", "do", "does",
      "either", "else", "ever", "every", "for", "from", "get", "got",
      "had", "has", "have", "he", "her", "hers", "him", "his", "how",
      "however", "i", "if", "in", "into", "is", "it", "its", "just",
      "least", "let", "like", "likely", "may", "me", "might", "most",
      "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
      "on", "only", "or", "other", "our", "own", "rather", "said", "say",
      "says", "she", "should", "since", "so", "some", "than", "that",
      "the", "their", "them", "then", "there", "these", "they", "this",
      "tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
      "what", "when", "where", "which", "while", "who", "whom", "why",
      "will", "with", "would", "yet", "you", "your");
  HashMap<String, List<Tuple>> index;
  HashMap<String, Object> data;
  
  public InvertedIndex(){
    index = new HashMap<String, List<Tuple>>();
  }
  
  public static void print(){
    System.out.println("This is indexer");

  }

  public int makeKey(Object obj){
    JSONObject js_obj = (JSONObject)obj;
    String delimiters = "[^a-zA-Z0-9]";
    String job_title = (String)js_obj.get("job_title");
    String location = (String)js_obj.get("location");
    String job_desc = (String)js_obj.get("job_description");
    String key = "";
    for(String w:job_title.split(delimiters)) key+=w;
    for(String w:location.split(delimiters)) key+=w;
    String[] job_desc_array = job_desc.split(delimiters);
    for(int i = 0;i<10&&i<job_desc_array.length;i++) key+=job_desc_array[i];
    return key.hashCode();
  }
  
  public boolean add(Object obj){
    System.out.println("Key: "+makeKey(obj));
    JSONObject js_obj = (JSONObject)obj;
    String desc= (String)js_obj.get("job_description");
    String delimiters = "[^a-zA-Z0-9]+";
    String[] words=desc.split(delimiters);
    for(String _w:words){
      String w = _w.toLowerCase();
      if(stopwords.contains(w))
        continue;
      List<Tuple> doclist = index.get(w);
      if(doclist==null){
        doclist= new LinkedList<Tuple>();
        index.put(w,doclist);
      }
      doclist.add(new Tuple(makeKey(obj),"Yay!"));
    }
    return true;
  }
  public void search(List<String> words){
    for(String _w:words){
      Set<Integer> answer = new HashSet<Integer>();
      String w = _w.toLowerCase();
      List<Tuple> doclist = index.get(w);
      if(doclist!=null){
        for(Tuple t:doclist){
          answer.add(t.docid);
        }
      }
      System.out.println("Query: " + w);
      System.out.print("Answer:");
      for(Integer out: answer){
        System.out.print(" "+out);
      }
    }
  }
}  

class Tuple{
  public int docid;
  public String path;
  public Tuple(int docid, String path){
    this.docid = docid;
    this.path = path;
  }
}

