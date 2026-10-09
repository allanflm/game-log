package com.allan.gamelog.backend.games;

import com.allan.gamelog.backend.igdb.MissingCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

// Transforma exceções em respostas HTTP claras para o app, sem expor detalhes internos.
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MissingCredentialsException.class)
    public ProblemDetail handleMissingCredentials(MissingCredentialsException e) {
        log.error(e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Busca de jogos indisponível.");
    }

    // Erro ao falar com a Twitch ou o IGDB (fora do ar, limite de chamadas, token inválido).
    @ExceptionHandler(RestClientException.class)
    public ProblemDetail handleUpstreamFailure(RestClientException e) {
        log.error("Falha ao consultar a Twitch/IGDB", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "Falha ao consultar o serviço de jogos.");
    }
}
