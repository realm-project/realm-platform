package net.realmproject.platform.security.authentication.password;


import net.objectof.connector.Connector;
import net.objectof.connector.ConnectorException;
import net.objectof.corc.Action;
import net.objectof.corc.web.v2.HttpRequest;
import net.realmproject.platform.schema.Person;
import net.realmproject.platform.security.authentication.AbstractAuthenticator;
import net.realmproject.platform.util.RealmAuthentication;
import net.realmproject.platform.util.RealmResponse;


/**
 * Handles changing a user's password. Requires them to re-authenticate with
 * information described in the PasswordChange class
 * 
 * @author NAS
 *
 */

public class ChangePassword extends AbstractAuthenticator<ChangePasswordData> {

    public ChangePassword(Connector connector) throws ConnectorException {
        super(connector, ChangePasswordData.class);
    }

    @Override
    protected void onAuthenticate(Action action, HttpRequest request, Person person, ChangePasswordData info,
            boolean success) throws Exception {

        if (success) {

            String newSalt = RealmAuthentication.generateSalt();
            String hashedNewPassword = RealmAuthentication.hash(info.newPassword, newSalt);

            person.setPwdHashed(hashedNewPassword);
            person.setSalt(newSalt);

            person.tx().post();

        } else {
            // 401 Unauthorized:
            // "specifically for use when authentication is required and has failed or has not yet been provided"
            request.getHttpResponse().getWriter().print(new RealmResponse("Unauthorized"));
            request.getHttpResponse().setStatus(401);
        }
    }

}
