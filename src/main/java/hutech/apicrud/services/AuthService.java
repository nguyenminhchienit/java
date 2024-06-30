package hutech.apicrud.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import hutech.apicrud.dto.request.AuthRequest;
import hutech.apicrud.dto.request.LogoutRequest;
import hutech.apicrud.dto.request.VerifyRequest;
import hutech.apicrud.dto.response.AuthResponse;
import hutech.apicrud.dto.response.VerifyResponse;
import hutech.apicrud.entities.InvalidToken;
import hutech.apicrud.entities.User;
import hutech.apicrud.exception.AppException;
import hutech.apicrud.exception.ErrorCode;
import hutech.apicrud.repository.InvalidTokenRepository;
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
import java.util.UUID;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

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
                .jwtID(UUID.randomUUID().toString())
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

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var jwt = verifySignedJWT(request.getToken());
        InvalidToken invalidToken = new InvalidToken();
        invalidToken.setId(jwt.getJWTClaimsSet().getJWTID());
        invalidToken.setExp(jwt.getJWTClaimsSet().getExpirationTime());

        invalidTokenRepository.save(invalidToken);
    }

    public VerifyResponse verifyToken(VerifyRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        SignedJWT jwt = null;
        try{
            jwt = verifySignedJWT(token);
        }catch (AppException e){
            isValid = false;
        }

        VerifyResponse verifyResponse = new VerifyResponse();
        verifyResponse.setValid(isValid);
        //verifyResponse.setData(jwt.getJWTClaimsSet().getClaim("user"));

        return verifyResponse;
    }

    private SignedJWT verifySignedJWT(String token) throws ParseException, JOSEException {
        JWSVerifier jwsVerifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        var claims = signedJWT.getJWTClaimsSet().getClaim("user");

        var verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expirationDate.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidTokenRepository
                .existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions()
                            .forEach(permission -> stringJoiner.add(permission.getName()));
            });
        return stringJoiner.toString();
    }
}
