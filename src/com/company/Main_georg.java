package KI;

import weka.core.*;
import weka.core.converters.ArffSaver;
import weka.core.converters.ArffLoader.ArffReader;

import weka.core.Attribute;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	Instances datensatz;
	int anzAttribute = 3;
	//String neuerDatensatzpath = "/home/sebastian/Documents/NeuerDatensatz.arff";

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
		wortAnzahlHinzufuegen();
		zeichenAnzahlHinzufuegen();
		anzahlSpecialCharacterHinzufuegen();
		anzahlSuffixHinzufuegen("ing");
		anzahlSuffixHinzufuegen("ed");
		speichern();
	}

	private void attributeProWort(Attribute attribute) {
		int anzAtt, anzWorte;
		Attribute attributeNew = attributHinzufuegen(attribute.name() + "/Wort");
		for (int i = 0; i < datensatz.numInstances(); i++) {
			anzAtt = Integer.parseInt(datensatz.instance(i).toString(attribute));
			System.out.println(datensatz.instance(i).toString(attribute) + "/" + datensatz.instance(i).toString(datensatz.attribute("anzWorte")) + datensatz.instance(i).stringValue(2));
			anzWorte = Integer.parseInt(datensatz.instance(i).toString(datensatz.attribute("anzWorte")));
			attributBearbeiten(i, attributeNew, anzAtt / anzWorte);
		}
	}

	private String getToken(int instanz) {
		return datensatz.instance(instanz).stringValue(2);
	}

	private void anzahlSuffixHinzufuegen(String suffix) {
		String attName = "anzEndetMit_" + suffix;
		Attribute attribute = attributHinzufuegen(attName);
		int cnt = 0;
		String words[];
		for (int i = 0; i < datensatz.numInstances(); i++) {
			words = getToken(i).split(" ");
			for (int j = 0; j < words.length; j++) {

				if (words[j].endsWith(suffix))
					cnt++;
			}
			attributBearbeiten(i, attribute, cnt);
			cnt = 0;
		}
		attributeProWort(attribute);
	}

	private void anzahlSpecialCharacterHinzufuegen() {
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
		}
		attributeProWort(attribute);
	}

	private void zeichenAnzahlHinzufuegen() {
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

	private void wortAnzahlHinzufuegen() {
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

	private void speichern() throws IOException {
		ArffSaver saver = new ArffSaver();
		saver.setInstances(datensatz);
		saver.setFile(new File(neuerDatensatzpath));
		saver.writeBatch();
	}

	private Instances getARFF_File(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();
		return data;
	}
}
