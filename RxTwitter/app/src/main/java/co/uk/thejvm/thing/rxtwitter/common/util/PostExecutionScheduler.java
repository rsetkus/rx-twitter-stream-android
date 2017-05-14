package co.uk.thejvm.thing.rxtwitter.common.util;

import io.reactivex.Scheduler;

public interface PostExecutionScheduler {
    Scheduler getScheduler();
}
