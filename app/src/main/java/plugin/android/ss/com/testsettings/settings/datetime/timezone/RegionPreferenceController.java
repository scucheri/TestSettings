/*
 * Copyright (C) 2018 The Android Open Source Project
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
package plugin.android.ss.com.testsettings.settings.datetime.timezone;

import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.preference.Preference;

import java.util.Locale;

public class RegionPreferenceController extends BaseTimeZonePreferenceController {
    private static final String PREFERENCE_KEY = "region";

    private final LocaleDisplayNames mLocaleDisplayNames;
    private String mRegionId = "";

    public RegionPreferenceController(Context context) {
        super(context, PREFERENCE_KEY);
        Locale locale = context.getResources().getConfiguration().getLocales().get(0);
        mLocaleDisplayNames = LocaleDisplayNames.getInstance(locale);

    }

    @Override
    public CharSequence getSummary() {
        return mLocaleDisplayNames.regionDisplayName(mRegionId);
    }

    public void setRegionId(String regionId) {
        mRegionId = regionId;
    }

    public String getRegionId() {
        return mRegionId;
    }
}