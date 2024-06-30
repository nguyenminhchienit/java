package hutech.apicrud.configuration;

import com.nimbusds.jose.JOSEException;
import hutech.apicrud.dto.request.VerifyRequest;
import hutech.apicrud.services.AuthService;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @NonFinal
    @Value("${jwt.sign-key}")
    public String SIGN_KEY;
    @Autowired
    private AuthService authService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            VerifyRequest request = new VerifyRequest();
            request.setToken(token);
            var response = authService.verifyToken(request);

            if(!response.getValid()){
                throw new JwtException("JWT token not verified");
            }

        }catch (JOSEException | ParseException e){
            throw new JwtException(e.getMessage());
        }

        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec spec = new SecretKeySpec(SIGN_KEY.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(spec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
