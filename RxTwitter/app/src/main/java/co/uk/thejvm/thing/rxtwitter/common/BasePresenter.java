package co.uk.thejvm.thing.rxtwitter.common;

public abstract class BasePresenter<V extends BaseView> {
    protected V view;

    public void setView(V view) {
        this.view = view;
    }

    public abstract void onPause();
}
