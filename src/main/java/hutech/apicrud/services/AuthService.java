package hutech.apicrud.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import hutech.apicrud.dto.request.AuthRequest;
import hutech.apicrud.dto.request.VerifyRequest;
import hutech.apicrud.dto.response.AuthResponse;
import hutech.apicrud.dto.response.VerifyResponse;
import hutech.apicrud.entities.User;
import hutech.apicrud.exception.AppException;
import hutech.apicrud.exception.ErrorCode;
import hutech.apicrud.repository.UserRepository;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository userRepository;

    @NonFinal
    @Value("${jwt.sign-key}")
    public String SIGN_KEY;

    public AuthResponse authenticate(AuthRequest request) throws KeyLengthException {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!auth){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user, request);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setAuthenticated(auth);
        return authResponse;
    }

    public String generateToken(User user, AuthRequest request) throws KeyLengthException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("takis.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("user", request)
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public VerifyResponse verifyToken(VerifyRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        var claims = signedJWT.getJWTClaimsSet().getClaim("user");

//        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
//        Object userValue = claimsSet.getClaim("user");

        var verified = signedJWT.verify(jwsVerifier);

        VerifyResponse verifyResponse = new VerifyResponse();
        verifyResponse.setValid(verified && expirationDate.after(new Date()));
        verifyResponse.setData(claims);

        return verifyResponse;
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> stringJoiner.add(role));
        }
        return stringJoiner.toString();
    }
}
