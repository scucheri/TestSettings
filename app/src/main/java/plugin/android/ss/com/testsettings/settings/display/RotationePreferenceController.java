/*
 * Copyright (C) 2018 The Android Open Source Project
 *`
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
 * limitations under the License
 */

package plugin.android.ss.com.testsettings.settings.display;


import android.content.Context;
import android.provider.Settings;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.util.FeatureFlagUtils;
import android.os.SystemProperties;
import android.provider.Settings;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;

/**
 * Setting where user can pick if SystemUI will be light, dark or try to match
 * the wallpaper colors.
 */
public class RotationePreferenceController extends BasePreferenceController
        implements Preference.OnPreferenceChangeListener {

    private ListPreference mSystemUiThemePref;

    public RotationePreferenceController(Context context, String preferenceKey) {
        super(context, preferenceKey);
    }

    @Override
    public int getAvailabilityStatus() {
        
        return AVAILABLE ;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mSystemUiThemePref = (ListPreference) screen.findPreference(getPreferenceKey());
        String  value = SystemProperties.get("persist.sys.app.rotation","null");
        mSystemUiThemePref.setValue(value);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = (String) newValue;
		SystemProperties.set("persist.sys.app.rotation",value);
		if(value.equals("force_land")){
		Settings.System.putInt(mContext.getContentResolver(), Settings.System.USER_ROTATION, 0);
		}
		if(value.equals("original")){
			Settings.System.putInt(mContext.getContentResolver(), Settings.System.USER_ROTATION, 1);
       }
		if(value.equals("middl_port")){
			Settings.System.putInt(mContext.getContentResolver(), Settings.System.USER_ROTATION, 2);
       }
		if(value.equals("original_port")){
			Settings.System.putInt(mContext.getContentResolver(), Settings.System.USER_ROTATION, 3);
       }
        refreshSummary(preference);
        return true;
    }

    @Override
    public CharSequence getSummary() {
		String  value = SystemProperties.get("persist.sys.app.rotation","null");
        int index = mSystemUiThemePref.findIndexOfValue(value);
        return mSystemUiThemePref.getEntries()[index];
    }
}
