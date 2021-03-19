package com.example.maskinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maskinfo.model.Store;
import com.example.maskinfo.model.StoreInfo;
import com.example.maskinfo.repository.MaskService;
import com.example.maskinfo.viewmodel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MainViewModel viewModel;
    // 위치 데이터 객체
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // view model 객체 생성
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                performAction();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();




        // 리사이클러뷰 세팅 코드
        // retrofit 통신 코드 --> view model 로 분리 가능
        // 리사이클러뷰 어댑터 코드

//       RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
//
//        final StoreAdapter adapter = new StoreAdapter();
//        recyclerView.setAdapter(adapter);

        // view model 객체 생성
        // 통신 기능을 view model 로 분리함
//       viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//       viewModel.itemLiveData.observe(this, new Observer<List<Store>>() {
//
//           @Override
//           public void onChanged(List<Store> stores) {
//
//           }
//       });

        // 람다식으로 변경
        // livedata 가 데이터의 변화를 감지해서 UI 를 갱신
//        viewModel.itemLiveData.observe(this, stores -> {
//            adapter.updateItems(stores);
//            getSupportActionBar().setTitle("마스크 재고 있는 곳: "+stores.size()+ "곳 ");
//
//        });

        // view model 에게 데이터 요청
        // 화면 돌릴 때마다 로그에 찍히는 것 확인 = live data 를 제대로 활용 못하고 있다.
//        viewModel.fetchStoreInfo();


        // retrofit 세팅 및 통신 준비
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(MaskService.BASE_URL)
//                .addConverterFactory(MoshiConverterFactory.create())
//                .build();
//
//        MaskService service = retrofit.create(MaskService.class);

//        Call<StoreInfo> storeInfoCall = service.fetchStoreInfo();
        // 안드로이드 에서는 네트워크 관련 메소드를 실행할 때 비동기식으로 처리해야만 작동한다.
        // .execute()는 동기식을 지원하는 메소드이기 때문에 바꿔야한다.
//        StoreInfo storeInfo = storeInfoCall.execute().body();

        // .execute() 대신에 enqueue()를 사용해야 한다.
        // 기존에 사용하던 방식 -> 통신 call back 메소드
//        storeInfoCall.enqueue(new Callback<StoreInfo>() {
//            @Override
//            public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
//                Log.d(TAG, "onResponse: refresh");
//                List<Store> items = response.body().getStores();
//
//                // 외부에서 선언된 변수를 사용할 경우 에러가 난다.
//                // 이유는 변경이 가능한 객체이기 때문에
//
//                // 받아온 데이터 중 null 값이 있는 경우
//                // stream API 사용하여 null 값인 데이터들만 골라내고
//                // list 에 적용할 수 있다.
//                adapter.updateItems(items
//                        .stream()
//                        .filter(item -> item.getRemainStat() != null)
//                        .collect(Collectors.toList()));
//                getSupportActionBar().setTitle("마스크 재고 있는 곳: "+items.size()+ "곳 ");
//            }
//
//            @Override
//            public void onFailure(Call<StoreInfo> call, Throwable t) {
//                Log.e(TAG, "onFailure: ", t );
//            }
//        });
    }

    @SuppressLint("MissingPermission")
    private void performAction() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object

//                        location.setLatitude(37.641420769234);
//                        location.setLongitude(127.03856546312);

                        Log.d(TAG, "performAction: "+ location);
                        Log.d(TAG, "performAction: "+ location.getLatitude());
                        Log.d(TAG, "performAction: "+ location.getLongitude());

                        // 받아온 Location 데이터를 view model로 넘겨주는 부분
                       viewModel.location = location;
                       //
                       viewModel.fetchStoreInfo();
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        final StoreAdapter adapter = new StoreAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.itemLiveData.observe(this, stores -> {
            adapter.updateItems(stores);
            getSupportActionBar().setTitle("마스크 재고 있는 곳: "+stores.size()+ "곳 ");

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // 데이터 새로고침
                viewModel.fetchStoreInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> mItems = new ArrayList<>();

    // view holder 를 만드는 부분
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    // View Holder 에 데이터를 bind 하는 부분
    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = mItems.get(position);

        holder.nameTextView.setText(store.getName());
        holder.addressTextView.setText(store.getAddr());
        holder.distanceTextView.setText(String.format("%.2fkm", store.getDistance()));

        int color = Color.GREEN;
        String count = "100개 이상";
        String remainStat = "충분";
        switch (store.getRemainStat()) {
            case "plenty" :
                remainStat = "충분";
                count = "100개 이상";
                color = Color.GREEN;
                break;
            case "some" :
                remainStat = "여유";
                count = "30개 이상";
                color = Color.CYAN;
                break;
            case "few" :
                remainStat = "매진 임박";
                count = "2개 이상";
                color = Color.RED;
                break;
            case "empty" :
                remainStat = "재고 없음";
                count = "1개 이하";
                color = Color.GRAY;
                break;
            default:
        }

        holder.remainTextView.setText(remainStat);
        holder.countTextView.setText(count);

        holder.remainTextView.setTextColor(color);
        holder.countTextView.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    //  item view 의 정보를 가지고 있는 클래스
    static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;
        TextView distanceTextView;
        TextView remainTextView;
        TextView countTextView;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            addressTextView = itemView.findViewById(R.id.addr_text_view);
            distanceTextView = itemView.findViewById(R.id.distance_text_view);
            remainTextView = itemView.findViewById(R.id.remain_text_view);
            countTextView = itemView.findViewById(R.id.count_text_view);
        }
    }

    public void updateItems(List<Store> items) {
        mItems = items;
        notifyDataSetChanged();
    }

}