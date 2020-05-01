package com.makgyber.villagebuys.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mTindahanName;
    private MutableLiveData<String> mAddress;
    private MutableLiveData<String> mContactInfo;
    private MutableLiveData<String> mServiceArea;


    public GalleryViewModel() {
        mTindahanName = new MutableLiveData<>();
        mAddress = new MutableLiveData<>();
        mContactInfo = new MutableLiveData<>();
        mServiceArea = new MutableLiveData<>();
    }
}