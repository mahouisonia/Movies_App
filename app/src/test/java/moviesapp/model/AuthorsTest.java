package moviesapp.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthorsTest {
    private Authors authors = new Authors(Arrays.asList("Martin Dupont", "Marie Martin", "François Cordonnier"));

    @Test
    public void testToString() {
        String expected = "Martin Dupont, Marie Martin, François Cordonnier";
        assertThat(authors.toString()).isEqualTo(expected);
    }
}
