package com.libreria.model;

import java.util.ArrayList;
import java.util.List;

public class Libreria {

    private List<Libro> libri;

    public Libreria() {
        this.libri = new ArrayList<>();
    }

    public void aggiungiLibro(Libro libro) {
        libri.add(libro);
    }

    public List<Libro> getLibri() {
        return libri;
    }
}
