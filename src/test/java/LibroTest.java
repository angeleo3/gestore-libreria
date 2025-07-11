import com.libreria.model.Libro;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibroTest {

    @Test
    public void testCostruttoreEGetter() {
        Libro libro = new Libro("Miss Bee e il principe d'inverno", "Alessia Gazzola",
                "9788830462694", "Giallo",2, "letto");
        assertEquals("Miss Bee e il principe d'inverno", libro.getTitolo());
        assertEquals("Alessia Gazzola", libro.getAutore());
        assertEquals("9788830462694", libro.getIsbn());
        assertEquals("Giallo", libro.getGenere());
        assertEquals(2, libro.getValutazione());
        assertEquals("letto", libro.getStatoLettura());
    }

    @Test
    public void testSetValutazione() {
        Libro libro = new Libro();
        libro.setValutazione(5);
        assertEquals(5, libro.getValutazione());
    }
}
