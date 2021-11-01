package KI;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ArffSaver;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffLoader.ArffReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class Main {

    public static void main(String[] args) throws Exception{
    	 Main programm = new Main(args[0]);
    }

    Instances datensatz;
    int anzAttribute = 3;

    public Main(String filename)throws Exception{
        Instances data = new Instances(new BufferedReader(new FileReader(filename)));
        //Instances data = getARFF_File("D:/UNI/KI/Git/KI-Projekt-Pado/sarcasm.arff");

        datensatz =  data;
        attributHinzufuegen("anzWorte");
        attributBearbeiten(1,2,3);
        getBewertung(2, 3);
        //System.out.println(datensatz);
        wortAnzahlHinzufuegen();
        speichern();
    }
    
    private String getToken(int instanz) {
    	return datensatz.instance(instanz).stringValue(2);
    }
    private void wortAnzahlHinzufuegen() {
    	int wortanzahl = 0;
    	String token;
		for (int i = 0; i < datensatz.numAttributes(); i++) {
				token = getToken(i);
			
			if (token == null || token.isEmpty()) {
				wortanzahl = 0; 
			}else { 
				String[] words = token.split("\\s+"); 
				wortanzahl = words.length; 
				attributBearbeiten(i,3, wortanzahl);
			}
		}
	}

        
    
    private void attributHinzufuegen(String nameAttribut){
        datensatz.insertAttributeAt(new Attribute(nameAttribut),anzAttribute++);
    }
    private void attributBearbeiten(int i,int attributIndex, int value){
        datensatz.instance(i).setValue(attributIndex,value);
    }
    private void getBewertung(int i, int attribute){
        System.out.println(datensatz.instance(i).stringValue(attribute));
    }
    private void speichern() throws IOException {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(datensatz);
            saver.setFile(new File("D:/UNI/KI/DatensatzeSaver/NeuerDatensatz.arff"));
            saver.writeBatch();
    }
    private Instances getARFF_File(String path) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(path));
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();
		return  data;
    }
}