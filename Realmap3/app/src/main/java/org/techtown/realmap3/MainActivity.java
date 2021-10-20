package org.techtown.realmap3;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.techtown.realmap3.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import org.techtown.realmap3;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {
    // 카드뷰 보이게 할 때 implements에 GoogleMap.OnMarkerClickListener 추가하기


    TextView addr; // 식당주소
    // private String areacode; // 구별 숫자
    // ImageView firstimage; // 이미지 사진
    // private  String mapx; // 위도
    // private String mapy; // 경도
    TextView tel; // 전화번호
    TextView title; //식당 이름
    EditText search;

    String data;
    String str;
    String loc;
    StringBuffer buffer;
    String key;
    String queryUrl;

    XmlPullParserFactory parserCreator;
    XmlPullParser parser;
    InputStream is;
    String tag;

    // 지도 만들기
    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 100000;  // 기존 갱신 1초(10000)에서 늘림
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500000; // 0.5초에서 늘림


    // onRequestPermissionsResult에서 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 권한 요청을 구별하기 위해 사용
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    // 앱을 실행하기 위해 필요한 권한 정의
    // 위치 권한
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    Location mCurrentLocatiion;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;

    private View mLayout;  // Snackbar 사용하기 위해서는 View가 필요


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 처음에 카드뷰 안보이게 하기
        LinearLayout mapCardView;
        mapCardView = (LinearLayout) findViewById(R.id.mapview);
        mapCardView.setVisibility((View.GONE));


        // 검색 기능
        // 구글맵에서 검색창 editText 와 검색된 위치 불러올 textView 초기화
        search = findViewById(R.id.search);
        title = findViewById(R.id.title);
        //locationText = findViewById(R.id.shop_text_location);

        // 구글맵 검색 하는 부분
        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색창에서 텍스트를 가져온다
                String searchText = search.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocationName(searchText, 3);
                    if (addresses != null && !addresses.equals(" ")) {
                        search(addresses);
                    }
                } catch (Exception e) {

                }

                // 카드뷰 보이게
                if (searchText != null) {
                    LinearLayout mapCardView;
                    mapCardView = (LinearLayout) findViewById(R.id.mapview);
                    mapCardView.setVisibility((View.VISIBLE));
                } else {
                    mapCardView.setVisibility((View.GONE));
                }
                try {
                    getXmlData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }); // end of searchButton.setOnClickListener

    }//end of onCreate()

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        //지도의 초기위치를 서울로 이동
        setDefaultLocation();


        // 1. 위치 권한을 가지고 있는지 체크
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        // 2. 이미 권한을 가지고 있다면
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            startLocationUpdates(); // 3. 위치 업데이트 시작


        } else {  //2. 권한 요청을 허용한 적이 없다면 퍼미션 요청이 필요

            // 3-1. 사용자가 권한 요청 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 권한이 필요한 이유를 설명해주기
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        // 3-3. 사용자에게 퍼미션 요청함 요청 결과는 onRequestPermissionResult에서 수신
                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {
                // 4-1. 사용자가 권한 거부를 한 적이 없는 경우 (바로 권한 요청)
                // 요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        } // end of if~else


        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d(TAG, "onMapClick :");
            }
        });


    } // end of OnMapReady

    // 위치 결과 받고 나서 좌표 찍어주기
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }


        }

    };

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    } // end of startLocationUpdates()


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap != null)
                mMap.setMyLocationEnabled(true);

        }
    } // end of onStart()


    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    } // end of onStop()


    public String getCurrentAddress(LatLng latlng) {

        // 지오코더
        // GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }


    public void setDefaultLocation() {


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    // 위치 권한 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;

    }


    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 권한을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {

                // 권한 허용했다면 위치 업데이트를 시작
                startLocationUpdates();
            } else {
                // 거부한 권한이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료하기

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있음
                    Snackbar.make(mLayout, "위치 권한이 거부되었습니다. 앱을 다시 실행하여 위치 권한을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있음
                    Snackbar.make(mLayout, "위치 권한이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    // GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE) {//사용자가 GPS 활성 시켰는지 검사
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {
                    Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
                    needRequest = true;
                    return;
                }
            }
        }

    } // end of onActivityResult()


    // 구글맵 주소 검색 메서드
    protected void search(List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng2 = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getLatitude(), address.getLongitude());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng2);
        markerOptions.title(addressText);

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    private void getXmlData() throws UnsupportedEncodingException {

        StrictMode.enableDefaults();

        title = (TextView)findViewById(R.id.title);

        boolean initem = false, inAddr = false;
        boolean intitle = false, intel = false;

       String addr1 = null, name = null, tel1 = null;

        search = findViewById(R.id.search);
        buffer = new StringBuffer();
        key = "a9xMZrBYPLK1%2BobKPJ6FYb2Ii%2FfhuqE5MsPwfE7QpaFwPAo6dDAt%2Fjsu2bKPgRThPL%2FlafYq%2FcFRSgdVrOx9GA%3D%3D";

        str = search.getText().toString();//EditText에 작성된 Text얻어오기
        loc = URLEncoder.encode(str, "UTF-8");//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding

        queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword" +
                "?serviceKey=" + key +
                "&MobileApp=ilinboon&MobileOS=AND" + "&keyword=" + loc +
                "&pageNo=1&numOfRows=10&listYN=Y&arrange=A&contentTypeId=39&areaCode=1";

        try {
            URL url = new URL(queryUrl);

            parserCreator = XmlPullParserFactory.newInstance();
            parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            System.out.println("파싱시작합니다.");

            tag=null;


            int parserEvent = parser.getEventType();


            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        tag= parser.getName();

                        switch (tag) {
                            case "items":
                                // 첫번째 검색결과
                                break;
                            case "item":
                                // 첫번째 검색결과
                                break;
                            case "addr1":
                                buffer.append("주소 : ");
                                parser.next();
                                buffer.append(parser.getText());//addr 요소의 TEXT 읽어와서 문자열버퍼에 추가

                                buffer.append("\n"); //줄바꿈 문자 추가

                                break;
                            case "title":
                                buffer.append("식당 이름 : ");
                                parser.next();
                                buffer.append(parser.getText());
                                buffer.append("\n");
                                break;
                            case "tel":
                                buffer.append("전화번호 :");
                                parser.next();
                                buffer.append(parser.getText());//cpId

                                buffer.append("\n");
                                break;

                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= parser.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

              //  parserEvent= parser.next();
            }

        } catch (Exception e) {
            buffer.append("파싱 오류\n");
        }

        buffer.append("파싱 끝\n");
        title.setText(buffer.toString());
       // return buffer.toString();//StringBuffer 문자열 객체 반환

    }//getXmlData method....
} // end of MainActivity