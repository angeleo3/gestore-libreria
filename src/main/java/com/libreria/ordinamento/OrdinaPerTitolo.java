package com.libreria.ordinamento;

import com.libreria.model.Libro;

import java.util.Comparator;
import java.util.List;

public class OrdinaPerTitolo implements StrategiaOrdinamento {

    @Override
    public void ordina(List<Libro> libri) {
        libri.sort(Comparator.comparing(Libro::getTitolo, String.CASE_INSENSITIVE_ORDER));
    }
}
