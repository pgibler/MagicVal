package com.magicval.search;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;
import ch.boye.httpclientandroidlib.conn.ssl.SSLConnectionSocketFactory;
import ch.boye.httpclientandroidlib.conn.ssl.SSLContextBuilder;
import ch.boye.httpclientandroidlib.conn.ssl.TrustStrategy;
import ch.boye.httpclientandroidlib.impl.client.CloseableHttpClient;
import ch.boye.httpclientandroidlib.impl.client.HttpClientBuilder;
import ch.boye.httpclientandroidlib.impl.client.HttpClients;

/**
 * Functions for http.
 */
public class HttpFunctions {

    public static InputStream performHttpsRequest(HttpRequestBase request) throws IOException,
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        // Open an SSL connection
        SSLContextBuilder builder = new SSLContextBuilder();

        builder.loadTrustMaterial(null, new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                return true;
            }
        });

        SSLContext context = builder.build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                context);

        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(sslsf);
        CloseableHttpClient httpclient = clientBuilder.build();

        // Now execute it
        HttpResponse hr = httpclient.execute(request);

        // Get contents of response
        return hr.getEntity().getContent();
    }
}
