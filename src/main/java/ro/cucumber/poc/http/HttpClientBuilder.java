package ro.cucumber.poc.http;

// import cucumber.runtime.java.guice.ScenarioScoped;
import cucumber.runtime.java.guice.ScenarioScoped;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@ScenarioScoped
public class HttpClientBuilder {

    private SSLContext ctx;
    private Integer timeout;
    private HttpHost proxyHost;

    public HttpClientBuilder() {
        this.ctx = getContext();
        SSLContext.setDefault(ctx);
    }

    public CloseableHttpClient build() {
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        if (timeout != null) {
            configBuilder.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout);
        }
        if (proxyHost != null) {
            configBuilder.setProxy(proxyHost);
        }
        return HttpClients.custom().setSSLSocketFactory(getConnectionFactory(ctx))
                .setDefaultRequestConfig(configBuilder.build()).build();
    }

    private SSLContext getContext() {
        SSLContext ctx;
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()},
                    new SecureRandom());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return ctx;
    }

    private SSLConnectionSocketFactory getConnectionFactory(SSLContext ctx) {
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        return new SSLConnectionSocketFactory(ctx, allowAllHosts);
    }


    public HttpClientBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public HttpClientBuilder useProxy(String proxyHost, int proxyPort, String proxyScheme) {
        this.proxyHost = new HttpHost(proxyHost, proxyPort, proxyScheme);
        return this;
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}


