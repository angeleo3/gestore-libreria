import com.libreria.controller.GestoreFile;
import com.libreria.model.Libro;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GestoreFileTest {

    @Test
    public void testSalvaECaricaLibri() {

        // Prepara un libro
        Libro libro = new Libro("Amori e segreti al Pumpkin Spice Cafè", "Laurie Gilmore",
                "9788822782674", "Romanzo", 0, "da leggere");

        // Salva su file
        GestoreFile.salvaLibri(List.of(libro));

        // Controlla se il file esiste
        File file = new File("libreria.json");
        assertTrue(file.exists());

        // Carica dal file
        List<Libro> caricati = GestoreFile.caricaLibri();

        assertEquals(1, caricati.size());
        assertEquals("Amori e segreti al Pumpkin Spice Cafè", caricati.get(0).getTitolo());
    }
}
