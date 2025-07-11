package com.libreria.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libreria.model.Libro;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestoreFile {

    //nome del file dove salvo i dati
    private static final String NOME_FILE = "libreria.json";

    //Gson è una libreria che trasforma oggetti Java in JSON e viceversa
    // questo oggetto di tipo Gson, lo si usa per la serializzazione/deserializzazione
    private static final Gson gson = new Gson();

    // converte lista -> JSON -> file
    public static void salvaLibri(List<Libro> libri) {
        try (FileWriter writer = new FileWriter(NOME_FILE)) {
            //Uso FileWiter per aprire il file NOME_FILE in scrittura

            gson.toJson(libri, writer);
            //converte la lista in stringa JSON e la scrive

            System.out.println("Libreria salvata con successo");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: "+e.getMessage());
        }
    }

    // file -> JSON -> lista
    public static List<Libro> caricaLibri() {
        try (FileReader reader = new FileReader(NOME_FILE)) {
            Type listType = new TypeToken<List<Libro>>() {}.getType();
            // specifica il tipo List<Libro> usando TypeToken

            List<Libro> libri = gson.fromJson(reader, listType);
            //per leggere il file e creare la lista

            System.out.println("Libreria caricata con successo");
            return libri;
        } catch (IOException e) {
            System.out.println("Nessun file trovato, si parte da una libreria vuota");
            return new ArrayList<>();
        }
    }
}
