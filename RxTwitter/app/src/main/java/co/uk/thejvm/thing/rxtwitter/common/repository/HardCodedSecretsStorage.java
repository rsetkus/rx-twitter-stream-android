package co.uk.thejvm.thing.rxtwitter.common.repository;

/**
 * All secrects are provided on Twitter developer project so, copy and paste them here.
 */
public class HardCodedSecretsStorage implements SecretsStorage {

    @Override
    public String getConsumerKey() {
        return "";
    }

    @Override
    public String getConsumerSecret() {
        return "";
    }

    @Override
    public String getToken() {
        return "";
    }

    @Override
    public String getTokenSecret() {
        return "";
    }
}
