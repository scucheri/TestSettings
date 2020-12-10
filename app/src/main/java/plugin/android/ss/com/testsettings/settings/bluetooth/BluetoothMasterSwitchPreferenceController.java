/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plugin.android.ss.com.testsettings.settings.bluetooth;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;

import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.R;
import com.android.settings.widget.SummaryUpdater;
import com.android.settings.widget.MasterSwitchPreference;
import com.android.settings.widget.MasterSwitchController;
import android.text.TextUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settings.overlay.FeatureFactory;
public class BluetoothMasterSwitchPreferenceController extends AbstractPreferenceController
        implements PreferenceControllerMixin, SummaryUpdater.OnSummaryChangeListener,
        LifecycleObserver, OnResume, OnPause, OnStart, OnStop {

    //public static final String KEY_TOGGLE_WIFI = "toggle_wifi";
	public static final String KEY_TOGGLE_BLUETOOTH = "toggle_bluetooth";
    //private MasterSwitchPreference mWifiPreference;
    private MasterSwitchPreference mBtPreference;

    //private BluetoothEnabler mWifiEnabler;
    private BluetoothEnabler mBluetoothEnabler;

    //private final BluetoothSummaryUpdater mSummaryHelper;
    private BluetoothSummaryUpdater mSummaryHelper;

    private final MetricsFeatureProvider mmMetricsFeatureProvider;

    private LocalBluetoothManager mBluetoothManager;
    public BluetoothMasterSwitchPreferenceController(Context context,LocalBluetoothManager bluetoothManager,MetricsFeatureProvider mMetricsFeatureProvider) {
        super(context);
	mmMetricsFeatureProvider =mMetricsFeatureProvider;
	mBluetoothManager =bluetoothManager;
         /*mBluetoothFeatureProvider = FeatureFactory.getFactory(
                 mContext).getBluetoothFeatureProvider(mContext);*/
        mSummaryHelper = new BluetoothSummaryUpdater(mContext, this, mBluetoothManager);
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mBtPreference = (MasterSwitchPreference) screen.findPreference(KEY_TOGGLE_BLUETOOTH);
    }
    	@Override
	public boolean handlePreferenceTreeClick(Preference preference) {
	if(TextUtils.equals(preference.getKey(),KEY_TOGGLE_BLUETOOTH)){
	//if(mBtPreference !=null)
	//mBtPreference.setChecked(!mBtPreference.isChecked());

	if (mBluetoothEnabler != null) {
	mBluetoothEnabler.onSwitchToggled(!mBtPreference.isChecked());

	}
	}
	return !mBtPreference.isChecked();
	}
    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_TOGGLE_BLUETOOTH;
    }

    @Override
    public void onResume() {
        mSummaryHelper.register(true);
        if (mBluetoothEnabler != null) {
            mBluetoothEnabler.resume(mContext);
        }
    }

    @Override
    public void onPause() {
        if (mBluetoothEnabler != null) {
           mBluetoothEnabler.pause();
        }
        mSummaryHelper.register(false);
    }

    @Override
    public void onStart() {
        mBluetoothEnabler = new BluetoothEnabler(mContext, new MasterSwitchController(mBtPreference),
			mmMetricsFeatureProvider,mBluetoothManager,
			MetricsEvent.ACTION_SETTINGS_MASTER_SWITCH_BLUETOOTH_TOGGLE);
    }

    @Override
    public void onStop() {
        if (mBluetoothEnabler != null) {
            mBluetoothEnabler.teardownSwitchController();
        }
    }

    @Override
    public void onSummaryChanged(String summary) {
        if (mBtPreference != null) {
            mBtPreference.setSummary(summary);
        }
    }

}
