package com.uniajc.hotellosolivos.util;

import org.mindrot.jbcrypt.BCrypt;

public class SeguridadUtil {
    public static String hashear(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt());
    }
    public static boolean verificar(String contrasena, String hash) {
        return BCrypt.checkpw(contrasena, hash);
    }
}
