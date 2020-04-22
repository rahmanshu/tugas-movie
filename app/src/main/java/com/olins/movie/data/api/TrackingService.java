package com.olins.movie.data.api;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.olins.movie.data.api.response.DetailMovieResponse;
import com.olins.movie.data.api.response.MovieResponse;
import com.olins.movie.ui.receiver.Tls12SocketFactory;
import com.olins.movie.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public class TrackingService {

    private Api api;
    public final static String BASE_URL = "http://www.omdbapi.com/";

    public TrackingService(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(provideOkHttpClient(context))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.api = retrofit.create(Api.class);
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder httpClient) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                httpClient.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);
                httpClient.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return httpClient;
    }

    private OkHttpClient provideOkHttpClient(final Context context) {
        HttpLoggingInterceptor httpLoggingInterceptorinterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptorinterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(500, TimeUnit.SECONDS);
        httpClient.connectTimeout(500, TimeUnit.SECONDS);
        httpClient.addInterceptor(httpLoggingInterceptorinterceptor); //disable ketika production
       // httpClient.addInterceptor(new ChuckInterceptor(context)); //disable ketika production
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                invalidTokenHandler(response, context);
                return response;
            }
        });
        return enableTls12OnPreLollipop(httpClient).build();
    }

    private void invalidTokenHandler(Response response, Context context) throws IOException {
        String responseBody = response.peekBody(100000L).string();
        try {
            Object json = new JSONTokener(responseBody).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonObject = new JSONObject(responseBody);
                if (jsonObject.has("code") && jsonObject.getInt("code") == Constant.INVALID_TOKEN) {
                    Intent intent = new Intent("Invalid");
                    context.sendBroadcast(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public TrackingService.Api getApi() {
        return api;
    }

    public interface Api {

        @POST("/")
        Observable<MovieResponse> getListItem(@Query("apikey") String apikey, @Query("t") String t,
                                              @Query("s") String s, @Query("y") String y, @Query("i") String i);

        @POST("/")
        Observable<DetailMovieResponse> getDetailMovie(@Query("apikey") String apikey, @Query("i") String i);

    }

}
