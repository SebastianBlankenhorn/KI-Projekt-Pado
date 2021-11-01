package com.company;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ArffSaver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception{
        Main programm = new Main(args[0]);
    }

    Instances datensatz;
    int anzAttribute = 3;

    public Main(String filename)throws Exception{
        Instances data = new Instances(new BufferedReader(new FileReader(filename)));
        datensatz = new Instances(data);
        attributHinzufuegen("anzWorte");
        attributBearbeiten(1,2,3);
        getBewertung(2, 3);
        System.out.println(datensatz);
        speichern();
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
            saver.setFile(new File("/home/sebastian/Documents/Studium/KI/test/neuDatensatz.arff"));
            saver.writeBatch();
    }

}



//TODO Datei(Kommentar) einlesen

//TODO Neu generierte Datei ausgeben