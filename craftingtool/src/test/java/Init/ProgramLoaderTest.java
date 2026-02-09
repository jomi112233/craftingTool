package Init;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.jomi.Handlers.Init.ProgramLoader;

public class ProgramLoaderTest {
    
    @Test
    void programLoaderRunsNoErrors() {
        ProgramLoader loader = new ProgramLoader();

        assertDoesNotThrow(loader::start);
    }
}
