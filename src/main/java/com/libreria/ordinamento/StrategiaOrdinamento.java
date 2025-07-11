package com.libreria.ordinamento;

import com.libreria.model.Libro;

import java.util.List;

public interface StrategiaOrdinamento {

    void ordina(List<Libro> libri);

}
