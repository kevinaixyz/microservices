package com.prototype.microservice.edge.security;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 *
 */
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final static Logger LOG = LoggerFactory.getLogger(CustomUserDetailsAuthenticationProvider.class);

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    /**
     *
     */
    public CustomUserDetailsAuthenticationProvider() {
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authToken) throws AuthenticationException {
        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format(
                    "additionalAuthenticationChecks for [{0}] with auth [{1}] -> secret [{2}] | password encoder [{3}]",
                    new Object[]{userDetails, authToken, authToken.getCredentials(), passwordEncoder}));
        }

        // Check: correct password
        String secret = (String) authToken.getCredentials();
        if (!passwordEncoder.matches(secret, userDetails.getPassword())) {
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format(
                        "Password mismatch for user [{0}] secret [{1}] hash [{2}]",
                        new Object[]{userDetails, secret, userDetails.getPassword()}));
            }
            throw new BadCredentialsException("Incorrect password");
        }

    }

    /* (non-Javadoc)
     * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#retrieveUser(java.lang.String, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format(
                        "Retrieved user details -> [{0}]",
                        new Object[]{userDetails}));
            }
            return userDetails;
        } catch (UsernameNotFoundException e) {
            String msg = MessageFormat.format(
                    "User [{0}] cannot be found due to: {1}",
                    new Object[]{username, e.getLocalizedMessage()});
            LOG.info(msg, e);
            throw new InternalAuthenticationServiceException(msg, e);
        } catch (Exception e) {
            String errMsg = MessageFormat.format(
                    "User [{0}] cannot be found due to error: {1}",
                    new Object[]{username, e.getLocalizedMessage()});
            LOG.error(errMsg, e);
            throw new InternalAuthenticationServiceException(errMsg, e);
        }
    }

}
