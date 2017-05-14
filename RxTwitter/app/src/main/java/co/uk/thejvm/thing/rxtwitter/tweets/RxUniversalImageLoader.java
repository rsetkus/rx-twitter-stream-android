package co.uk.thejvm.thing.rxtwitter.tweets;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RxUniversalImageLoader extends Observable<Bitmap> {

    private final String uri;

    public RxUniversalImageLoader(String uri) {
        this.uri = uri;
    }

    @Override
    protected void subscribeActual(Observer<? super Bitmap> observer) {
        RxSimpleImageLoadingListener loadingListener = new RxSimpleImageLoadingListener(observer);
        ImageLoader.getInstance().loadImage(uri, loadingListener);
    }

    private static class RxSimpleImageLoadingListener extends SimpleImageLoadingListener implements Disposable {

        private AtomicBoolean isDisposed = new AtomicBoolean(false);
        private final Observer<? super Bitmap> observer;

        public RxSimpleImageLoadingListener(Observer<? super Bitmap> observer) {
            this.observer = observer;
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            observer.onError(failReason.getCause());
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            observer.onNext(loadedImage);
            observer.onComplete();
        }

        @Override
        public void dispose() {
            if (isDisposed.compareAndSet(false, true)) {
                ImageLoader.getInstance().stop();
            }
        }

        @Override
        public boolean isDisposed() {
            return isDisposed.get();
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            // Empty implementation
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            // Empty implementation
        }
    }
}
