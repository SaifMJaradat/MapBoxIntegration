package com.example.mapboxintegration.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseActivity<V extends ViewModel, T extends ViewDataBinding> extends AppCompatActivity {

    protected abstract int setLayoutResourceId();
    protected abstract ViewModel createViewModel();

    protected T viewDataBinding;
    protected V viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBuilding();
    }

    private void initBuilding() {
        viewDataBinding = DataBindingUtil.setContentView(this, setLayoutResourceId());
        viewModel = (V) createViewModel();
    }
}