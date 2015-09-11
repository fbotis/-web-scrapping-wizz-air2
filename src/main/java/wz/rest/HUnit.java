package wz.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HUnit {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException,
			KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
		  SSLSocketFactory sslsf = new SSLSocketFactory(new TrustStrategy() {

		        public boolean isTrusted(
		                final X509Certificate[] chain, String authType) throws CertificateException {
		            // Oh, I am easy...
		            return true;
		        }

		    });
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			final HtmlPage page = webClient.getPage("https://wizzair.com/en-GB/FlightSearch");
			
			
			
			
			
			
			page.getElementById(
					"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_OriginStation")
					.setAttribute("value", "AES");
			page.getElementById(
					"ControlGroupRibbonAnonNewHomeView_AvailabilitySearchInputRibbonAnonNewHomeView_DestinationStation")
					.setAttribute("value", "GDN");
			page.getElementById(
					"ControlGroupRibbonAnonNewHomeView$AvailabilitySearchInputRibbonAnonNewHomeView$DepartureDate")
					.setAttribute("value", "12/09/2015");
			HtmlPage newPage = page
					.getElementById(
							"ControlGroupRibbonAnonNewHomeView$AvailabilitySearchInputRibbonAnonNewHomeView$ButtonSubmit")
					.click();
			
			
			System.out.println(page);

		}
	}
}
