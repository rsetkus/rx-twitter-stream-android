package co.uk.thejvm.thing.rxtwitter.common;

public class BaseView<P> {
    private P presenter;

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }
}
