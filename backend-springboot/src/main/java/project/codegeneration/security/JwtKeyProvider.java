package project.codegeneration.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;

@Component
public class JwtKeyProvider {

    @Value("${jwt.key-store}")
    private String keyStore;

    @Value("${jwt.key-store-password}")
    private String keyStorePassword;

    @Value("${jwt.alias}")
    private String alias;

    @Getter
    private Key privateKey;

    @Getter
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        ClassPathResource resource = new ClassPathResource(keyStore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        keyStore.load(resource.getInputStream(), keyStorePassword.toCharArray());

        privateKey = keyStore.getKey(alias, keyStorePassword.toCharArray());
        publicKey = keyStore.getCertificate(alias).getPublicKey();

    }
}
