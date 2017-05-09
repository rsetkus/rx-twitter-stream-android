package co.uk.thejvm.thing.rxtwitter.common.di;

import android.content.Context;

import javax.inject.Singleton;

import co.uk.thejvm.thing.rxtwitter.common.repository.HardCodedSecretsStorage;
import co.uk.thejvm.thing.rxtwitter.common.repository.SecretsStorage;
import dagger.Module;
import dagger.Provides;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@Module
public class ApplicationModule {

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public SecretsStorage provideSecretsStorage() {
        return new HardCodedSecretsStorage();
    }

    @Singleton
    @Provides
    public Configuration provideConfiguration(SecretsStorage secretsStorage) {
        return new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(secretsStorage.getConsumerKey())
                .setOAuthConsumerSecret(secretsStorage.getConsumerSecret())
                .setOAuthAccessToken(secretsStorage.getToken())
                .setOAuthAccessTokenSecret(secretsStorage.getTokenSecret())
                .build();
    }

    @Provides
    @Singleton
    public TwitterStream provideTwitterStream(Configuration configuration) {
        return new TwitterStreamFactory(configuration).getInstance();
    }
}
