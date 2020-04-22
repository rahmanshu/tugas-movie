package com.olins.movie.utils.eventBus;


public class RxBusObject {

    public enum RxBusKey{
        OPEN_FRAGMENT,
        GET_NEW_INPUTTED_KYC_DATA,
        SUBMIT_KYC_DATA,
        KYC_PREVIOUS_FORM,
        SUBMIT_USER_PROFILE_DATA_CONFIRMATION,
        SELECTED_DASHBOARD_MENU,
        SCROLL_TO_BOTTOM,
        BACKT_TO_SETTLEMENT,
        SAVE_KYC_DATA,
        NEXT_FORM,
        NEXT_TO_RISK_PROFILE,
        NEXT_TO_DASHBOARD,
        GET_NEW_INPUTTED_KYC_DATA_WITHOUT_VALIDATION,
        SAVE_KYC_DATA_WHILE_BACK,
        GET_NEW_INPUTTED_WARGA_NEGARA,
        NEXT_TO_WARGA_NEGARA,
        SAVE_KYC_WARGA_NEGARA
    }

    private RxBusKey key;
    private Object object;

    public RxBusObject(RxBusKey key, Object object) {
        this.key = key;
        this.object = object;
    }

    public RxBusKey getKey() {
        return key;
    }

    public Object getObject() {
        return object;
    }
}
