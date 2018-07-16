package frontend.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import frontend.util.TokenBuilder;

public class RunButtonHandler implements ActionListener {
	private UI ui;

	public RunButtonHandler(UI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ui.writeLog("Invoked service");

		try {			
			KeyStore keystore = getKeystore();
			if (keystore != null) {
				String token = new TokenBuilder().getToken(ui, keystore, keystore.aliases().nextElement(), ui.getPassword());

				HttpClient client = HttpClients.createDefault();
				StringEntity request = new StringEntity("{ \"name\": \"Henry\", \"token\": \"" + token + "\" }", ContentType.APPLICATION_JSON);

				HttpPost post = new HttpPost("http://localhost:8100/hello");
				post.setEntity(request);

				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					InputStream instream = entity.getContent();

					try {
						byte[] raw = IOUtils.toByteArray(instream);
						ui.writeLog(new String(raw));
					}
					finally {
						instream.close();
					}
				}
			}
		}
		catch (Exception ex) {
			ui.writeLog("Failed to connect to backend", ex);
		}
	}

	private KeyStore getKeystore() {
		KeyStore ks = null;

		try {
			ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream(ui.getFile()), ui.getPassword().toCharArray());
		}
		catch (GeneralSecurityException | IOException ex) {
			ui.writeLog("Could not load keystore with provided password", ex);
		}

		return ks;
	}
}
