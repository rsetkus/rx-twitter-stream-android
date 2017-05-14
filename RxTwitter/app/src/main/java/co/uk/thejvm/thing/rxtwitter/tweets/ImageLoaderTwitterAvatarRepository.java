package co.uk.thejvm.thing.rxtwitter.tweets;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class ImageLoaderTwitterAvatarRepository implements TwitterAvatarRepository {

    @Override
    public Flowable<Bitmap> getAvatar(String imageUri) {
        return Flowable.create(e -> {
            AvatarListener listener = new AvatarListener(e);
            ImageLoader imgLoader = ImageLoader.getInstance();
            imgLoader.loadImage(imageUri, listener);
            e.setCancellable(() -> imgLoader.stop());
        }, BackpressureStrategy.ERROR);
    }

}
