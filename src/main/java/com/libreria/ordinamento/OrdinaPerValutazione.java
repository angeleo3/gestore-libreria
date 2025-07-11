package com.libreria.ordinamento;

import com.libreria.model.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerValutazione implements StrategiaOrdinamento {

    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparingInt(Libro::getValutazione).reversed());
    }
}
