import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.*;
import java.nio.file.*;

import com.mpatric.mp3agic.*;

import static java.lang.System.exit;

public class mp3tagger {
    public static void main(String args[]) {
      BufferedReader reader = new BufferedReader(
      new InputStreamReader(System.in));
      String[] pathnames;
      String folder = "";
      Boolean outputflag;
      int forcount = 0;
      try {
          if (args[0].toLowerCase().contains("-verbose")) {
              outputflag = true;
          } else {
              outputflag = false;
          }
      }catch (ArrayIndexOutOfBoundsException Ex) {
          outputflag = false;
      }
        
      System.out.println("Enter input folder below");
      try {
      folder = reader.readLine();}
      catch (Exception ex) {System.out.println("Input is required, please restart program and try again");
      exit(403);}
      File foldercontents = new File(folder);
      pathnames = foldercontents.list();
      File outputfolder = new File(folder + "/output");

          if (!outputfolder.exists()) {
              outputfolder.mkdir();
          } else {
              System.out.println("output directory exists");
      }
      for (String pathname : pathnames) {
          forcount = forcount + 1;
              try {
                  Mp3File mp3file = null;
                  try {
                      if(pathname.contains(".mp3")) {
                          mp3file = new Mp3File(folder + "/" + pathname);
                      }else{break;}
                  }  catch (Exception ex) {
                      System.out.println(folder + "/" + pathname + " is the file causing this issue");
                      ex.printStackTrace();
                  }
                  if (outputflag) {

                      System.out.println(folder + "/" + pathname + " is the file name");
                  }
                  ID3v1 id3v1Tag;
                  if (mp3file == null) {
                      exit(404);
                  }
                  if (mp3file.hasId3v1Tag()) {
                      id3v1Tag = mp3file.getId3v1Tag();
                  } else {
                      if (outputflag) {
                          System.out.println("mp3 does not have an ID3v1 tag, let's create one..");
                      }
                      id3v1Tag = new ID3v1Tag();
                  }
                  mp3file.setId3v1Tag(id3v1Tag);


                  String[] filename = pathname.split("-");
                  filename[0] = filename[0].replace("\"", "");
                  filename[0] = filename[0].replace("'", "");
                  filename[0] = filename[0].replace("“", "");
                  filename[0] = filename[0].replace("”", "");

                  id3v1Tag.setArtist(filename[1]);
                  if (outputflag) {
                      System.out.println("Artist Section: " + filename[1]);
                  }
                  id3v1Tag.setTitle(filename[0]);
                  if (outputflag) {
                      System.out.println("Title name: " + filename[0]);
                  }
                  try {
                      mp3file.save(folder + "/output/" + pathname);
                  } catch (Exception ex) {
                      System.out.println(ex.getMessage());
                  }
              } catch (NullPointerException ex) {
                  System.out.println("file involved was " + folder + "/" + pathname);
                  ex.printStackTrace();
              }




      }

    }
}