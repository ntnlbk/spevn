package com.LibBib.spevn;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;
public class HelpActivity extends AppCompatActivity {
    private ImageView backbtn;
    private LinearLayout addsongbtn, addaudiobtn, donatebtn, reclamabtn;
    private BillingClient billingClient;
    private BillingFlowParams billingFlowParams;
    private RewardedAd mRewardedAd;
    private boolean isreclamawatched = false;
    private MediaPlayer thanksSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        backbtn = (ImageView) findViewById(R.id.backbtn3);
        addsongbtn = (LinearLayout) findViewById(R.id.songbtn);
        addaudiobtn = (LinearLayout) findViewById(R.id.audiobtn);
        donatebtn = (LinearLayout) findViewById(R.id.moneybtn);
        reclamabtn = (LinearLayout) findViewById(R.id.reclama2);
        thanksSound = MediaPlayer.create(this, R.raw.thanks);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-2762019052396240/2052996231",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        //Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        // Log.d(TAG, "onAdFailedToLoad");
                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                // Log.d(TAG, "Ad was shown.");
                                mRewardedAd = null;
                            }
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                //  Log.d(TAG, "Ad failed to show.");
                            }
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                //   Log.d(TAG, "Ad was dismissed.");
                            }
                        });
                    }
                });
        billingClient = BillingClient.newBuilder(HelpActivity.this)
                .setListener(new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                        // Purchase retrieved from BillingClient#queryPurchasesAsync or your PurchasesUpdatedListener.
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                            for (Purchase purchase : purchases) {
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED &&
                                        !purchase.isAcknowledged()) {
                                    ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                                    billingClient.consumeAsync(
                                            consumeParams,
                                            new ConsumeResponseListener() {
                                                @Override
                                                public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                                        sayThanks();
                                                    }
                                                }
                                            }
                                    );
                                }
                            }
                        }
                    }
                })
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    List<String> skuList = new ArrayList<>();
                    skuList.add("donate1");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    billingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult billingResult,
                                                                 List<SkuDetails> skuDetailsList) {
                                    Activity activity = HelpActivity.this;
                                    // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
                                    billingFlowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                }
                            });
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(HelpActivity.this, "Повторите попытку", Toast.LENGTH_SHORT).show();
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addsongbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, addsongactivity.class);
                startActivity(intent);
            }
        });
        addaudiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, addaudioactivity.class);
                startActivity(intent);
            }
        });
        donatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billingClient != null && hasConnection(HelpActivity.this)) {
                    try{
                    int responseCode = billingClient.launchBillingFlow(HelpActivity.this, billingFlowParams).getResponseCode();}
                    catch (Exception e){
                        Toast.makeText(HelpActivity.this, "Проверьте подключение к интернету и повторите попытку", Toast.LENGTH_SHORT).show();
                    }
                } else if (!hasConnection(HelpActivity.this)) {
                    Toast.makeText(HelpActivity.this, "Проверьте подключение к интернету и повторите попытку", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reclamabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedAd != null) {
                    Activity activityContext = HelpActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            Toast.makeText(HelpActivity.this, "Спасибо!", Toast.LENGTH_SHORT).show();
                            isreclamawatched = true;
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                        }
                    });
                } else {
                    if (isreclamawatched)
                        Toast.makeText(HelpActivity.this, "Вы уже посмотрели рекламу", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(HelpActivity.this, "Проверьте подключение к интернету и попробуйте еще раз", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sayThanks() {
         thanksSound.start();
    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }
}