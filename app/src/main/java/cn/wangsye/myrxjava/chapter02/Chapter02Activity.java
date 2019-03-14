package cn.wangsye.myrxjava.chapter02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.concurrent.TimeUnit;

import cn.wangsye.myrxjava.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Chapter02Activity extends AppCompatActivity {
    private ConnectableObservable<Long> connectableObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_02);
    }

    public void onDemo1Clicked(View view) {
        Observable.just("Hello world")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onComplete");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        System.out.println("subscribe");
                    }
                });
    }

    public void onColdObservableClicked(View view) {
        Consumer<Long> subscirber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber1: " + aLong);
            }
        };
        Consumer<Long> subscirber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("    subscriber2: " + aLong);
            }
        };
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(10, TimeUnit.MICROSECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(value -> {
                            emitter.onNext(value);
                        });
            }
        }).observeOn(Schedulers.newThread());
        observable.subscribe(subscirber1);
        observable.subscribe(subscirber2);
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void publishToHotObservableClicked(View view) {
        Consumer<Long> subscirber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber1: " + aLong);
            }
        };
        Consumer<Long> subscirber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber2: " + aLong);
            }
        };
        Consumer<Long> subscirber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber3: " + aLong);
            }
        };
        connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(value -> {
                            emitter.onNext(value);
                        });
            }
        }).observeOn(Schedulers.newThread()).publish();
        //调用connect在真正执行热Observable
        connectableObservable.connect();
        connectableObservable.subscribe(subscirber1);
        connectableObservable.subscribe(subscirber2);
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        connectableObservable.subscribe(subscirber3);
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void onSubscribeHotObservableClicked(View view) {
        Consumer<Long> subscirber4 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber4: " + aLong);
            }
        };
        if (connectableObservable != null) {
            connectableObservable.subscribe(subscirber4);
        }
    }

    public void onSubjectHotObservableClicked(View view) {
        Consumer<Long> subscirber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber1: " + aLong);
            }
        };
        Consumer<Long> subscirber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber2: " + aLong);
            }
        };
        Consumer<Long> subscirber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber3: " + aLong);
            }
        };
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(value -> {
                            emitter.onNext(value);
                        });
            }
        }).observeOn(Schedulers.newThread());

        PublishSubject<Long> subject = PublishSubject.create();
        observable.subscribe(subject);

        subject.subscribe(subscirber1);
        subject.subscribe(subscirber2);
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        subject.subscribe(subscirber3);
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 全部取消订阅
     *
     * @param view
     */
    public void onRefCountToColdObservableClicked1(View view) {
        Consumer<Long> subscirber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber1: " + aLong);
            }
        };
        Consumer<Long> subscirber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber2: " + aLong);
            }
        };
        Consumer<Long> subscirber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber3: " + aLong);
            }
        };
        connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(value -> {
                            emitter.onNext(value);
                        });
            }
        }).observeOn(Schedulers.newThread()).publish();
        //调用connect在真正执行热Observable
        connectableObservable.connect();
        Observable<Long> observable = connectableObservable.refCount();
        Disposable disposable1 = observable.subscribe(subscirber1);
        Disposable disposable2 = observable.subscribe(subscirber2);
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1.dispose();
        disposable2.dispose();
        disposable1 = observable.subscribe(subscirber1);
        disposable2 = observable.subscribe(subscirber2);
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1.dispose();
        disposable2.dispose();
    }

    /**
     * 部分取消订阅
     *
     * @param view
     */
    public void onRefCountToColdObservableClicked2(View view) {
        Consumer<Long> subscirber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber1: " + aLong);
            }
        };
        Consumer<Long> subscirber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber2: " + aLong);
            }
        };
        Consumer<Long> subscirber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber3: " + aLong);
            }
        };
        connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(value -> {
                            emitter.onNext(value);
                        });
            }
        }).observeOn(Schedulers.newThread()).publish();
        //调用connect在真正执行热Observable
        connectableObservable.connect();
        Observable<Long> observable = connectableObservable.refCount();
        Disposable disposable1 = observable.subscribe(subscirber1);
        Disposable disposable2 = observable.subscribe(subscirber2);
        Disposable disposable3 = observable.subscribe(subscirber3);
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1.dispose();
        disposable2.dispose();
        disposable1 = observable.subscribe(subscirber1);
        disposable2 = observable.subscribe(subscirber2);
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1.dispose();
        disposable2.dispose();
    }

    public void onShareToColdObservableClicked(View view) {
        Consumer<Long> subscirber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber1: " + aLong);
            }
        };
        Consumer<Long> subscirber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber2: " + aLong);
            }
        };
        Consumer<Long> subscirber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                System.out.println("subscriber3: " + aLong);
            }
        };
        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(final ObservableEmitter<Long> emitter) throws Exception {
                Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(value -> {
                            emitter.onNext(value);
                        });
            }
        }).observeOn(Schedulers.newThread()).share();
        //调用connect在真正执行热Observable
//        connectableObservable.connect();
        Disposable disposable1 = observable.subscribe(subscirber1);
        Disposable disposable2 = observable.subscribe(subscirber2);
        Disposable disposable3 = observable.subscribe(subscirber3);
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1.dispose();
        disposable2.dispose();
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1 = observable.subscribe(subscirber1);
        disposable2 = observable.subscribe(subscirber2);
        try {
            Thread.sleep(2000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        disposable1.dispose();
        disposable2.dispose();
    }

    public void onSingleClicked(View view) {
        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                emitter.onSuccess("single String1");
                emitter.onSuccess("single String2");
            }
        }).subscribe(obj -> {
            System.out.println(obj);
        }, throwable -> {
        });
    }
}
