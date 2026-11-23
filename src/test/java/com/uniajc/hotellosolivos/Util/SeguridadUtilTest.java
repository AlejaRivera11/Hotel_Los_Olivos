package com.uniajc.hotellosolivos.Util;

import com.uniajc.hotellosolivos.util.SeguridadUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SeguridadUtilTest {

    @Test
    void hashNoDebeSerIgualAlTextoOriginal() {
        String hash = SeguridadUtil.hashear("123456");
        assertNotEquals("123456", hash);
    }

    @Test
    void verificarContrasenaCorrecta() {
        String hash = SeguridadUtil.hashear("123456");
        assertTrue(SeguridadUtil.verificar("123456", hash));
    }

    @Test
    void rechazarContrasenaIncorrecta() {
        String hash = SeguridadUtil.hashear("123456");
        assertFalse(SeguridadUtil.verificar("654321", hash));
    }

    @Test
    void mismoTextoDebeGenerarHashesDistintos() {
        String hash1 = SeguridadUtil.hashear("123456");
        String hash2 = SeguridadUtil.hashear("123456");
        assertNotEquals(hash1, hash2);
    }
}