package security.api;

import org.checkerframework.checker.units.qual.C;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;

public class RSASecurityUtil {

    /**
     * 指定加密算法为RSA
     */
    private static final String ALGORITHM_RSA = "RSA";

    /**
     * 密匙长度，用于初始化
     */
    public static final int KEYSIZE = 2048;

    /**
     * 指定公匙存放文件
     */
    public static String PUBLIC_KEY_FILE = "publicKey";

    /**
     * 指定私钥存放文件
     */
    public static String PRIVATE_KEY_FILE = "privateKey";

    /**
     * 生成密匙对
     */
    private static void generateKeyPair() throws NoSuchAlgorithmException {

        // 根据RSA算法创建keyPairGenerator对象
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);

        keyPairGenerator.initialize(KEYSIZE);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        Key publicKey = keyPair.getPublic();

        Key privateKey = keyPair.getPrivate();

        // 写入文件
        try (
                ObjectOutputStream oosPublic = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
                ObjectOutputStream oosPrivate = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE))
        ) {

            oosPublic.writeObject(publicKey);
            oosPrivate.writeObject(privateKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密，使用公匙
     *
     * @param source
     * @return
     */
    public static String encrypt(String source) throws Exception {
        Key publicKey = null;

        // 读到公匙文件
        try (
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE))
        ) {

            publicKey = (Key) stream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("公匙文件" + PUBLIC_KEY_FILE + "不存在！");
        }

        if (publicKey == null) {
            return null;
        }

        // 得到Cipher对象来实现对源数据的RSA加密
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        //  执行加密操作
        byte[] encrypt = cipher.doFinal(source.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(encrypt);
    }

    /**
     * 解密，私匙
     *
     * @param cryptograph
     * @return
     */
    public static String decrypt(String cryptograph) throws Exception {
        Key privateKey = null;

        // 读到公匙文件
        try (
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE))
        ) {

            privateKey = (Key) stream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("私匙文件" + PRIVATE_KEY_FILE + "不存在！");
        }

        if (privateKey == null) {
            return null;
        }

        // 得到Cipher对象对已用公钥加密的数据进行RSA解密
        Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        //  执行解密操作
        BASE64Decoder decoder = new BASE64Decoder();
        return new String(cipher.doFinal(decoder.decodeBuffer(cryptograph)));
    }

    public static void main(String[] args) throws Exception {
        generateKeyPair();

        String source = "恭喜发财";
        System.out.println("准备加密的串为:" + source);

        long encryptStart = System.currentTimeMillis();
        String cryptograph = encrypt(source);
        System.out.println("公匙加密后结果" + cryptograph);
        long encryptEnd = System.currentTimeMillis();

        System.out.println();

        long decryptStart = System.currentTimeMillis();
        String target = decrypt(cryptograph);
        System.out.println("私匙解密后结果:" + target);
        long decryptEnd = System.currentTimeMillis();

        System.out.println();

        System.out.println("encrypt time:" + (encryptEnd - encryptStart));
        System.out.println("decrypt time:" + (decryptEnd - decryptStart));
    }

}
