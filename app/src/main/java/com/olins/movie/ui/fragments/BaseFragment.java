package com.olins.movie.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.olins.movie.ui.activities.BaseActivity;
import com.olins.movie.R;
import com.olins.movie.data.api.TrackingService;
import com.olins.movie.utils.eventBus.RxBus;
import com.olins.movie.utils.eventBus.RxBusObject;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.ButterKnife;
import icepick.Icepick;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;



public abstract  class BaseFragment extends Fragment implements Validator.ValidationListener {

    @BindString(R.string.loading)
    public String loading;
    @BindString(R.string.connection_error)
    public String connectionError;
    @BindString(R.string.connection_error_swipe)
    public String connectionErrorSwipe;
    @BindString(R.string.no_data)
    public String noData;
    @BindInt(R.integer.success_code)
    public int successCode;
    @BindInt(R.integer.success_activication_code)
    public  int activicationSuccessCode;
    @BindInt(R.integer.failure_code)
    public int failureCode;
    @BindColor(R.color.colorPrimary)
    public int cAppsPrimary;
    @BindColor(R.color.colorPrimaryDark)
    public int cAppsPrimaryDark;
    @BindColor(R.color.blue_700)
    public int cPrimary;
    @BindColor(R.color.green_500)
    public int cSuccess;
    @BindColor(R.color.light_blue_500)
    public int cInfo;
    @BindColor(R.color.orange_500)
    public int cWarning;
    @BindColor(R.color.red_500)
    public int cDanger;

    protected Validator validator;
    protected RxBus bus;
    private CompositeSubscription subscriptions;

    protected abstract int getLayout();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        Icepick.restoreInstanceState(this, savedInstanceState);
        this.validator = new Validator(this);
        this.validator.setValidationListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.bus = ((BaseActivity) getActivity()).getBus();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.subscriptions = new CompositeSubscription();
        this.subscriptions
                .add(bus.toObserverable()
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object event) {
                                if (event instanceof RxBusObject) {
                                    RxBusObject busObject = (RxBusObject) event;
                                    busHandler(busObject.getKey(), busObject.getObject());
                                }
                            }
                        })
                );
    }

    @Override
    public void onStop() {
        super.onStop();
        this.subscriptions.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public TrackingService.Api getApi() {
        return ((BaseActivity) getActivity()).getTrackingService().getApi();
    }

    public Validator getValidator() {
        return validator;
    }

    public RxBus getBus() {
        return this.bus;
    }

    public void busHandler(RxBusObject.RxBusKey busKey, Object busObject) {

    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());
            if (view instanceof EditText) {
                EditText et = ((EditText) view);
                et.setError(message);
                et.requestFocus();
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showProgressDialog(String message) {
        ((BaseActivity) getActivity()).showProgressDialog(message);
    }

    public void dismissProgressDialog() {
        ((BaseActivity) getActivity()).dismissProgressDialog();
    }

    public void showFailedDialog(String message) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.mipmap.ic_launcher)
                .backgroundColor(Color.WHITE)
                .title(getString(R.string.infortmation).toUpperCase())
                .titleColor(Color.BLACK)
                .content(message)
                .contentColor(Color.GRAY)
                .positiveText(R.string.ok)
                .positiveColor(Color.RED)
                .cancelable(false)
                .show();
    }

    public void showUploadSuccessDialog(String message) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.mipmap.ic_launcher)
                .backgroundColor(Color.WHITE)
                .title(getString(R.string.success).toUpperCase())
                .titleColor(Color.BLACK)
                .content(message)
                .contentColor(Color.GRAY)
                .positiveText(R.string.ok)
                .positiveColor(Color.RED)
                .cancelable(false)
                .show();
    }

    public void showSuccessDialog(String message, final Boolean isFinishActivity) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.mipmap.ic_launcher)
                .backgroundColor(Color.WHITE)
                .title(getString(R.string.success).toUpperCase())
                .titleColor(Color.BLACK)
                .content(message)
                .contentColor(Color.GRAY)
                .positiveText(R.string.ok)
                .positiveColor(Color.RED)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        if (isFinishActivity) {
                            getActivity().finish();
                        }
                    }
                })
                .cancelable(false)
                .show();
    }

    public void showCallbackDialog(String message, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.mipmap.ic_launcher)
                .backgroundColor(Color.WHITE)
                .title(getString(R.string.failed).toUpperCase())
                .titleColor(Color.BLACK)
                .content(message)
                .contentColor(Color.GRAY)
                .positiveText(R.string.ok)
                .positiveColor(Color.RED)
                .negativeText("Nanti")
                .cancelable(false)
                .onPositive(callback)
                .show();
    }

    public void showSuccessDialogCallback(String message, MaterialDialog.SingleButtonCallback callback) {
        new MaterialDialog.Builder(getActivity())
                .iconRes(R.mipmap.ic_launcher)
                .backgroundColor(Color.WHITE)
                .title(getString(R.string.success).toUpperCase())
                .titleColor(Color.BLACK)
                .content(message)
                .contentColor(Color.GRAY)
                .positiveText(R.string.ok)
                .positiveColor(Color.RED)
                .cancelable(false)
                .onPositive(callback)
                .show();
    }

}
