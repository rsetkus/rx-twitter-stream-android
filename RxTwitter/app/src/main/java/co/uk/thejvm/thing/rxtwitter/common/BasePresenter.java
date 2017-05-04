package co.uk.thejvm.thing.rxtwitter.common;

public class BasePresenter<V extends BaseView> {
    private V view;

    public void setView(V view) {
        this.view = view;
    }
}
