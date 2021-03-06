/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package plugin.android.ss.com.testsettings.settings.applications.appinfo;

import android.content.Context;

import com.android.settings.applications.defaultapps.DefaultPhonePreferenceController;

public class DefaultPhoneShortcutPreferenceController
        extends DefaultAppShortcutPreferenceControllerBase {

    private static final String KEY = "default_phone_app";

    public DefaultPhoneShortcutPreferenceController(Context context, String packageName) {
        super(context, KEY, packageName);
    }

    @Override
    protected boolean hasAppCapability() {
        return DefaultPhonePreferenceController.hasPhonePreference(mPackageName, mContext);
    }

    @Override
    protected boolean isDefaultApp() {
        return DefaultPhonePreferenceController.isPhoneDefault(mPackageName, mContext);
    }

}
