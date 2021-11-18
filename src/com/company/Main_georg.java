package KI;

import weka.core.*;
import weka.core.converters.ArffSaver;

import weka.core.Attribute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	Instances datensatz;
	int anzAttribute = 2;
	String neuerDatensatzpath = "P:\\KI\\Data\\sarcasm_dev_1.arff";

	public static void main(String[] args) throws Exception {
		Main programm = new Main(args[0]);
	}

	public Main(String filename) throws Exception {
		Instances data = new Instances(new BufferedReader(new FileReader(filename)));
		//// Instances data = getARFF_File("D:/UNI/KI/Git/KI-Projekt-Pado/sarcasm.arff");

		datensatz = data;

		// getBewertung(2, 3);
		// System.out.println(datensatz);
		//Features
		anzahlWorteHinzufuegen();
		anzahlZeichenHinzufuegen();
		anzahlSonderzeichenHinzufuegen();
		anzahlSuffixHinzufuegen("ing");
		anzahlSuffixHinzufuegen("ed");
		anzahlSuffixHinzufuegen("s");
		anzahlSuffixHinzufuegen("y");
		anzahlSuffixHinzufuegen("ied");
		anzahlWorteInCapsHinzufuegen();
		anzahlEinzelnerSonderzeichenHinzufuegen('?', "Fragezeichen");
		anzahlEinzelnerSonderzeichenHinzufuegen('!', "Ausrufezeichen");
		anzahlEinzelnerSonderzeichenHinzufuegen('.', "Punkt");
		anzahlEinzelnerSonderzeichenHinzufuegen(',', "Komma");
		anzahlGesuchtesWort("joke");
		anzahlGesuchtesWort("irony");
		anzahlGesuchtesWort("ironic");
		anzahlGesuchtesWort("sarcasm");
		anzahlGesuchtesWort("kidding");
		anzahlGesuchtesWort("this");
		anzahlGesuchtesWort("these");
		anzahlGesuchtesWort("those");
		anzahlGesuchtesWort("who");
		anzahlGesuchtesWort("which");
		anzahlGesuchtesWort("whose");
		entferneUrspruenglicheAttribute();
		speichern();
	}

	private void anzahlGesuchtesWort(String wort) {
		Attribute attribute = attributHinzufuegen("anzWort_" + wort);
		wort = (" " + wort + " ");
		int wortanzahl;
		String token;
		int index;
		for (int i = 0; i < datensatz.numInstances(); i++) {
			token = getToken(i);
			token.replaceAll("[^a-zA-Z0-9]+"," ");
			wortanzahl = 0;
			index = 0;
			if (token == null || token.isEmpty()) {
				wortanzahl = 0;
			} else {
				while(index != -1){

				    index = token.indexOf(wort,index);

				    if(index != -1){
				        wortanzahl ++;
				        index += wort.length();
				    }
				}
			}
			attributBearbeiten(i, attribute, wortanzahl);
		}
		attributeProWort(attribute);
	}
	private void attributeProWort(Attribute attribute) {
		int anzAtt, anzWorte;
		Attribute attributeNew = attributHinzufuegen(attribute.name() + "/Wort");
		for (int i = 0; i < datensatz.numInstances(); i++) {
			anzAtt = Integer.parseInt(datensatz.instance(i).toString(attribute)) * 1000;
			//System.out.println(datensatz.instance(i).toString(attribute) + "/" + datensatz.instance(i).toString(datensatz.attribute("anzWorte")) + datensatz.instance(i).stringValue(2));
			anzWorte = Integer.parseInt(datensatz.instance(i).toString(datensatz.attribute("anzWorte")));
			attributBearbeiten(i, attributeNew, anzAtt / anzWorte);
		}
	}

	private String getToken(int instanz) {
		return datensatz.instance(instanz).stringValue(2);
	}
	private void anzahlWorteInCapsHinzufuegen() {
		Attribute attribute = attributHinzufuegen("anzWorteInCaps");
		int wortanzahl = 0;
		String token;
		Pattern p = Pattern.compile("\\b[A-Z]{3,}\\b"); //Mit midestens 3 capsbuchstaben
		Matcher m;
		for (int i = 0; i < datensatz.numInstances(); i++) {
			token = getToken(i);
			token.split(" ,.!?");

			if (token == null || token.isEmpty()) {
				wortanzahl = 0;
				 //System.out.println("keine caps in Instanz " + i );
			} else {
				m = p.matcher(token);
				while (m.find()) {
					wortanzahl++;
				    String word = m.group();
				    //System.out.println(word + "  ist in instanz: " + i);
				}
			}
			//System.out.println(wortanzahl);
			attributBearbeiten(i, attribute, wortanzahl);
			wortanzahl = 0;
		}
		attributeProWort(attribute);
	}
	private void anzahlSuffixHinzufuegen(String suffix) {
		String attName = "anzEndetMit_" + suffix;
		Attribute attribute = attributHinzufuegen(attName);
		int cnt = 0;
		String words[];
		for (int i = 0; i < datensatz.numInstances(); i++) {
			words = getToken(i).split(" ,.!?");
			for (int j = 0; j < words.length; j++) {

				if (words[j].endsWith(suffix))
					cnt++;
			}
			attributBearbeiten(i, attribute, cnt);
			cnt = 0;
		}
		attributeProWort(attribute);
	}

	private void anzahlSonderzeichenHinzufuegen() {
		Attribute attribute = attributHinzufuegen("anzSonderzeichen");
		int count = 0;
		String token = null;

		for (int i = 0; i < datensatz.numInstances(); i++) {
			token = getToken(i).replace(" ", "");
			for (int j = 0; j < token.length(); j++) {
				if (token.substring(j, j + 1).matches("[^A-Za-z0-9]")) {
					count++;
				}
			}
			attributBearbeiten(i, attribute, count);
			count = 0;
		}
		attributeProWort(attribute);
	}

	private void anzahlEinzelnerSonderzeichenHinzufuegen(char zeichen, String zeichenName) {
		Attribute attribute = attributHinzufuegen("anz" + zeichenName);
		int count = 0;
		String token = null;

		for (int i = 0; i < datensatz.numInstances(); i++) {
			token = getToken(i).replace(" ", "");
			for (int j = 0; j < token.length(); j++) {
				if (token.substring(j, j + 1).matches("[" + zeichen + "]")) {
					count++;
				}
			}
			attributBearbeiten(i, attribute, count);
			count = 0;
		}
		attributeProWort(attribute);
	}

	private void anzahlZeichenHinzufuegen() {
		Attribute attribute = attributHinzufuegen("anzZeichen");
		int zeichenAnzahl = 0;
		String token = null;
		for (int i = 0; i < datensatz.numInstances(); i++) {
			token = getToken(i);
			zeichenAnzahl = token.replace(" ", "").length();
			attributBearbeiten(i, attribute, zeichenAnzahl);
		}
		attributeProWort(attribute);
	}

	private void anzahlWorteHinzufuegen() {
		Attribute attribute = attributHinzufuegen("anzWorte");
		int wortanzahl = 0;
		String token;
		for (int i = 0; i < datensatz.numInstances(); i++) {
			token = getToken(i);

			if (token == null || token.isEmpty()) {
				wortanzahl = 0;
			} else {
				String[] words = token.split("\\s+");
				wortanzahl = words.length;
				attributBearbeiten(i, attribute, wortanzahl);
			}
		}
	}

	private Attribute attributHinzufuegen(String nameAttribut) {
		datensatz.insertAttributeAt(new Attribute(nameAttribut), ++anzAttribute);
		return datensatz.attribute(nameAttribut);
	}

	private void attributBearbeiten(int i, Attribute attribute, int value) {
		datensatz.instance(i).setValue(attribute, value);
	}

	private void getBewertung(int i, Attribute attribute) {
		System.out.println(datensatz.instance(i).stringValue(attribute));
	}

	private void entferneUrspruenglicheAttribute() {
		datensatz.deleteStringAttributes();
	}

	private void speichern() throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(datensatz);
		saver.setFile(new File(neuerDatensatzpath));
		saver.writeBatch();
	}
}
