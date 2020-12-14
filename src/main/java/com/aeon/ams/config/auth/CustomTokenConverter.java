package com.aeon.ams.config.auth;
import com.aeon.ams.model.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Enhance token only not the response.
 * </p>
 */
public class CustomTokenConverter extends JwtAccessTokenConverter {
    private final TokenStore tokenStore;

    public CustomTokenConverter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        if(authentication.getOAuth2Request().getGrantType().equalsIgnoreCase("password")) {
            final Map<String, Object> additionalInfo = new HashMap<String, Object>();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            Long userId = Long.valueOf(details.get("userId").toString());
            User user =userDetails.getUser();
            additionalInfo.put("sid", user.getId());
            additionalInfo.put("name",user.getDisplayName());
            additionalInfo.put("authorities",user.getRoles());
            ((DefaultOAuth2AccessToken) accessToken)
                    .setAdditionalInformation(additionalInfo);
        }
        accessToken = super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
        return accessToken;
    }
}
