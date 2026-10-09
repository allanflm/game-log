package com.allan.gamelog.backend.igdb;

// Lançada quando o backend subiu sem TWITCH_CLIENT_ID / TWITCH_CLIENT_SECRET. O servidor
// sobe mesmo assim (para os testes e o desenvolvimento rodarem), mas as buscas falham
// com uma mensagem clara em vez de um erro obscuro da Twitch.
public class MissingCredentialsException extends RuntimeException {

    public MissingCredentialsException() {
        super("Credenciais da Twitch não configuradas (TWITCH_CLIENT_ID e TWITCH_CLIENT_SECRET).");
    }
}
