package dev.henriqueluiz.peoplemanager.web.response;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@AllArgsConstructor
public class TokenResponse {
    private String type;

    @JsonProperty("access_token")
    private Map<String, Object> accessToken;

    @JsonProperty("refresh_token")
    private Map<String, Object> refreshToken;
}
