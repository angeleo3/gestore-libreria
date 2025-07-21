package com.libreria.view;

import com.libreria.controller.GestoreFile;
import com.libreria.model.Libreria;
import com.libreria.model.Libro;
import com.libreria.ordinamento.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterfacciaGrafica extends JFrame {

    private JTable tabella;
    private DefaultTableModel modelloTabella;
    private Libreria libreria;

    private JComboBox<String> filtroStato;
    private JComboBox<String> filtroOrdine;
    private JTextField campoRicerca;

    public InterfacciaGrafica() {
        super("Gestore Libreria Personale");

        libreria = new Libreria();
        libreria.getLibri().addAll(GestoreFile.caricaLibri());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        //Pannelo filtri e ricerca
        JPanel pannelloTop = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //Menù a tendina
        JLabel statoLabel = new JLabel("Stato: ");
        String[] stati = {"Tutti", "letto", "da leggere", "in lettura"};
        filtroStato = new JComboBox<>(stati);

        //Menù a tendina
        JLabel ordinaLabel = new JLabel("Ordina per: ");
        String[] criteriOrdinamento = {"Titolo", "Autore", "Genere", "Valutazione"};
        filtroOrdine = new JComboBox<>(criteriOrdinamento);

        //Barra di ricerca
        JLabel ricercaLabel = new JLabel("Ricerca: ");
        campoRicerca = new JTextField(15);

        pannelloTop.add(statoLabel);
        pannelloTop.add(filtroStato);
        pannelloTop.add(ordinaLabel);
        pannelloTop.add(filtroOrdine);
        pannelloTop.add(ricercaLabel);
        pannelloTop.add(campoRicerca);

        add(pannelloTop, BorderLayout.NORTH);

        // Colonne della tabella
        String[] colonne = {"Titolo", "Autore", "ISBN", "Genere", "Valutazione", "Stato lettura"};
        modelloTabella = new DefaultTableModel(colonne, 0);
        tabella = new JTable(modelloTabella);
        aggiornaTabella();

        JScrollPane scrollPane = new JScrollPane(tabella);
        add(scrollPane, BorderLayout.CENTER);

        // Pannello bottoni
        JPanel pannelloBottoni = new JPanel();
        JButton aggiungiBtn = new JButton("Aggiungi");
        JButton modificaBtn = new JButton("Modifica");
        JButton eliminaBtn = new JButton("Elimina");

        pannelloBottoni.add(aggiungiBtn);
        pannelloBottoni.add(modificaBtn);
        pannelloBottoni.add(eliminaBtn);
        add(pannelloBottoni, BorderLayout.SOUTH);

        // Azioni bottoni
        aggiungiBtn.addActionListener(e -> aggiungiLibro());
        modificaBtn.addActionListener(e -> modificaLibro());
        eliminaBtn.addActionListener(e -> eliminaLibro());

        // Listener dinamici
        filtroStato.addActionListener(e -> aggiornaTabella());
        filtroOrdine.addActionListener(e -> aggiornaTabella());
        campoRicerca.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { aggiornaTabella(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { aggiornaTabella(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { aggiornaTabella(); }
        });

        // Carica iniziale
        aggiornaTabella();
    }

    private void aggiornaTabella() {
        // statoFiltro - criterioOrdinamento -> valore scleto all'interno della ComboBox filtrare - ordinare
        String statoFiltro = ((JComboBox<String>) ((JPanel) getContentPane().getComponent(0)).getComponent(1)).getSelectedItem().toString();
        String criterioOrdinamento = ((JComboBox<String>) ((JPanel) getContentPane().getComponent(0)).getComponent(3)).getSelectedItem().toString();
        // testo che viene digitato all'interno della barra di ricerca e viene convertito in LowerCase
        String ricerca = ((JTextField) ((JPanel) getContentPane().getComponent(0)).getComponent(5)).getText().toLowerCase();

        //Clona la lista per non modificare l'originale
        List<Libro> filtrati = new ArrayList<>(libreria.getLibri());

        //Filtro stato lettura
        if(!statoFiltro.equals("Tutti")) {
            filtrati.removeIf(l -> !l.getStatoLettura().equalsIgnoreCase(statoFiltro));
        }

        //Ricerca su titolo o autore o genere
        if(!ricerca.isEmpty()) {
            filtrati.removeIf(l -> !l.getTitolo().toLowerCase().contains(ricerca)
                                            && !l.getAutore().toLowerCase().contains(ricerca)
                                            && !l.getGenere().toLowerCase().contains(ricerca));
        }

        // Map delle strategie
        Map<String, StrategiaOrdinamento> strategie = Map.of(
                "Titolo", new OrdinaPerTitolo(),
                "Autore", new OrdinaPerAutore(),
                "Genere", new OrdinaPerGenere(),
                "Valutazione", new OrdinaPerValutazione()
        );

        // Si applica la strategia selezionata
        StrategiaOrdinamento strategy = strategie.get(criterioOrdinamento);
        if(strategy != null) {
            strategy.ordina(filtrati);
        }

        modelloTabella.setRowCount(0); // svuota tabella
        for (Libro l : filtrati) {
            modelloTabella.addRow(new Object[]{
                    l.getTitolo(), l.getAutore(), l.getIsbn(), l.getGenere(), l.getValutazione(), l.getStatoLettura()
            });
        }

    }

    private void aggiungiLibro() {
        Libro nuovo = mostraDialogoLibro(null);
        if (nuovo != null) {
            libreria.aggiungiLibro(nuovo);
            GestoreFile.salvaLibri(libreria.getLibri());
            aggiornaTabella();
        }
    }



    private void modificaLibro() {
        int selezionata = tabella.getSelectedRow();
        if (selezionata == -1) {
            JOptionPane.showMessageDialog(this, "Selezionare un libro da modificare");
            return;
        }

        Libro esistente = libreria.getLibri().get(selezionata);
        Libro modificato = mostraDialogoLibro(esistente);
        if (modificato != null) {
            libreria.getLibri().set(selezionata, modificato);
            GestoreFile.salvaLibri(libreria.getLibri());
            aggiornaTabella();
        }
    }

    private void eliminaLibro() {
        int selezionata = tabella.getSelectedRow();
        if (selezionata == -1) {
            JOptionPane.showMessageDialog(this, "Selezionare un libro da eliminare");
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(this, "Sicuro di voler eliminare questo libro?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            libreria.getLibri().remove(selezionata);
            GestoreFile.salvaLibri(libreria.getLibri());
            aggiornaTabella();
        }
    }

    private Libro mostraDialogoLibro(Libro libroEsistente) {
        JTextField campoTitolo = new JTextField();
        JTextField campoAutore = new JTextField();
        JTextField campoISBN = new JTextField();
        JTextField campoGenere = new JTextField();
        JTextField campoValutazione = new JTextField();
        JTextField campoStatoLettura = new JTextField();

        if (libroEsistente != null) {
            campoTitolo.setText(libroEsistente.getTitolo());
            campoAutore.setText(libroEsistente.getAutore());
            campoISBN.setText(libroEsistente.getIsbn());
            campoGenere.setText(libroEsistente.getGenere());
            campoValutazione.setText(String.valueOf(libroEsistente.getValutazione()));
            campoStatoLettura.setText(libroEsistente.getStatoLettura());
        }

        JPanel pannello = new JPanel(new GridLayout(0, 1));
        pannello.add(new JLabel("Titolo: "));
        pannello.add(campoTitolo);
        pannello.add(new JLabel("Autore: "));
        pannello.add(campoAutore);
        pannello.add(new JLabel("ISBN: "));
        pannello.add(campoISBN);
        pannello.add(new JLabel("Genere: "));
        pannello.add(campoGenere);
        pannello.add(new JLabel("Valutazione (1-5): "));
        pannello.add(campoValutazione);
        pannello.add(new JLabel("Stato lettura: "));
        pannello.add(campoStatoLettura);

        int result = JOptionPane.showConfirmDialog(this, pannello, libroEsistente == null ? "Aggiungi Libro" : "Modifica Libro", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                return new Libro(campoTitolo.getText(), campoAutore.getText(), campoISBN.getText(), campoGenere.getText(), Integer.parseInt(campoValutazione.getText()), campoStatoLettura.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore nei dati inseriti");
            }
        }

        return null;
    }
}
