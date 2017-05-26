package king.org.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class JarStream {
    private static final Map<String, String> parameters = new HashMap<>();
    private String gamepack = null;
    private String codebase = null;
    private final int[] charset = new int[128];

    {
        charset[42] = 62;
        charset[43] = 62;
        charset[45] = 63;
        charset[47] = 63;
        for (byte i = 48; i < 58; ++i) {
            charset[i] = i + 4;
        }
        for (byte i = 65; i < 91; ++i) {
            charset[i] = i - 65;
        }
        for (byte i = 97; i < 123; ++i) {
            charset[i] = i - 71;
        }
    }

    public static String hash;

    private URL getWorldUrl() throws MalformedURLException {
        return new URL("http://world1.runescape.com/");
    }

    private String downloadAsString(final URL url) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    private byte[] getJarResource(final URL url) throws IOException {
        final JarURLConnection connection = (JarURLConnection) url.openConnection();
        hash = getRSChecksum(connection);
        final InputStream input = connection.getInputStream();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[2048];
        int bit;
        while (input.available() > 0 && (bit = input.read(buffer, 0, buffer.length)) >= 0) {
            output.write(buffer, 0, bit);
        }
        return output.toByteArray();
    }

    private void crawl() throws IOException {
        final String html = downloadAsString(getWorldUrl());
        final Pattern regex = Pattern.compile("<param name=\"?([^\\s]+)\"?\\s+value=\"?([^>]*)\"?>", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        final Matcher regexMatcher = regex.matcher(html);
        while (regexMatcher.find()) {
            if (!parameters.containsKey(regexMatcher.group(1))) {
                parameters.put(regexMatcher.group(1).replaceAll("\"", ""), regexMatcher.group(2).replaceAll("\"", ""));
            }
        }
        gamepack = html.split("'archive=")[1].split(" ")[0];
    }

    private URL getGamepack() throws MalformedURLException {
        return new URL(getWorldUrl().toString().concat(gamepack));
    }

    private int charIndex(final char character) {
        return character >= 0 && character < charset.length ? charset[character] : -1;
    }

    private int unscramble(final byte[] bytes, int offset, final String key) {
        final int initialOffset = offset;
        final int keyLength = key.length();
        int pos = 0;
        while (keyLength > pos) {
            final int currentChar = charIndex(key.charAt(pos));
            final int pos_1 = keyLength > (pos + 1) ? charIndex(key.charAt(pos + 1)) : -1;
            final int pos_2 = pos + 2 < keyLength ? charIndex(key.charAt(2 + pos)) : -1;
            final int pos_3 = keyLength > (pos + 3) ? charIndex(key.charAt(3 + pos)) : -1;
            bytes[offset++] = (byte) (pos_1 >>> 4 | currentChar << 2);
            if (pos_2 != -1) {
                bytes[offset++] = (byte) (pos_1 << 4 & 240 | pos_2 >>> 2);
                if (pos_3 != -1) {
                    bytes[offset++] = (byte) (192 & pos_2 << 6 | pos_3);
                    pos += 4;
                    continue;
                }
            }
            break;
        }
        return offset - initialOffset;
    }

    private byte[] toByte(final String key) {
        final int keyLength = key.length();
        if (keyLength == 0) {
            return new byte[0];
        } else {
            int unscrambledLength;
            int lengthMod = -4 & keyLength + 3;
            unscrambledLength = lengthMod / 4 * 3;
            if (keyLength <= lengthMod - 2 || charIndex(key.charAt(lengthMod - 2)) == -1) {
                unscrambledLength -= 2;
            } else if (keyLength <= lengthMod - 1 || -1 == charIndex(key.charAt(lengthMod - 1))) {
                --unscrambledLength;
            }
            final byte[] keyBytes = new byte[unscrambledLength];
            unscramble(keyBytes, 0, key);
            return keyBytes;
        }
    }

    private byte[] read(final JarInputStream jis) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[2048];
        int read;
        while (jis.available() > 0 && (read = jis.read(buffer, 0, buffer.length)) >= 0) {
            byteArrayOutputStream.write(buffer, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }


    private static byte[] createChecksum(JarURLConnection juc) {
        if (juc == null) {
            return null;
        }
        try {
            Attributes a = juc.getManifest().getAttributes("inner.pack.gz");
            if (a != null) {
                return a.getValue("SHA1-Digest").getBytes("UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getRSChecksum(JarURLConnection juc) {
        try {
            byte[] bytes = createChecksum(juc);
            if (bytes == null) {
                return "";
            }
            String result = "";
            for (byte b : bytes) {
                result += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
            }
            return result.substring(0, 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public HashMap<String, ClassNode> getRSPSContents(JarFile jar) {
    	HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
		try {
			Enumeration<?> enumeration = jar.entries();
			
			// Pass through every file in the jar
			while (enumeration.hasMoreElements()) {
				JarEntry entry = (JarEntry) enumeration.nextElement();
				
				// Only reads classes
				if (entry.getName().endsWith(".class")) {
					// Parse the class
					ClassReader classReader = new ClassReader(jar.getInputStream(entry));
					ClassNode classNode = new ClassNode();
					classReader.accept(classNode, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
					
					// Add to HashSet
					classes.put(classNode.name, classNode);
				}
			}
			jar.close();
			return classes;
		} catch (Exception e) {
			return null;
		}
    }
    
    public HashMap<String, ClassNode> getContents() {
        HashMap<String, ClassNode> parsedClasses = new HashMap<>();
        try {
            crawl();
            ClassLoader classloader = URLClassLoader.newInstance(new URL[]{getGamepack()}, getClass().getClassLoader());
            final byte[] pack = getJarResource(classloader.getResource("inner.pack.gz"));
            final SecretKeySpec secretKeySpec = new SecretKeySpec(toByte(parameters.get("0")), "AES");
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(toByte(parameters.get("-1")));
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            final byte[] unscrambled = cipher.doFinal(pack);
            final Pack200.Unpacker unpacker = Pack200.newUnpacker();
            final GZIPInputStream zipstream = new GZIPInputStream(new ByteArrayInputStream(unscrambled));
            final ByteArrayOutputStream innerstream = new ByteArrayOutputStream();
            unpacker.unpack(zipstream, new JarOutputStream(innerstream));
            zipstream.close();
            final JarOutputStream output = new JarOutputStream(new FileOutputStream(new File("gamepack.jar")));

            final JarInputStream jis = new JarInputStream(new ByteArrayInputStream(innerstream.toByteArray()));
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (name.startsWith("META-INF")) {
                    continue;
                }
                if (name.endsWith(".class")) {
                    final byte[] read = read(jis);
                    name = name.replace('/', '.');
                    name = name.substring(0, name.length() - 6);
                    final ClassReader classReader = new ClassReader(read);
                    ClassNode node = new ClassNode();
                    classReader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    parsedClasses.put(name, node);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedClasses;
    }

}
