package com.example.mapboxintegration.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseActivity<V extends ViewModel, T extends ViewDataBinding> extends AppCompatActivity {

    // Create customize layout resource.
    protected abstract int setLayoutResourceId();

    // Create customize ViewModel because we have multi ViewModel.
    protected abstract ViewModel createViewModel();

    // Make it generic to support parameterized types.
    protected T viewDataBinding;
    protected V viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBuilding();
    }

    private void initBuilding() {
        // We use DataBinding to make access for views more easier for us.
        viewDataBinding = DataBindingUtil.setContentView(this, setLayoutResourceId());
        viewModel = (V) createViewModel();
    }
}