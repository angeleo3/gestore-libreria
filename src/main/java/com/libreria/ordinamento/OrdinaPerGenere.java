package com.libreria.ordinamento;

import com.libreria.model.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerGenere implements StrategiaOrdinamento {

    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparing(Libro::getGenere, String.CASE_INSENSITIVE_ORDER));
    }
}
