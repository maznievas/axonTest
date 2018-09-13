package com.example.andrey.axontesttask.employees;

import android.content.res.AssetManager;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.andrey.axontesttask.api.entity.Employees;
import com.example.andrey.axontesttask.util.Const;
import com.google.gson.Gson;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by andrey on 12.09.18.
 */

@InjectViewState
public class EmployeesPresenter extends MvpPresenter<EmployeesView> {

    CompositeDisposable compositeDisposable;

    public EmployeesPresenter() {
        compositeDisposable = new CompositeDisposable();
    }

    public void clear() {
        compositeDisposable.clear();
    }

    public void fetchEmployees(AssetManager assetManager) {
        compositeDisposable.add(
                Flowable.generate(
                        () -> new BufferedReader(new InputStreamReader(assetManager.open("employees.json"))),
                        (BiConsumer<BufferedReader, Emitter<String>>) (reader, emitter) -> {
                            String line;// = reader.readLine();
                            StringBuilder total = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                total.append(line).append('\n');
                            }
                            if (line == null) {
                                Log.d("mLog", total.toString());
                                emitter.onNext(total.toString());
                                emitter.onComplete();
                            }
                        },
                        reader -> reader.close()
                )
                        .map(rawJson -> new Gson().fromJson(rawJson, Employees.class).getEmployees())
                        .flatMapIterable(employeesList -> employeesList)
                        .flatMap(employee -> {
                            return getAvatarUrlFromAddress(employee.getAvatar())
                                    .map(imgUrl -> {
                                        employee.setAvatar(imgUrl);
                                        return employee;
                                    });
                        })
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> getViewState().showLoadingState())
                        .doAfterTerminate(() -> getViewState().hideLoadingState())
                        .subscribe(employeesList -> {
                            Log.d("mLog", "subscribe");
                            getViewState().setEmployees(employeesList);
                        }, throwable -> {
                            Log.e("mLog", "fetching employees");
                            throwable.printStackTrace();
                        })
        );
    }

    public Flowable<String> getAvatarUrlFromAddress(String address) {
        return Flowable.just(address)
                .map(_address -> {
                    try {
                        Document doc = Jsoup.connect(_address).get();
                        Elements links = doc.select(".fullImageLink");
                        return Const.BASE_PROTOCOL + links.get(0).select("a").attr("href");
                    } catch (HttpStatusException e) {
                        Log.e("mLog", "Status exception");
                        e.printStackTrace();
                        return "error";
                    }
                }).onErrorResumeNext(new Function<Throwable, Publisher<? extends String>>() {
                    @Override
                    public Publisher<? extends String> apply(Throwable throwable) throws Exception {
                        return Flowable.just("error");
                    }
                });
    }
}
