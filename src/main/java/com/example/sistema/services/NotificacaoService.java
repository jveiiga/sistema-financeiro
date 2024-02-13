package com.example.sistema.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NotificacaoService {

    private final RestTemplate restTemplate;

    @Autowired
    public NotificacaoService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public void enviarNotificacao(String mensagem, String email) {

        String url = "https://webhook.site/5a417874-b03f-4dfb-8368-c75df28b7f8f?email=jeferson.veiiga@gmail.com" + email;
        String corpoRequisicao = "{ \"mensagem\": \"" + mensagem + "\" }";

        restTemplate.postForEntity(url, corpoRequisicao, String.class);
    }
}
