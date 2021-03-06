/*
 * Copyright (C) 2018 The Android Open Source Project
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

package plugin.android.ss.com.testsettings.settings.search;

import android.content.Context;
import android.net.Uri;

import java.util.List;

public class DeviceIndexFeatureProviderImpl implements DeviceIndexFeatureProvider {

    @Override
    public boolean isIndexingEnabled() {
        return false;
    }

    @Override
    public void index(Context context, CharSequence title, Uri sliceUri, Uri launchUri,
            List<String> keywords) {
        // Not enabled by default.
    }

    @Override
    public void clearIndex(Context context) {
        // Not enabled by default.
    }
}
