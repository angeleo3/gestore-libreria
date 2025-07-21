import com.libreria.model.Libreria;
import com.libreria.model.Libro;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibreriaTest {

    @Test
    public void testAggiungiLibro() {
        Libreria libreria = new Libreria();
        Libro libro = new Libro("Miss Bee e il principe d'inverno", "Alessia Gazzola",
                "9788830462694", "Giallo",2, "letto");

        libreria.aggiungiLibro(libro);

        List<Libro> libri = libreria.getLibri();
        assertEquals(1, libri.size());
        assertEquals("Miss Bee e il principe d'inverno", libri.get(0).getTitolo());
    }
}
