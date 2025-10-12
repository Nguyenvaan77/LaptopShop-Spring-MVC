package com.basis.anhangda37.config;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.basis.anhangda37.domain.Cart;
import com.basis.anhangda37.domain.User;
import com.basis.anhangda37.service.CartService;
import com.basis.anhangda37.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomSuccessHandler implements AuthenticationSuccessHandler{
    @Autowired
    private UserService userService;

    @Autowired
    private CartService CartService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  

    protected String determineTargetUrl(final Authentication authentication) {
        Map<String, String> roleTargetUrlMap = new HashMap<>();

        roleTargetUrlMap.put("ROLE_ADMIN", "/admin");
        roleTargetUrlMap.put("ROLE_USER", "/");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for(final GrantedAuthority grantedAuthority : authorities) {   
            String authorityName = grantedAuthority.getAuthority();
            if(roleTargetUrlMap.containsKey(authorityName)) {
                return roleTargetUrlMap.get(authorityName);
            }
        }

        throw new IllegalStateException();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, Authentication authentication) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        String gmail = authentication.getName();
        User loginUser = userService.getUserByEmail(gmail);
        if(loginUser != null) {
            session.setAttribute("avatar", loginUser.getAvatar());
            session.setAttribute("fullName", loginUser.getFullName());
            session.setAttribute("email", loginUser.getEmail());

            Cart userCart = CartService.findCartByUser(loginUser);
            int sum = userCart.getSum();
            session.setAttribute("sum", sum);
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(authentication);

        if(response.isCommitted()) {
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request, authentication);
    }
    
}
