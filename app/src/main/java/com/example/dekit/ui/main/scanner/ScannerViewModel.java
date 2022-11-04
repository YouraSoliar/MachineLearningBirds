package com.example.dekit.ui.main.scanner;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.dekit.room.BirdsDatabase;
import com.example.dekit.room.dao.BirdsDao;
import com.example.dekit.room.enteties.Bird;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScannerViewModel extends AndroidViewModel {

    private BirdsDao birdsDao;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ScannerViewModel(@NonNull Application application) {
        super(application);
        birdsDao = BirdsDatabase.getInstance(application).birdsDao();
    }

    public void add(Bird bird) {
        Disposable disposable = birdsDao.add(bird)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Toast.makeText(
                                getApplication(),
                                "Saved",
                                Toast.LENGTH_SHORT).show(),
                        throwable -> Log.e("ErrorMessage", throwable.getMessage())
                );
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}