package cn.wangsye.myrxjava.chapter04;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.wangsye.myrxjava.R;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

public class OperatorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
    }

    public void onMapClicked(View view) {
        Observable.just("HELLO")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toLowerCase();
                    }
                })
                .subscribe(s -> {
                    System.out.println(s);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void onFlatmapClicked(View view) {
        User user = new User();
        user.userName = "tony";
        user.addresses = new ArrayList<>();

        Address address1 = new Address();
        address1.street = "street1";
        address1.city = "city1";
        user.addresses.add(address1);

        Address address2 = new Address();
        address2.street = "street2";
        address2.city = "city2";
        user.addresses.add(address2);

        Observable.just(user)
                .flatMap(new Function<User, ObservableSource<Address>>() {
                    @Override
                    public ObservableSource<Address> apply(User user) throws Exception {
                        return Observable.fromIterable(user.addresses);
                    }
                })
                .subscribe(address -> {
                    System.out.println(address.street);
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void onGroupByClicked(View view) {
        Observable.range(1, 8)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return (integer % 2 == 0) ? "偶数组" : "奇数组";
                    }
                }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                if (TextUtils.equals("奇数组", stringIntegerGroupedObservable.getKey())) {
                    System.out.println(stringIntegerGroupedObservable.getKey());
                    stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            System.out.println("  " + integer);
                        }
                    });
                }
            }
        });
    }

    static class Address {
        public String street;
        public String city;
    }

    static class User {
        public String userName;
        public List<Address> addresses;
    }
}
