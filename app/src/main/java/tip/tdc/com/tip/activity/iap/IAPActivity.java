/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.activity.iap;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.activity.comment.CommentActivity;
import tip.tdc.com.tip.activity.detail.DetailTabsActivity;
import tip.tdc.com.tip.activity.home.HomeActivity;
import tip.tdc.com.tip.activity.iap.utils.IabHelper;
import tip.tdc.com.tip.activity.iap.utils.IabResult;
import tip.tdc.com.tip.activity.iap.utils.Inventory;
import tip.tdc.com.tip.activity.iap.utils.Purchase;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppSetting;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.service.ChangeEventListener;
import tip.tdc.com.tip.service.CityService;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;

import com.android.vending.billing.IInAppBillingService;
import com.bpackingapp.vietnam.travel.R;
import com.google.firebase.database.DatabaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IAPActivity extends AppCompatActivity {

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuOL+MQDodwpeIueTR7KFb4X8TeXRyb/RiUsAGuKA2UB5NgcY0yOq3aMRMK5xh/KmpK+Y3sj+92bRSRRBkC/1qhaVJMxbVXEK7ifR+t5oEkG+9EJ0atr5dZ0XBmA7H8QzyINt6xC9BwWpvrOSEkxxYW6JiTXWmj6v6yz+l4++rUuwZy3aXVqzVHWxMPTCCkcguwiIcvd2EjnPcrUbUJjjl/tAhd0zyJJbCn3p8mWqgzNu8cAItjizAkKEfmlTvuAyuwoyotA4A/Jbj1UqZVb8ixm3+R+CB2uquZOwSGAH1SjMrzpe+T3CKW9bKOSeOB7yKohBAD8u8ImlWEUV5ZrWVQIDAQAB";
    String TAG = IAPActivity.class.getSimpleName();
    String skuAllCity = "vn.leaestudio.trid.all_cities";

    Context mContext;

    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    FCity mFCity;

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, IAPActivity.class);
        return in;
    }

    IabHelper iabHelper;


    TextView tvUnlockCity;
    Button btnBuyCity, btnRestorePurchase, btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iap);

        mContext = this;

        btnBuyCity = (Button) findViewById(R.id.btnBuyCity);
        btnRestorePurchase = (Button) findViewById(R.id.btnRestorePurchase);
        btnBuy = (Button) findViewById(R.id.btnBuy);


        btnBuyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFCity != null) {
                    buyItem(mFCity.getApplePurchaseId());
                } else {
                    Toast.makeText(mContext, "Purchase Id empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRestorePurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Checking...", Toast.LENGTH_SHORT).show();
                restorePurchase(true);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem(skuAllCity);
            }
        });

        if (TipApplication.getTipApplication().countryService.getCount() > 0) {
            cityService = new CityService(TipApplication.getTipApplication().countryService.getItem(0).getKey());
            cityService.setOnChangedListener(new ChangeEventListener() {
                @Override
                public void onChildChanged(EventType type, int index, int oldIndex) {

                }

                @Override
                public void onDataChanged() {
                    fCities.clear();
                    fCities.addAll(cityService.getListCities());

                    listAllCityPurchaseId.clear();
                    fCityPurchases.clear();

                    for (FCity fCity : fCities) {
                        if (fCity.getApplePurchaseId() != null && fCity.getApplePurchaseId().length() > 0) {
                            listAllCityPurchaseId.add(fCity.getApplePurchaseId());
                            fCityPurchases.add(fCity);
                        }
                    }

//                    Toast.makeText(mContext, "Load list city purchase id complete, size:" + listAllCityPurchaseId.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

        parseIntent();

        Utils.setStatusBarColor(IAPActivity.this, 0);
        findViewById(R.id.imvExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        overridePendingTransition(R.anim.svslide_in_top, R.anim.svslide_out_top);


        iabHelper = new IabHelper(mContext, base64EncodedPublicKey);
        iabHelper.enableDebugLogging(true);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                    Toast.makeText(mContext, "In-app Billing setup failed: " + result, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
//                    Toast.makeText(mContext, "In-app Billing setup OK", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iabHelper.setServiceReadyListener(new IabHelper.ServiceReadyListener() {
            @Override
            public void onServiceReady(boolean isReady) {
                loadListSku("", iabHelper.mService);
            }
        });
    }


    private void parseIntent() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.KEY_INTENT_CITY)) {
                mFCity = (FCity) getIntent().getSerializableExtra(AppConstants.KEY_INTENT_CITY);

                tvUnlockCity = (TextView) findViewById(R.id.tvUnlockCity);
                tvUnlockCity.setText("Unlock " + mFCity.getName());

                LoggerFactory.d("IAP for " + mFCity.toString());
//                Toast.makeText(mContext, "IAP for " + mFCity.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadListSku(String itemId, IInAppBillingService mService) {
//        LoggerFactory.d(TAG, "loadListSku...");
        try {

            ArrayList<String> skuList = new ArrayList<String>();
            skuList.add(skuAllCity);
            if (itemId != null && itemId.length() > 0) {
                skuList.add(itemId);
            }
            if (mFCity != null && mFCity.getApplePurchaseId() != null && mFCity.getApplePurchaseId().length() > 0) {
                skuList.add(mFCity.getApplePurchaseId());
            } else {
                Toast.makeText(mContext, "Purchase id of city invalid", Toast.LENGTH_SHORT).show();
            }

            Bundle querySkus = new Bundle();
            querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

            Bundle skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == BILLING_RESPONSE_RESULT_OK) {
                ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

                for (String thisResponse : responseList) {
                    JSONObject object = new JSONObject(thisResponse);
                    String sku = object.getString("productId");
                    String price = object.getString("price");
                    if (sku.equals("premium_upgrade")) mPremiumUpgradePrice = price;
                    else if (sku.equals(skuAllCity)) {
                        btnBuy.setText("Buy - " + price);
                    } else if (sku.equals(itemId)) {
                        mCityPrice = price;
                        btnBuyCity.setText("Buy - " + mCityPrice);
                    } else if (mFCity != null && sku.equals(mFCity.getApplePurchaseId())) {
                        mCityPrice = price;
                        btnBuyCity.setText("Buy - " + mCityPrice);
                    }

//                    LoggerFactory.d("Load Purchase item: " + object.toString());
                }
            }
        } catch (RemoteException | JSONException e) {
            LoggerFactory.logStackTrace(e);
//            Toast.makeText(mContext, "crash when load price of item", Toast.LENGTH_SHORT).show();
        }
    }

    String mPremiumUpgradePrice;
    String mCityPrice;
    public static final int REQUEST_CODE = 10001;
    String skuPurchasing;

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {

                    try {
                        if (result.isSuccess()) {
                            btnBuyCity.setEnabled(true);
                            if (!gotoCityView) {
                                checkPurchaseStatus(skuPurchasing, result, purchase);
                            }
                        } else {
                            // handle error
                        }
                    } catch (Exception e) {
                        LoggerFactory.logStackTrace(e);
                    }
                }
            };


    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            try {
                if (result.isFailure()) {
                    // Handle failure
//                    Toast.makeText(mContext, "QueryInventory Failure: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(mContext, "QueryInventory Success: " + skuPurchasing, Toast.LENGTH_SHORT).show();
                    if (skuPurchasing != null) {
//                            iabHelper.consumeAsync(inventory.getPurchase(sku), mConsumeFinishedListener);
                    }
                }
            } catch (Exception e) {
                LoggerFactory.logStackTrace(e);
                Toast.makeText(mContext, "crash when reveived inventory", Toast.LENGTH_SHORT).show();
            }
        }
    };

//    IabHelper.OnIabPurchaseFinishedListener mPurchaseCityFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
//        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            if (result.isFailure()) {
//                // Handle error
//                Toast.makeText(mContext, "Buy Failed: " + result.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(mContext, "Buy Failed: response " + result.getResponse(), Toast.LENGTH_SHORT).show();
//                if (result.getResponse() == 7) {
//                    AppSetting.getInstant(mContext).setCityUnlocked(mFCity.getCityKey());
//                    gotoCity();
//                }
//                return;
//            } else if (purchase.getSku().equals(skuPurchasing)) {
//                try {
//                    iabHelper.queryInventoryAsync(mReceivedInventoryListener);
//                } catch (Exception e) {
//                    LoggerFactory.logStackTrace(e);
//                }
//                Toast.makeText(mContext, "Purchase success sku: " + skuPurchasing + ". queryInventoryAsync", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(mContext, "Sku not match: " + purchase.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            checkPurchaseStatus(skuPurchasing, result, purchase);
//
////                mHandler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
////                        if (!gotoCityView) {
////                            Toast.makeText(mContext, "Purchase not callback, manual restore", Toast.LENGTH_SHORT).show();
////                            restorePurchase(false);
////                        }
////                    }
////                }, 5000);
//        }
//    };
    IabHelper.OnIabPurchaseFinishedListener mPurchaseAllCityFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//            Toast.makeText(mContext, "§onIabPurchaseFinished", Toast.LENGTH_SHORT).show();
            if (result.isFailure()) {
                // Handle error
//                Toast.makeText(mContext, "Buy Failed: " + result.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Buy Failed: response " + result.getResponse(), Toast.LENGTH_SHORT).show();
                if (result.getResponse() == 7) {
                    if (skuAllCity.equalsIgnoreCase(skuPurchasing)) {
                        AppSetting.getInstant(mContext).setUnlockAllCity(true);
                    } else if (mFCity.getApplePurchaseId().equalsIgnoreCase(skuPurchasing)) {
                        AppSetting.getInstant(mContext).setCityUnlocked(mFCity.getCityKey());
                    }
                    gotoCity();
                }
                return;
            }

            if (purchase == null) {
//                Toast.makeText(mContext, "Purchase success but purchase null", Toast.LENGTH_SHORT).show();
                return;
            }



            if (purchase.getSku().equals(skuPurchasing)) {
                try {
                    iabHelper.queryInventoryAsync(mReceivedInventoryListener);
                } catch (Exception e) {
                    LoggerFactory.logStackTrace(e);
                }
//                Toast.makeText(mContext, "Purchase success sku: " + skuPurchasing + ". queryInventoryAsync", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "Sku not match: " + purchase.toString(), Toast.LENGTH_SHORT).show();
            }

            checkPurchaseStatus(skuPurchasing, result, purchase);
        }
    };

    private void buyItem(final String sku) {
        skuPurchasing = sku;

        if (iabHelper != null) iabHelper.flagEndAsync();
        try{
            iabHelper.launchPurchaseFlow(this, sku, REQUEST_CODE, mPurchaseAllCityFinishedListener);
        }
        catch(IllegalStateException ex){
//            Toast.makeText(this, "Please retry in a few seconds.", Toast.LENGTH_SHORT).show();
            iabHelper.flagEndAsync();
        }
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!gotoCityView) {
//                    Toast.makeText(mContext, "Purchase not callback, manual restore", Toast.LENGTH_SHORT).show();
//                    restorePurchase(false);
//                }
//            }
//        }, 20000);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!gotoCityView) {
//                    Toast.makeText(mContext, "Purchase not callback, manual restore", Toast.LENGTH_SHORT).show();
//                    restorePurchase(false);
//                }
//            }
//        }, 15000);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!gotoCityView) {
//                    Toast.makeText(mContext, "Purchase not callback, manual restore", Toast.LENGTH_SHORT).show();
//                    restorePurchase(false);
//                }
//            }
//        }, 10000);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!gotoCityView) {
//                    Toast.makeText(mContext, "Purchase not callback, manual restore", Toast.LENGTH_SHORT).show();
//                    restorePurchase(false);
//                }
//            }
//        }, 5000);
    }

    Handler mHandler = new Handler();
    boolean gotoCityView = false;

    private void checkPurchaseStatus(final String sku, IabResult result, Purchase purchase) {
        if (result.isSuccess()) {
            try {
                if (sku.equalsIgnoreCase(skuAllCity)) {
//                    Toast.makeText(mContext, "Purchase all cities success", Toast.LENGTH_SHORT).show();
                    AppSetting.getInstant(mContext).setUnlockAllCity(true);
                } else {
//                    Toast.makeText(mContext, "Purchase city success", Toast.LENGTH_SHORT).show();
                    AppSetting.getInstant(mContext).setCityUnlocked(mFCity.getCityKey());
                }
            } catch (Exception e) {
                LoggerFactory.logStackTrace(e);
            }

            gotoCity();
        } else {
//            Toast.makeText(mContext, "Buy Failed: " + result.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(mContext, "Buy Failed: response " + result.getResponse(), Toast.LENGTH_SHORT).show();
        }


    }

    private void gotoCity() {
        if (gotoCityView)
            return;

        Toast.makeText(mContext, "Loading city ...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(IAPActivity.this, DetailTabsActivity.class);
        intent.putExtra(AppConstants.KEY_INTENT_CITY_KEY, mFCity.getCityKey());
        intent.putExtra(AppConstants.KEY_INTENT_CITY, mFCity);
        startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.activity_enter_back, R.anim.activity_exit_back);
        finish();

        gotoCityView = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mService != null && mServiceConn != null) {
//            unbindService(mServiceConn);
//        }
//        if (iabHelper != null) {
//            iabHelper.dispose();
//            iabHelper = null;
//        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE) {
//            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
//            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
//            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
//
//            if (resultCode == RESULT_OK) {
//                try {
//                    JSONObject jo = new JSONObject(purchaseData);
//                    String sku = jo.getString("productId");
//                    LoggerFactory.d("You have bought the " + sku + ". Excellent choice, adventurer!");
//                }
//                catch (JSONException e) {
//                    LoggerFactory.d("Failed to parse purchase data.");
//                    LoggerFactory.logStackTrace(e);
//                }
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (iabHelper != null && !iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
//            Toast.makeText(mContext, "iabHelper can not handle result", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
//            Toast.makeText(mContext, "onActivityResult handled by IABUtil", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        restorePurchase(false);
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Toast.makeText(mContext, "onActivityResult " + data.toString(), Toast.LENGTH_SHORT).show();
//        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        } else if (requestCode == REQUEST_CODE) {
//            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
//            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
//            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
//
//            if (resultCode == RESULT_OK) {
//                try {
//                    JSONObject jo = new JSONObject(purchaseData);
//                    String sku = jo.getString("productId");
//                    Toast.makeText(mContext, "You have bought the " + sku + ". Excellent choice, adventurer!", Toast.LENGTH_LONG).show();
//
//                    if (sku.equalsIgnoreCase(skuAllCity)) {
//                        Toast.makeText(mContext, "Purchase all cities success", Toast.LENGTH_SHORT).show();
//                        AppSetting.getInstant(mContext).setUnlockAllCity(true);
//                    } else if (sku.equalsIgnoreCase(mFCity.getApplePurchaseId())) {
//                        Toast.makeText(mContext, "Purchase city success", Toast.LENGTH_SHORT).show();
//                        AppSetting.getInstant(mContext).setCityUnlocked(mFCity.getCityKey());
//                    }
//
//
//                    gotoCity();
//                } catch (JSONException e) {
//                    Toast.makeText(mContext, "Failed to parse purchase data.", Toast.LENGTH_LONG).show();
//                    LoggerFactory.logStackTrace(e);
//                }
//            }
//        }
//    }


    private List<String> listAllCityPurchaseId = new LinkedList<>();
    List<FCity> fCities = new LinkedList<FCity>();
    List<FCity> fCityPurchases = new LinkedList<FCity>();
    CityService cityService;

    private void restorePurchase(final boolean showDialogSuccess) {
        try {
            // Listener that's called when we finish querying the items and subscriptions we own
            IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
                public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                    LoggerFactory.d("PAY", "Query inventory finished.");

                    // Have we been disposed of in the meantime? If so, quit.
                    if (iabHelper == null) {
//                        Toast.makeText(mContext, "iabHelper null", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (inventory == null) {
//                        Toast.makeText(mContext, "inventory null", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, "result:" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (result.isSuccess()) {
                        // restore for each city
                        for (int i = 0; i < listAllCityPurchaseId.size(); i++) {
                            Purchase purchase = inventory.getPurchase(listAllCityPurchaseId.get(i));
                            if (purchase != null) {
                                //purchased
//                                Toast.makeText(IAPActivity.this, "Purchased city: " + listAllCityPurchaseId.get(i), Toast.LENGTH_SHORT).show();
                                AppSetting.getInstant(mContext).setCityUnlocked(fCityPurchases.get(i).getCityKey());
                            }
                        }

                        // restore for all purchase
                        Purchase purchaseAllCity = inventory.getPurchase(skuAllCity);
                        if (purchaseAllCity != null) {
                            AppSetting.getInstant(mContext).setUnlockAllCity(true);
                        }

                        // this city was purchased
                        Purchase purchaseCity = inventory.getPurchase(mFCity.getApplePurchaseId());
                        if (purchaseCity != null || purchaseAllCity != null) {
                            gotoCity();
                        }
                    } else {
                        Toast.makeText(IAPActivity.this, "Restore failed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (showDialogSuccess) {
                        Utils.showDialog(getResources().getString(R.string.app_name), "Restore purchased success", IAPActivity.this);
                    }
                }
            };

            if (iabHelper == null) {
//                Toast.makeText(mContext, "restore faile bc iabHelper null", Toast.LENGTH_SHORT).show();
                return;
            }
            iabHelper.queryInventoryAsync(true, listAllCityPurchaseId, mGotInventoryListener);
        } catch (Exception e) {
            LoggerFactory.logStackTrace(e);
//            Toast.makeText(mContext, "Catch error. please try again::" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
