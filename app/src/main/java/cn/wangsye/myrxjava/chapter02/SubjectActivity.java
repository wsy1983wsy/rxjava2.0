package cn.wangsye.myrxjava.chapter02;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.wangsye.myrxjava.R;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public class SubjectActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
    }

    /**
     * AsyncSubject的观察者只会收到onComplete之前的那条数据，且onComplete只有被调用后观察者才会收到数据
     *
     * @param view
     */
    public void onAsyncSubjectClicked(View view) {
        AsyncSubject<String> subject = AsyncSubject.create();
        subject.onNext("AsyncSubject1");
        subject.onNext("AsyncSubject2");
        subject.subscribe(s -> {
            System.out.println("AsyncSubject:" + s);
        }, throwable -> {
            throwable.printStackTrace();
        }, () -> {
            System.out.println("AsyncSubject: completed");
        });
        subject.onNext("AsyncSubject3");
        subject.onNext("AsyncSubject4");
        subject.onComplete();
    }

    /**
     * BehaviorSubject会缓存一个数据，即订阅前的那条数据
     *
     * @param view
     */
    public void onBehaviorSubjectClicked(View view) {
        BehaviorSubject<String> subject = BehaviorSubject.createDefault("Behavior1");
        subject.onNext("BehaviorSubject1");
        subject.onNext("BehaviorSubject2");
        subject.subscribe(s -> {
            System.out.println("BehaviorSubject:" + s);
        }, throwable -> {
            throwable.printStackTrace();
        }, () -> {
            System.out.println("BehaviorSubject: completed");
        });
        subject.onNext("BehaviorSubject3");
        subject.onNext("BehaviorSubject4");
        subject.onComplete();
    }

    /**
     * 只接受订阅后发出的数据
     *
     * @param view
     */
    public void onReplaySubjectClicked(View view) {
        ReplaySubject<String> subject = ReplaySubject.create();
        subject.onNext("ReplaySubject1");
        subject.onNext("ReplaySubject2");
        subject.subscribe(s -> {
            System.out.println("ReplaySubject:" + s);
        }, throwable -> {
            throwable.printStackTrace();
        }, () -> {
            System.out.println("ReplaySubject: completed");
        });
        subject.onNext("ReplaySubject3");
        subject.onNext("ReplaySubject4");
        subject.onComplete();
    }

    public void onPublishSubjectClicked(View view) {
        PublishSubject<String> subject = PublishSubject.create();
        subject.onNext("PublishSubject1");
        subject.onNext("PublishSubject2");
        subject.subscribe(s -> {
            System.out.println("PublishSubject:" + s);
        }, throwable -> {
            throwable.printStackTrace();
        }, () -> {
            System.out.println("PublishSubject: completed");
        });
        subject.onNext("PublishSubject3");
        subject.onNext("PublishSubject4");
        subject.onComplete();
    }
}
