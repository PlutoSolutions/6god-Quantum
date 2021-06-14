// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.manager.Identify;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.Callable;
import javax.net.ssl.HttpsURLConnection;
import java.net.URLEncoder;
import java.io.Flushable;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.io.Writer;
import java.nio.CharBuffer;
import java.io.PrintStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.util.concurrent.atomic.AtomicReference;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import javax.net.ssl.SSLSession;
import java.security.GeneralSecurityException;
import java.io.IOException;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public class UtilTwo
{
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String ENCODING_GZIP = "gzip";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_SERVER = "Server";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";
    public static final String PARAM_CHARSET = "charset";
    private static final String BOUNDARY = "00content0boundary00";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary=00content0boundary00";
    private static final String CRLF = "\r\n";
    private static final String[] EMPTY_STRINGS;
    private static SSLSocketFactory TRUSTED_FACTORY;
    private static HostnameVerifier TRUSTED_VERIFIER;
    private static ConnectionFactory CONNECTION_FACTORY;
    private HttpURLConnection connection;
    private final URL url;
    private final String requestMethod;
    private RequestOutputStream output;
    private boolean multipart;
    private boolean form;
    private boolean ignoreCloseExceptions;
    private boolean uncompress;
    private int bufferSize;
    private long totalSize;
    private long totalWritten;
    private String httpProxyHost;
    private int httpProxyPort;
    private UploadProgress progress;
    
    private static String getValidCharset(final String charset) {
        if (charset != null && charset.length() > 0) {
            return charset;
        }
        return "UTF-8";
    }
    
    private static SSLSocketFactory getTrustedFactory() throws HttpRequestException {
        if (UtilTwo.TRUSTED_FACTORY == null) {
            final TrustManager[] trustAllCerts = { new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    
                    @Override
                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                    }
                    
                    @Override
                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                    }
                } };
            try {
                final SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new SecureRandom());
                UtilTwo.TRUSTED_FACTORY = context.getSocketFactory();
            }
            catch (GeneralSecurityException e) {
                final IOException ioException = new IOException("Security exception configuring SSL context");
                ioException.initCause(e);
                throw new HttpRequestException(ioException);
            }
        }
        return UtilTwo.TRUSTED_FACTORY;
    }
    
    private static HostnameVerifier getTrustedVerifier() {
        if (UtilTwo.TRUSTED_VERIFIER == null) {
            UtilTwo.TRUSTED_VERIFIER = new HostnameVerifier() {
                @Override
                public boolean verify(final String hostname, final SSLSession session) {
                    return true;
                }
            };
        }
        return UtilTwo.TRUSTED_VERIFIER;
    }
    
    private static StringBuilder addPathSeparator(final String baseUrl, final StringBuilder result) {
        if (baseUrl.indexOf(58) + 2 == baseUrl.lastIndexOf(47)) {
            result.append('/');
        }
        return result;
    }
    
    private static StringBuilder addParamPrefix(final String baseUrl, final StringBuilder result) {
        final int queryStart = baseUrl.indexOf(63);
        final int lastChar = result.length() - 1;
        if (queryStart == -1) {
            result.append('?');
        }
        else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&') {
            result.append('&');
        }
        return result;
    }
    
    private static StringBuilder addParam(final Object key, Object value, final StringBuilder result) {
        if (value != null && value.getClass().isArray()) {
            value = arrayToList(value);
        }
        if (value instanceof Iterable) {
            final Iterator<?> iterator = ((Iterable)value).iterator();
            while (iterator.hasNext()) {
                result.append(key);
                result.append("[]=");
                final Object element = iterator.next();
                if (element != null) {
                    result.append(element);
                }
                if (iterator.hasNext()) {
                    result.append("&");
                }
            }
        }
        else {
            result.append(key);
            result.append("=");
            if (value != null) {
                result.append(value);
            }
        }
        return result;
    }
    
    public static void setConnectionFactory(final ConnectionFactory connectionFactory) {
        if (connectionFactory == null) {
            UtilTwo.CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
        }
        else {
            UtilTwo.CONNECTION_FACTORY = connectionFactory;
        }
    }
    
    private static List<Object> arrayToList(final Object array) {
        if (array instanceof Object[]) {
            return Arrays.asList((Object[])array);
        }
        final List<Object> result = new ArrayList<Object>();
        if (array instanceof int[]) {
            for (final int value : (int[])array) {
                result.add(value);
            }
        }
        else if (array instanceof boolean[]) {
            for (final boolean value2 : (boolean[])array) {
                result.add(value2);
            }
        }
        else if (array instanceof long[]) {
            for (final long value3 : (long[])array) {
                result.add(value3);
            }
        }
        else if (array instanceof float[]) {
            for (final float value4 : (float[])array) {
                result.add(value4);
            }
        }
        else if (array instanceof double[]) {
            for (final double value5 : (double[])array) {
                result.add(value5);
            }
        }
        else if (array instanceof short[]) {
            for (final short value6 : (short[])array) {
                result.add(value6);
            }
        }
        else if (array instanceof byte[]) {
            for (final byte value7 : (byte[])array) {
                result.add(value7);
            }
        }
        else if (array instanceof char[]) {
            for (final char value8 : (char[])array) {
                result.add(value8);
            }
        }
        return result;
    }
    
    public static String encode(final CharSequence url) throws HttpRequestException {
        URL parsed;
        try {
            parsed = new URL(url.toString());
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        String host = parsed.getHost();
        final int port = parsed.getPort();
        if (port != -1) {
            host = host + ':' + Integer.toString(port);
        }
        try {
            String encoded = new URI(parsed.getProtocol(), host, parsed.getPath(), parsed.getQuery(), null).toASCIIString();
            final int paramsStart = encoded.indexOf(63);
            if (paramsStart > 0 && paramsStart + 1 < encoded.length()) {
                encoded = encoded.substring(0, paramsStart + 1) + encoded.substring(paramsStart + 1).replace("+", "%2B");
            }
            return encoded;
        }
        catch (URISyntaxException e2) {
            final IOException io = new IOException("Parsing URI failed");
            io.initCause(e2);
            throw new HttpRequestException(io);
        }
    }
    
    public static String append(final CharSequence url, final Map<?, ?> params) {
        final String baseUrl = url.toString();
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        final StringBuilder result = new StringBuilder(baseUrl);
        addPathSeparator(baseUrl, result);
        addParamPrefix(baseUrl, result);
        final Iterator<?> iterator = params.entrySet().iterator();
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)iterator.next();
        addParam(entry.getKey().toString(), entry.getValue(), result);
        while (iterator.hasNext()) {
            result.append('&');
            entry = (Map.Entry<?, ?>)iterator.next();
            addParam(entry.getKey().toString(), entry.getValue(), result);
        }
        return result.toString();
    }
    
    public static String append(final CharSequence url, final Object... params) {
        final String baseUrl = url.toString();
        if (params == null || params.length == 0) {
            return baseUrl;
        }
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Must specify an even number of parameter names/values");
        }
        final StringBuilder result = new StringBuilder(baseUrl);
        addPathSeparator(baseUrl, result);
        addParamPrefix(baseUrl, result);
        addParam(params[0], params[1], result);
        for (int i = 2; i < params.length; i += 2) {
            result.append('&');
            addParam(params[i], params[i + 1], result);
        }
        return result.toString();
    }
    
    public static UtilTwo get(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "GET");
    }
    
    public static UtilTwo get(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "GET");
    }
    
    public static UtilTwo get(final CharSequence baseUrl, final Map<?, ?> params, final boolean encode) {
        final String url = append(baseUrl, params);
        return get(encode ? encode(url) : url);
    }
    
    public static UtilTwo get(final CharSequence baseUrl, final boolean encode, final Object... params) {
        final String url = append(baseUrl, params);
        return get(encode ? encode(url) : url);
    }
    
    public static UtilTwo post(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "POST");
    }
    
    public static UtilTwo post(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "POST");
    }
    
    public static UtilTwo post(final CharSequence baseUrl, final Map<?, ?> params, final boolean encode) {
        final String url = append(baseUrl, params);
        return post(encode ? encode(url) : url);
    }
    
    public static UtilTwo post(final CharSequence baseUrl, final boolean encode, final Object... params) {
        final String url = append(baseUrl, params);
        return post(encode ? encode(url) : url);
    }
    
    public static UtilTwo patch(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "PATCH");
    }
    
    public static UtilTwo put(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "PUT");
    }
    
    public static UtilTwo put(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "PUT");
    }
    
    public static UtilTwo put(final CharSequence baseUrl, final Map<?, ?> params, final boolean encode) {
        final String url = append(baseUrl, params);
        return put(encode ? encode(url) : url);
    }
    
    public static UtilTwo put(final CharSequence baseUrl, final boolean encode, final Object... params) {
        final String url = append(baseUrl, params);
        return put(encode ? encode(url) : url);
    }
    
    public static UtilTwo delete(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "DELETE");
    }
    
    public static UtilTwo delete(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "DELETE");
    }
    
    public static UtilTwo delete(final CharSequence baseUrl, final Map<?, ?> params, final boolean encode) {
        final String url = append(baseUrl, params);
        return delete(encode ? encode(url) : url);
    }
    
    public static UtilTwo delete(final CharSequence baseUrl, final boolean encode, final Object... params) {
        final String url = append(baseUrl, params);
        return delete(encode ? encode(url) : url);
    }
    
    public static UtilTwo head(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "HEAD");
    }
    
    public static UtilTwo head(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "HEAD");
    }
    
    public static UtilTwo head(final CharSequence baseUrl, final Map<?, ?> params, final boolean encode) {
        final String url = append(baseUrl, params);
        return head(encode ? encode(url) : url);
    }
    
    public static UtilTwo head(final CharSequence baseUrl, final boolean encode, final Object... params) {
        final String url = append(baseUrl, params);
        return head(encode ? encode(url) : url);
    }
    
    public static UtilTwo options(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "OPTIONS");
    }
    
    public static UtilTwo options(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "OPTIONS");
    }
    
    public static UtilTwo trace(final CharSequence url) throws HttpRequestException {
        return new UtilTwo(url, "TRACE");
    }
    
    public static UtilTwo trace(final URL url) throws HttpRequestException {
        return new UtilTwo(url, "TRACE");
    }
    
    public static void keepAlive(final boolean keepAlive) {
        setProperty("http.keepAlive", Boolean.toString(keepAlive));
    }
    
    public static void maxConnections(final int maxConnections) {
        setProperty("http.maxConnections", Integer.toString(maxConnections));
    }
    
    public static void proxyHost(final String host) {
        setProperty("http.proxyHost", host);
        setProperty("https.proxyHost", host);
    }
    
    public static void proxyPort(final int port) {
        final String portValue = Integer.toString(port);
        setProperty("http.proxyPort", portValue);
        setProperty("https.proxyPort", portValue);
    }
    
    public static void nonProxyHosts(final String... hosts) {
        if (hosts != null && hosts.length > 0) {
            final StringBuilder separated = new StringBuilder();
            final int last = hosts.length - 1;
            for (int i = 0; i < last; ++i) {
                separated.append(hosts[i]).append('|');
            }
            separated.append(hosts[last]);
            setProperty("http.nonProxyHosts", separated.toString());
        }
        else {
            setProperty("http.nonProxyHosts", null);
        }
    }
    
    private static String setProperty(final String name, final String value) {
        PrivilegedAction<String> action;
        if (value != null) {
            action = new PrivilegedAction<String>() {
                @Override
                public String run() {
                    return System.setProperty(name, value);
                }
            };
        }
        else {
            action = new PrivilegedAction<String>() {
                @Override
                public String run() {
                    return System.clearProperty(name);
                }
            };
        }
        return AccessController.doPrivileged(action);
    }
    
    public UtilTwo(final CharSequence url, final String method) throws HttpRequestException {
        this.connection = null;
        this.ignoreCloseExceptions = true;
        this.uncompress = false;
        this.bufferSize = 8192;
        this.totalSize = -1L;
        this.totalWritten = 0L;
        this.progress = UploadProgress.DEFAULT;
        try {
            this.url = new URL(url.toString());
        }
        catch (MalformedURLException e) {
            throw new HttpRequestException(e);
        }
        this.requestMethod = method;
    }
    
    public UtilTwo(final URL url, final String method) throws HttpRequestException {
        this.connection = null;
        this.ignoreCloseExceptions = true;
        this.uncompress = false;
        this.bufferSize = 8192;
        this.totalSize = -1L;
        this.totalWritten = 0L;
        this.progress = UploadProgress.DEFAULT;
        this.url = url;
        this.requestMethod = method;
    }
    
    private Proxy createProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.httpProxyHost, this.httpProxyPort));
    }
    
    private HttpURLConnection createConnection() {
        try {
            HttpURLConnection connection;
            if (this.httpProxyHost != null) {
                connection = UtilTwo.CONNECTION_FACTORY.create(this.url, this.createProxy());
            }
            else {
                connection = UtilTwo.CONNECTION_FACTORY.create(this.url);
            }
            connection.setRequestMethod(this.requestMethod);
            return connection;
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    @Override
    public String toString() {
        return this.method() + ' ' + this.url();
    }
    
    public HttpURLConnection getConnection() {
        if (this.connection == null) {
            this.connection = this.createConnection();
        }
        return this.connection;
    }
    
    public UtilTwo ignoreCloseExceptions(final boolean ignore) {
        this.ignoreCloseExceptions = ignore;
        return this;
    }
    
    public boolean ignoreCloseExceptions() {
        return this.ignoreCloseExceptions;
    }
    
    public int code() throws HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseCode();
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public UtilTwo code(final AtomicInteger output) throws HttpRequestException {
        output.set(this.code());
        return this;
    }
    
    public boolean ok() throws HttpRequestException {
        return 200 == this.code();
    }
    
    public boolean created() throws HttpRequestException {
        return 201 == this.code();
    }
    
    public boolean noContent() throws HttpRequestException {
        return 204 == this.code();
    }
    
    public boolean serverError() throws HttpRequestException {
        return 500 == this.code();
    }
    
    public boolean badRequest() throws HttpRequestException {
        return 400 == this.code();
    }
    
    public boolean notFound() throws HttpRequestException {
        return 404 == this.code();
    }
    
    public boolean notModified() throws HttpRequestException {
        return 304 == this.code();
    }
    
    public String message() throws HttpRequestException {
        try {
            this.closeOutput();
            return this.getConnection().getResponseMessage();
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public UtilTwo disconnect() {
        this.getConnection().disconnect();
        return this;
    }
    
    public UtilTwo chunk(final int size) {
        this.getConnection().setChunkedStreamingMode(size);
        return this;
    }
    
    public UtilTwo bufferSize(final int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
        this.bufferSize = size;
        return this;
    }
    
    public int bufferSize() {
        return this.bufferSize;
    }
    
    public UtilTwo uncompress(final boolean uncompress) {
        this.uncompress = uncompress;
        return this;
    }
    
    protected ByteArrayOutputStream byteStream() {
        final int size = this.contentLength();
        if (size > 0) {
            return new ByteArrayOutputStream(size);
        }
        return new ByteArrayOutputStream();
    }
    
    public String body(final String charset) throws HttpRequestException {
        final ByteArrayOutputStream output = this.byteStream();
        try {
            this.copy(this.buffer(), output);
            return output.toString(getValidCharset(charset));
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public String body() throws HttpRequestException {
        return this.body(this.charset());
    }
    
    public UtilTwo body(final AtomicReference<String> output) throws HttpRequestException {
        output.set(this.body());
        return this;
    }
    
    public UtilTwo body(final AtomicReference<String> output, final String charset) throws HttpRequestException {
        output.set(this.body(charset));
        return this;
    }
    
    public boolean isBodyEmpty() throws HttpRequestException {
        return this.contentLength() == 0;
    }
    
    public byte[] bytes() throws HttpRequestException {
        final ByteArrayOutputStream output = this.byteStream();
        try {
            this.copy(this.buffer(), output);
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return output.toByteArray();
    }
    
    public BufferedInputStream buffer() throws HttpRequestException {
        return new BufferedInputStream(this.stream(), this.bufferSize);
    }
    
    public InputStream stream() throws HttpRequestException {
        InputStream stream = null;
        Label_0082: {
            if (this.code() < 400) {
                try {
                    stream = this.getConnection().getInputStream();
                    break Label_0082;
                }
                catch (IOException e) {
                    throw new HttpRequestException(e);
                }
            }
            stream = this.getConnection().getErrorStream();
            if (stream == null) {
                try {
                    stream = this.getConnection().getInputStream();
                }
                catch (IOException e) {
                    if (this.contentLength() > 0) {
                        throw new HttpRequestException(e);
                    }
                    stream = new ByteArrayInputStream(new byte[0]);
                }
            }
        }
        if (!this.uncompress || !"gzip".equals(this.contentEncoding())) {
            return stream;
        }
        try {
            return new GZIPInputStream(stream);
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public InputStreamReader reader(final String charset) throws HttpRequestException {
        try {
            return new InputStreamReader(this.stream(), getValidCharset(charset));
        }
        catch (UnsupportedEncodingException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public InputStreamReader reader() throws HttpRequestException {
        return this.reader(this.charset());
    }
    
    public BufferedReader bufferedReader(final String charset) throws HttpRequestException {
        return new BufferedReader(this.reader(charset), this.bufferSize);
    }
    
    public BufferedReader bufferedReader() throws HttpRequestException {
        return this.bufferedReader(this.charset());
    }
    
    public UtilTwo receive(final File file) throws HttpRequestException {
        OutputStream output;
        try {
            output = new BufferedOutputStream(new FileOutputStream(file), this.bufferSize);
        }
        catch (FileNotFoundException e) {
            throw new HttpRequestException(e);
        }
        return new CloseOperation<UtilTwo>(output, this.ignoreCloseExceptions) {
            @Override
            protected UtilTwo run() throws HttpRequestException, IOException {
                return UtilTwo.this.receive(output);
            }
        }.call();
    }
    
    public UtilTwo receive(final OutputStream output) throws HttpRequestException {
        try {
            return this.copy(this.buffer(), output);
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public UtilTwo receive(final PrintStream output) throws HttpRequestException {
        return this.receive((OutputStream)output);
    }
    
    public UtilTwo receive(final Appendable appendable) throws HttpRequestException {
        final BufferedReader reader = this.bufferedReader();
        return new CloseOperation<UtilTwo>(reader, this.ignoreCloseExceptions) {
            public UtilTwo run() throws IOException {
                final CharBuffer buffer = CharBuffer.allocate(UtilTwo.this.bufferSize);
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    buffer.rewind();
                    appendable.append(buffer, 0, read);
                    buffer.rewind();
                }
                return UtilTwo.this;
            }
        }.call();
    }
    
    public UtilTwo receive(final Writer writer) throws HttpRequestException {
        final BufferedReader reader = this.bufferedReader();
        return new CloseOperation<UtilTwo>(reader, this.ignoreCloseExceptions) {
            public UtilTwo run() throws IOException {
                return UtilTwo.this.copy(reader, writer);
            }
        }.call();
    }
    
    public UtilTwo readTimeout(final int timeout) {
        this.getConnection().setReadTimeout(timeout);
        return this;
    }
    
    public UtilTwo connectTimeout(final int timeout) {
        this.getConnection().setConnectTimeout(timeout);
        return this;
    }
    
    public UtilTwo header(final String name, final String value) {
        this.getConnection().setRequestProperty(name, value);
        return this;
    }
    
    public UtilTwo header(final String name, final Number value) {
        return this.header(name, (value != null) ? value.toString() : null);
    }
    
    public UtilTwo headers(final Map<String, String> headers) {
        if (!headers.isEmpty()) {
            for (final Map.Entry<String, String> header : headers.entrySet()) {
                this.header(header);
            }
        }
        return this;
    }
    
    public UtilTwo header(final Map.Entry<String, String> header) {
        return this.header(header.getKey(), header.getValue());
    }
    
    public String header(final String name) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderField(name);
    }
    
    public Map<String, List<String>> headers() throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFields();
    }
    
    public long dateHeader(final String name) throws HttpRequestException {
        return this.dateHeader(name, -1L);
    }
    
    public long dateHeader(final String name, final long defaultValue) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldDate(name, defaultValue);
    }
    
    public int intHeader(final String name) throws HttpRequestException {
        return this.intHeader(name, -1);
    }
    
    public int intHeader(final String name, final int defaultValue) throws HttpRequestException {
        this.closeOutputQuietly();
        return this.getConnection().getHeaderFieldInt(name, defaultValue);
    }
    
    public String[] headers(final String name) {
        final Map<String, List<String>> headers = this.headers();
        if (headers == null || headers.isEmpty()) {
            return UtilTwo.EMPTY_STRINGS;
        }
        final List<String> values = headers.get(name);
        if (values != null && !values.isEmpty()) {
            return values.toArray(new String[values.size()]);
        }
        return UtilTwo.EMPTY_STRINGS;
    }
    
    public String parameter(final String headerName, final String paramName) {
        return this.getParam(this.header(headerName), paramName);
    }
    
    public Map<String, String> parameters(final String headerName) {
        return this.getParams(this.header(headerName));
    }
    
    protected Map<String, String> getParams(final String header) {
        if (header == null || header.length() == 0) {
            return Collections.emptyMap();
        }
        final int headerLength = header.length();
        int start = header.indexOf(59) + 1;
        if (start == 0 || start == headerLength) {
            return Collections.emptyMap();
        }
        int end = header.indexOf(59, start);
        if (end == -1) {
            end = headerLength;
        }
        final Map<String, String> params = new LinkedHashMap<String, String>();
        while (start < end) {
            final int nameEnd = header.indexOf(61, start);
            if (nameEnd != -1 && nameEnd < end) {
                final String name = header.substring(start, nameEnd).trim();
                if (name.length() > 0) {
                    final String value = header.substring(nameEnd + 1, end).trim();
                    final int length = value.length();
                    if (length != 0) {
                        if (length > 2 && '\"' == value.charAt(0) && '\"' == value.charAt(length - 1)) {
                            params.put(name, value.substring(1, length - 1));
                        }
                        else {
                            params.put(name, value);
                        }
                    }
                }
            }
            start = end + 1;
            end = header.indexOf(59, start);
            if (end == -1) {
                end = headerLength;
            }
        }
        return params;
    }
    
    protected String getParam(final String value, final String paramName) {
        if (value == null || value.length() == 0) {
            return null;
        }
        final int length = value.length();
        int start = value.indexOf(59) + 1;
        if (start == 0 || start == length) {
            return null;
        }
        int end = value.indexOf(59, start);
        if (end == -1) {
            end = length;
        }
        while (start < end) {
            final int nameEnd = value.indexOf(61, start);
            if (nameEnd != -1 && nameEnd < end && paramName.equals(value.substring(start, nameEnd).trim())) {
                final String paramValue = value.substring(nameEnd + 1, end).trim();
                final int valueLength = paramValue.length();
                if (valueLength != 0) {
                    if (valueLength > 2 && '\"' == paramValue.charAt(0) && '\"' == paramValue.charAt(valueLength - 1)) {
                        return paramValue.substring(1, valueLength - 1);
                    }
                    return paramValue;
                }
            }
            start = end + 1;
            end = value.indexOf(59, start);
            if (end == -1) {
                end = length;
            }
        }
        return null;
    }
    
    public String charset() {
        return this.parameter("Content-Type", "charset");
    }
    
    public UtilTwo userAgent(final String userAgent) {
        return this.header("User-Agent", userAgent);
    }
    
    public UtilTwo referer(final String referer) {
        return this.header("Referer", referer);
    }
    
    public UtilTwo useCaches(final boolean useCaches) {
        this.getConnection().setUseCaches(useCaches);
        return this;
    }
    
    public UtilTwo acceptEncoding(final String acceptEncoding) {
        return this.header("Accept-Encoding", acceptEncoding);
    }
    
    public UtilTwo acceptGzipEncoding() {
        return this.acceptEncoding("gzip");
    }
    
    public UtilTwo acceptCharset(final String acceptCharset) {
        return this.header("Accept-Charset", acceptCharset);
    }
    
    public String contentEncoding() {
        return this.header("Content-Encoding");
    }
    
    public String server() {
        return this.header("Server");
    }
    
    public long date() {
        return this.dateHeader("Date");
    }
    
    public String cacheControl() {
        return this.header("Cache-Control");
    }
    
    public String eTag() {
        return this.header("ETag");
    }
    
    public long expires() {
        return this.dateHeader("Expires");
    }
    
    public long lastModified() {
        return this.dateHeader("Last-Modified");
    }
    
    public String location() {
        return this.header("Location");
    }
    
    public UtilTwo authorization(final String authorization) {
        return this.header("Authorization", authorization);
    }
    
    public UtilTwo proxyAuthorization(final String proxyAuthorization) {
        return this.header("Proxy-Authorization", proxyAuthorization);
    }
    
    public UtilTwo basic(final String name, final String password) {
        return this.authorization("Basic " + Base64.encode(name + ':' + password));
    }
    
    public UtilTwo proxyBasic(final String name, final String password) {
        return this.proxyAuthorization("Basic " + Base64.encode(name + ':' + password));
    }
    
    public UtilTwo ifModifiedSince(final long ifModifiedSince) {
        this.getConnection().setIfModifiedSince(ifModifiedSince);
        return this;
    }
    
    public UtilTwo ifNoneMatch(final String ifNoneMatch) {
        return this.header("If-None-Match", ifNoneMatch);
    }
    
    public UtilTwo contentType(final String contentType) {
        return this.contentType(contentType, null);
    }
    
    public UtilTwo contentType(final String contentType, final String charset) {
        if (charset != null && charset.length() > 0) {
            final String separator = "; charset=";
            return this.header("Content-Type", contentType + "; charset=" + charset);
        }
        return this.header("Content-Type", contentType);
    }
    
    public String contentType() {
        return this.header("Content-Type");
    }
    
    public int contentLength() {
        return this.intHeader("Content-Length");
    }
    
    public UtilTwo contentLength(final String contentLength) {
        return this.contentLength(Integer.parseInt(contentLength));
    }
    
    public UtilTwo contentLength(final int contentLength) {
        this.getConnection().setFixedLengthStreamingMode(contentLength);
        return this;
    }
    
    public UtilTwo accept(final String accept) {
        return this.header("Accept", accept);
    }
    
    public UtilTwo acceptJson() {
        return this.accept("application/json");
    }
    
    protected UtilTwo copy(final InputStream input, final OutputStream output) throws IOException {
        return new CloseOperation<UtilTwo>(input, this.ignoreCloseExceptions) {
            public UtilTwo run() throws IOException {
                final byte[] buffer = new byte[UtilTwo.this.bufferSize];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    UtilTwo.this.totalWritten += read;
                    UtilTwo.this.progress.onUpload(UtilTwo.this.totalWritten, UtilTwo.this.totalSize);
                }
                return UtilTwo.this;
            }
        }.call();
    }
    
    protected UtilTwo copy(final Reader input, final Writer output) throws IOException {
        return new CloseOperation<UtilTwo>(input, this.ignoreCloseExceptions) {
            public UtilTwo run() throws IOException {
                final char[] buffer = new char[UtilTwo.this.bufferSize];
                int read;
                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                    UtilTwo.this.totalWritten += read;
                    UtilTwo.this.progress.onUpload(UtilTwo.this.totalWritten, -1L);
                }
                return UtilTwo.this;
            }
        }.call();
    }
    
    public UtilTwo progress(final UploadProgress callback) {
        if (callback == null) {
            this.progress = UploadProgress.DEFAULT;
        }
        else {
            this.progress = callback;
        }
        return this;
    }
    
    private UtilTwo incrementTotalSize(final long size) {
        if (this.totalSize == -1L) {
            this.totalSize = 0L;
        }
        this.totalSize += size;
        return this;
    }
    
    protected UtilTwo closeOutput() throws IOException {
        this.progress(null);
        if (this.output == null) {
            return this;
        }
        if (this.multipart) {
            this.output.write("\r\n--00content0boundary00--\r\n");
        }
        if (this.ignoreCloseExceptions) {
            try {
                this.output.close();
            }
            catch (IOException ex) {}
        }
        else {
            this.output.close();
        }
        this.output = null;
        return this;
    }
    
    protected UtilTwo closeOutputQuietly() throws HttpRequestException {
        try {
            return this.closeOutput();
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    protected UtilTwo openOutput() throws IOException {
        if (this.output != null) {
            return this;
        }
        this.getConnection().setDoOutput(true);
        final String charset = this.getParam(this.getConnection().getRequestProperty("Content-Type"), "charset");
        this.output = new RequestOutputStream(this.getConnection().getOutputStream(), charset, this.bufferSize);
        return this;
    }
    
    protected UtilTwo startPart() throws IOException {
        if (!this.multipart) {
            this.multipart = true;
            this.contentType("multipart/form-data; boundary=00content0boundary00").openOutput();
            this.output.write("--00content0boundary00\r\n");
        }
        else {
            this.output.write("\r\n--00content0boundary00\r\n");
        }
        return this;
    }
    
    protected UtilTwo writePartHeader(final String name, final String filename) throws IOException {
        return this.writePartHeader(name, filename, null);
    }
    
    protected UtilTwo writePartHeader(final String name, final String filename, final String contentType) throws IOException {
        final StringBuilder partBuffer = new StringBuilder();
        partBuffer.append("form-data; name=\"").append(name);
        if (filename != null) {
            partBuffer.append("\"; filename=\"").append(filename);
        }
        partBuffer.append('\"');
        this.partHeader("Content-Disposition", partBuffer.toString());
        if (contentType != null) {
            this.partHeader("Content-Type", contentType);
        }
        return this.send("\r\n");
    }
    
    public UtilTwo part(final String name, final String part) {
        return this.part(name, null, part);
    }
    
    public UtilTwo part(final String name, final String filename, final String part) throws HttpRequestException {
        return this.part(name, filename, null, part);
    }
    
    public UtilTwo part(final String name, final String filename, final String contentType, final String part) throws HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(name, filename, contentType);
            this.output.write(part);
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return this;
    }
    
    public UtilTwo part(final String name, final Number part) throws HttpRequestException {
        return this.part(name, null, part);
    }
    
    public UtilTwo part(final String name, final String filename, final Number part) throws HttpRequestException {
        return this.part(name, filename, (part != null) ? part.toString() : null);
    }
    
    public UtilTwo part(final String name, final File part) throws HttpRequestException {
        return this.part(name, null, part);
    }
    
    public UtilTwo part(final String name, final String filename, final File part) throws HttpRequestException {
        return this.part(name, filename, null, part);
    }
    
    public UtilTwo part(final String name, final String filename, final String contentType, final File part) throws HttpRequestException {
        InputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(part));
            this.incrementTotalSize(part.length());
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return this.part(name, filename, contentType, stream);
    }
    
    public UtilTwo part(final String name, final InputStream part) throws HttpRequestException {
        return this.part(name, null, null, part);
    }
    
    public UtilTwo part(final String name, final String filename, final String contentType, final InputStream part) throws HttpRequestException {
        try {
            this.startPart();
            this.writePartHeader(name, filename, contentType);
            this.copy(part, this.output);
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return this;
    }
    
    public UtilTwo partHeader(final String name, final String value) throws HttpRequestException {
        return this.send(name).send(": ").send(value).send("\r\n");
    }
    
    public UtilTwo send(final File input) throws HttpRequestException {
        InputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(input));
            this.incrementTotalSize(input.length());
        }
        catch (FileNotFoundException e) {
            throw new HttpRequestException(e);
        }
        return this.send(stream);
    }
    
    public UtilTwo send(final byte[] input) throws HttpRequestException {
        if (input != null) {
            this.incrementTotalSize(input.length);
        }
        return this.send(new ByteArrayInputStream(input));
    }
    
    public UtilTwo send(final InputStream input) throws HttpRequestException {
        try {
            this.openOutput();
            this.copy(input, this.output);
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return this;
    }
    
    public UtilTwo send(final Reader input) throws HttpRequestException {
        try {
            this.openOutput();
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        final Writer writer = new OutputStreamWriter(this.output, this.output.encoder.charset());
        return new FlushOperation<UtilTwo>(writer) {
            @Override
            protected UtilTwo run() throws IOException {
                return UtilTwo.this.copy(input, writer);
            }
        }.call();
    }
    
    public UtilTwo send(final CharSequence value) throws HttpRequestException {
        try {
            this.openOutput();
            this.output.write(value.toString());
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return this;
    }
    
    public OutputStreamWriter writer() throws HttpRequestException {
        try {
            this.openOutput();
            return new OutputStreamWriter(this.output, this.output.encoder.charset());
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }
    
    public UtilTwo form(final Map<?, ?> values) throws HttpRequestException {
        return this.form(values, "UTF-8");
    }
    
    public UtilTwo form(final Map.Entry<?, ?> entry) throws HttpRequestException {
        return this.form(entry, "UTF-8");
    }
    
    public UtilTwo form(final Map.Entry<?, ?> entry, final String charset) throws HttpRequestException {
        return this.form(entry.getKey(), entry.getValue(), charset);
    }
    
    public UtilTwo form(final Object name, final Object value) throws HttpRequestException {
        return this.form(name, value, "UTF-8");
    }
    
    public UtilTwo form(final Object name, final Object value, String charset) throws HttpRequestException {
        final boolean first = !this.form;
        if (first) {
            this.contentType("application/x-www-form-urlencoded", charset);
            this.form = true;
        }
        charset = getValidCharset(charset);
        try {
            this.openOutput();
            if (!first) {
                this.output.write(38);
            }
            this.output.write(URLEncoder.encode(name.toString(), charset));
            this.output.write(61);
            if (value != null) {
                this.output.write(URLEncoder.encode(value.toString(), charset));
            }
        }
        catch (IOException e) {
            throw new HttpRequestException(e);
        }
        return this;
    }
    
    public UtilTwo form(final Map<?, ?> values, final String charset) throws HttpRequestException {
        if (!values.isEmpty()) {
            for (final Map.Entry<?, ?> entry : values.entrySet()) {
                this.form(entry, charset);
            }
        }
        return this;
    }
    
    public UtilTwo trustAllCerts() throws HttpRequestException {
        final HttpURLConnection connection = this.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(getTrustedFactory());
        }
        return this;
    }
    
    public UtilTwo trustAllHosts() {
        final HttpURLConnection connection = this.getConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setHostnameVerifier(getTrustedVerifier());
        }
        return this;
    }
    
    public URL url() {
        return this.getConnection().getURL();
    }
    
    public String method() {
        return this.getConnection().getRequestMethod();
    }
    
    public UtilTwo useProxy(final String proxyHost, final int proxyPort) {
        if (this.connection != null) {
            throw new IllegalStateException("The connection has already been created. This method must be called before reading or writing to the request.");
        }
        this.httpProxyHost = proxyHost;
        this.httpProxyPort = proxyPort;
        return this;
    }
    
    public UtilTwo followRedirects(final boolean followRedirects) {
        this.getConnection().setInstanceFollowRedirects(followRedirects);
        return this;
    }
    
    static {
        EMPTY_STRINGS = new String[0];
        UtilTwo.CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
    }
    
    public interface ConnectionFactory
    {
        public static final ConnectionFactory DEFAULT = new ConnectionFactory() {
            @Override
            public HttpURLConnection create(final URL url) throws IOException {
                return (HttpURLConnection)url.openConnection();
            }
            
            @Override
            public HttpURLConnection create(final URL url, final Proxy proxy) throws IOException {
                return (HttpURLConnection)url.openConnection(proxy);
            }
        };
        
        HttpURLConnection create(final URL p0) throws IOException;
        
        HttpURLConnection create(final URL p0, final Proxy p1) throws IOException;
    }
    
    public interface UploadProgress
    {
        public static final UploadProgress DEFAULT = new UploadProgress() {
            @Override
            public void onUpload(final long uploaded, final long total) {
            }
        };
        
        void onUpload(final long p0, final long p1);
    }
    
    public static class Base64
    {
        private static final byte EQUALS_SIGN = 61;
        private static final String PREFERRED_ENCODING = "US-ASCII";
        private static final byte[] _STANDARD_ALPHABET;
        
        private Base64() {
        }
        
        private static byte[] encode3to4(final byte[] source, final int srcOffset, final int numSigBytes, final byte[] destination, final int destOffset) {
            final byte[] ALPHABET = Base64._STANDARD_ALPHABET;
            final int inBuff = ((numSigBytes > 0) ? (source[srcOffset] << 24 >>> 8) : 0) | ((numSigBytes > 1) ? (source[srcOffset + 1] << 24 >>> 16) : 0) | ((numSigBytes > 2) ? (source[srcOffset + 2] << 24 >>> 24) : 0);
            switch (numSigBytes) {
                case 3: {
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                    destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                    destination[destOffset + 3] = ALPHABET[inBuff & 0x3F];
                    return destination;
                }
                case 2: {
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                    destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 0x3F];
                    destination[destOffset + 3] = 61;
                    return destination;
                }
                case 1: {
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 0x3F];
                    destination[destOffset + 3] = (destination[destOffset + 2] = 61);
                    return destination;
                }
                default: {
                    return destination;
                }
            }
        }
        
        public static String encode(final String string) {
            byte[] bytes;
            try {
                bytes = string.getBytes("US-ASCII");
            }
            catch (UnsupportedEncodingException e) {
                bytes = string.getBytes();
            }
            return encodeBytes(bytes);
        }
        
        public static String encodeBytes(final byte[] source) {
            return encodeBytes(source, 0, source.length);
        }
        
        public static String encodeBytes(final byte[] source, final int off, final int len) {
            final byte[] encoded = encodeBytesToBytes(source, off, len);
            try {
                return new String(encoded, "US-ASCII");
            }
            catch (UnsupportedEncodingException uue) {
                return new String(encoded);
            }
        }
        
        public static byte[] encodeBytesToBytes(final byte[] source, final int off, final int len) {
            if (source == null) {
                throw new NullPointerException("Cannot serialize a null array.");
            }
            if (off < 0) {
                throw new IllegalArgumentException("Cannot have negative offset: " + off);
            }
            if (len < 0) {
                throw new IllegalArgumentException("Cannot have length offset: " + len);
            }
            if (off + len > source.length) {
                throw new IllegalArgumentException(String.format("Cannot have offset of %d and length of %d with array of length %d", off, len, source.length));
            }
            final int encLen = len / 3 * 4 + ((len % 3 > 0) ? 4 : 0);
            final byte[] outBuff = new byte[encLen];
            int d = 0;
            int e = 0;
            for (int len2 = len - 2; d < len2; d += 3, e += 4) {
                encode3to4(source, d + off, 3, outBuff, e);
            }
            if (d < len) {
                encode3to4(source, d + off, len - d, outBuff, e);
                e += 4;
            }
            if (e <= outBuff.length - 1) {
                final byte[] finalOut = new byte[e];
                System.arraycopy(outBuff, 0, finalOut, 0, e);
                return finalOut;
            }
            return outBuff;
        }
        
        static {
            _STANDARD_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        }
    }
    
    public static class HttpRequestException extends RuntimeException
    {
        private static final long serialVersionUID = -1170466989781746231L;
        
        public HttpRequestException(final IOException cause) {
            super(cause);
        }
        
        @Override
        public IOException getCause() {
            return (IOException)super.getCause();
        }
    }
    
    protected abstract static class Operation<V> implements Callable<V>
    {
        protected abstract V run() throws HttpRequestException, IOException;
        
        protected abstract void done() throws IOException;
        
        @Override
        public V call() throws HttpRequestException {
            boolean thrown = false;
            try {
                return this.run();
            }
            catch (HttpRequestException e) {
                thrown = true;
                throw e;
            }
            catch (IOException e2) {
                thrown = true;
                throw new HttpRequestException(e2);
            }
            finally {
                try {
                    this.done();
                }
                catch (IOException e3) {
                    if (!thrown) {
                        throw new HttpRequestException(e3);
                    }
                }
            }
        }
    }
    
    protected abstract static class CloseOperation<V> extends Operation<V>
    {
        private final Closeable closeable;
        private final boolean ignoreCloseExceptions;
        
        protected CloseOperation(final Closeable closeable, final boolean ignoreCloseExceptions) {
            this.closeable = closeable;
            this.ignoreCloseExceptions = ignoreCloseExceptions;
        }
        
        @Override
        protected void done() throws IOException {
            if (this.closeable instanceof Flushable) {
                ((Flushable)this.closeable).flush();
            }
            if (this.ignoreCloseExceptions) {
                try {
                    this.closeable.close();
                }
                catch (IOException ex) {}
            }
            else {
                this.closeable.close();
            }
        }
    }
    
    protected abstract static class FlushOperation<V> extends Operation<V>
    {
        private final Flushable flushable;
        
        protected FlushOperation(final Flushable flushable) {
            this.flushable = flushable;
        }
        
        @Override
        protected void done() throws IOException {
            this.flushable.flush();
        }
    }
    
    public static class RequestOutputStream extends BufferedOutputStream
    {
        private final CharsetEncoder encoder;
        
        public RequestOutputStream(final OutputStream stream, final String charset, final int bufferSize) {
            super(stream, bufferSize);
            this.encoder = Charset.forName(getValidCharset(charset)).newEncoder();
        }
        
        public RequestOutputStream write(final String value) throws IOException {
            final ByteBuffer bytes = this.encoder.encode(CharBuffer.wrap(value));
            super.write(bytes.array(), 0, bytes.limit());
            return this;
        }
    }
}
