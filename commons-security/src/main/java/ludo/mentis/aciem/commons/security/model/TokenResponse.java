package ludo.mentis.aciem.commons.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenResponse that = (TokenResponse) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(tokenType, that.tokenType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType);
    }
}