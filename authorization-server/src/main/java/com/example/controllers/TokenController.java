package com.example.controllers;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class TokenController {

    @Resource(name = "createTokenStore")
    private TokenStore tokenStore;

    @RequestMapping(method = RequestMethod.GET, value = "/tokens/{clientId}")
    @ResponseBody
    public List<String> getTokens(@PathVariable String clientId) {

        System.out.println("getting tokens for client: " + clientId);

        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(clientId);
        Optional<Collection<OAuth2AccessToken>> optionalTokens = Optional.ofNullable(tokens);
        List<OAuth2AccessToken> tokensEmptyList = Collections.emptyList();
        Collection<OAuth2AccessToken> tokensCollection = optionalTokens.orElse(tokensEmptyList);
        Stream<String> tokenValuesStream = tokensCollection.stream().map(OAuth2AccessToken::getValue);
        Collector<String, ?, List<String>> stringsListCollector = Collectors.toList();
        List<String> tokenStrings = tokenValuesStream.collect(stringsListCollector);

        return tokenStrings;
    }
}
