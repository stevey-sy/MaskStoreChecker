package com.example.maskinfo.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.maskinfo.LocationDistance;
import com.example.maskinfo.model.Store;
import com.example.maskinfo.model.StoreInfo;
import com.example.maskinfo.repository.MaskService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
//    private List<Store> items = new ArrayList<>();
    // 원래는 getter setter 로 진행해야 한다.
    public Location location;

    public MainViewModel () {

    }

    // live data 적용
    public MutableLiveData<List<Store>> itemLiveData = new MutableLiveData<>();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MaskService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    private MaskService service = retrofit.create(MaskService.class);

//    private Call<StoreInfo> storeInfoCall = service.fetchStoreInfo();

    public void fetchStoreInfo() {
        // call back 함수를 사용할 경우
        service.fetchStoreInfo(location.getLatitude(), location.getLongitude())

                .enqueue(new Callback<StoreInfo>() {
            @Override
            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                Log.d(TAG, "onResponse: refresh");
                List<Store> items = response.body().getStores().stream()
                        .filter(item -> item.getRemainStat() != null)
                        .filter(item -> !item.getRemainStat().equals("empty"))
                        .collect(Collectors.toList());

                for(Store store : items) {
                    double distance = LocationDistance.distance(location.getLatitude(), location.getLongitude(), store.getLat(), store.getLng(), "k");
                    store.setDistance(distance);
                }
                Collections.sort(items);

                itemLiveData.postValue(items);
            }

            @Override
            public void onFailure(Call<StoreInfo> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t );
                // 에러가 발생하면 빈 공간을 받아오겠다.
                itemLiveData.postValue(Collections.emptyList());
            }
        });
    }
}
