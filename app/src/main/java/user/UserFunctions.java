package user;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import user.exceptions.IncorrectEmailConfirmationException;
import user.exceptions.IncorrectPwdException;
import user.exceptions.MissingArgumentsException;
import user.exceptions.MissmatchPwdException;
import user.exceptions.UnregisteredUserException;
import user.exceptions.UserAlreadyExistException;

public class UserFunctions implements Serializable {

    private static Map<String, User> users = new HashMap<>();
    private static User actualUser;
    private static int confirmation;

    public UserFunctions() {
        User master = new User("master", toHexString(encodedPwd("1234")), "pmejiaskudelka@gmail.com", null);
        users.put("master", master);
    }

    public void setActualUser(User actualUser){
        this.actualUser = actualUser;
    }

    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    public static boolean equalsPwd(String pin1, String encP2) {
        return (toHexString(encodedPwd(pin1))).equals(encP2);
    }

    //Mira si el código que ingresa el usuario es igual que el enviado al correo
    public static void checkEmail(User u, int code) throws IncorrectEmailConfirmationException {
        if(code != confirmation)
            throw new IncorrectEmailConfirmationException();
        users.put(u.getUsername(), u);
    }

    public static byte[] encodedPwd(String pwd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(pwd.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException s) {
            return new byte[32];
        }
    }

    // Funciones principales

    // This function creates the account
    // It meets the second, third and forth requirements
    public static User newUser(String username, String pwd1, String pwd2, String email)
            throws UserAlreadyExistException, MissingArgumentsException, MissmatchPwdException {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistException();
        }
        if(username.equals("") || pwd1.equals("") || pwd2.equals("") || email.equals("")){
            throw new MissingArgumentsException();
        }
        if (!pwd1.equals(pwd2)) {
            throw new MissmatchPwdException();
        }
        User user = new User(username, toHexString(encodedPwd(pwd1)), email, null);
        confirmation = (int)(Math.random() * 100000);
        mailManager g = new mailManager(email, "Confirmación de cuenta",
                "¡Enhorabuena! La cuenta con nombre de usuario " + username + " ha sido creada con exito,"
                        + " \n para validarla introduzca el siguiente código de verificación: \n" + confirmation);
        g.createEmail();
        return user;
    }

    //Login del usuario
    public static void login(String username, String pwd) throws UnregisteredUserException, IncorrectPwdException {
        User u = users.get(username);
        if (u == null) {
            throw new UnregisteredUserException();
        }
        if (!equalsPwd(pwd, users.get(username).getPassword())) {
            throw new IncorrectPwdException();
        }
        actualUser = u;
    }

    public static void logout() {
        actualUser = null;
    }


    public static void confirmIdentityPwdRecover(String username) throws UnregisteredUserException {
        User u = users.get(username);
        if (u == null) {
            throw new UnregisteredUserException();
        }
        confirmation = (int)(Math.random() * 100000);
        mailManager g = new mailManager(u.getEmail(), "Recuperación de contraseña",
                "La cuenta con nombre de usuario " + username +
                        " ha solicitado una recuperación de contraseña, \n para acceder al siguiente paso introduzca el siguiente"
                        + " código de verificación: "+ confirmation);
        g.createEmail();
    }

    //Segundo paso
    public static void pwdRecovery(String username, int code, String pwd1, String pwd2) throws MissmatchPwdException, IncorrectEmailConfirmationException {
        if(code != confirmation)
            throw new IncorrectEmailConfirmationException();
        if(!pwd1.equals(pwd2))
            throw new MissmatchPwdException();
        users.get(username).setPassword(toHexString(encodedPwd(pwd1)));
    }
}
