package com.pessoal.ToDoList.Infra.Security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pessoal.ToDoList.Domain.User.Usuario;
//Criação do Token Service JWT
@Service
public class TokenService {
    //Comando para pegar a chave secreta do arquivo application.propperties
    @Value("${api.security.token.secret}")
    private String secret;
    //Método para Gerar o Token
    public String GerarToken(Usuario usuario){
        try {
            //Criação do algoritmo de segurança HMAC256
            Algorithm algoritimo = Algorithm.HMAC256(secret);
            //Criação do Token através da biblioteca JWT
            String token = JWT.create()
                //Comando que define quem emitirá o token
                .withIssuer("ToDoList")
                //Comando que define quem usará o token
                .withSubject(usuario.getEmail())
                //Comando que define a data de expiração do token
                .withExpiresAt(this.GerarDataExpiracao())
                //Comando que executa de fato a criação do token
                .sign(algoritimo);
                return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro durante a geração de Token");
        }
    }
    //Método para validar o Token
    public String ValidarToken(String token){
        //Tenta validar o token, caso não consiga, retorna null
        try {
            //Criação do algoritmo de segurança HMAC256 da mesma forma que na geração do token
            Algorithm algoritimo = Algorithm.HMAC256(secret);
            //Comando que executa a validação do token
            return JWT.require(algoritimo)
                .withIssuer("ToDoList")
                //Comando que constrói o objeto de verificação do token
                .build()
                //Comando que executa a verificação do token
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
    //Método para gerar a data de expiração do token
    private Instant GerarDataExpiracao(){
        //Retorna a data atual mais 2 horas conforme o fuso horário de Brasília
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
