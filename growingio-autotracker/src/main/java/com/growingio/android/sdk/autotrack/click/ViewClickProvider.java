/*
 * Copyright (C) 2020 Beijing Yishu Technology Co., Ltd.
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

package com.growingio.android.sdk.autotrack.click;

import android.view.MenuItem;
import android.view.View;

import com.growingio.android.sdk.autotrack.GrowingAutotracker;
import com.growingio.android.sdk.autotrack.events.ViewElement;
import com.growingio.android.sdk.autotrack.events.ViewElementEvent;
import com.growingio.android.sdk.autotrack.events.base.BaseViewElement;
import com.growingio.android.sdk.autotrack.models.ViewNode;
import com.growingio.android.sdk.autotrack.page.Page;
import com.growingio.android.sdk.autotrack.page.PageProvider;
import com.growingio.android.sdk.autotrack.view.ViewHelper;
import com.growingio.android.sdk.track.TrackMainThread;
import com.growingio.android.sdk.track.events.EventType;
import com.growingio.android.sdk.track.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

class ViewClickProvider {
    private static final String TAG = "ViewClickProvider";

    private ViewClickProvider() {
    }

    public static void viewOnClick(View view) {
        if (!GrowingAutotracker.initializedSuccessfully()) {
            LogUtil.e(TAG, "Autotracker do not initialized successfully");
        }

        ViewNode viewNode = ViewHelper.getClickViewNode(view);
        if (viewNode != null) {
            LogUtil.printJson(TAG, "viewOnClick: ", viewNode.toJSONObject().toString());
            ViewElementEvent.EventBuilder event = new ViewElementEvent.EventBuilder();
            event.setEventType(EventType.CLICK);
            Page<?> page = PageProvider.get().findPage(view);
            event.addElementBuilders(viewNodeToElementBuilders(viewNode))
                    .setPageName(page.path())
                    .setPageShowTimestamp(page.getShowTimestamp());
            TrackMainThread.trackMain().postEventToTrackMain(event);
        } else {
            LogUtil.e(TAG, "ViewNode is NULL");
        }
    }

    public static void menuItemOnClick(MenuItem menuItem) {
        if (!GrowingAutotracker.initializedSuccessfully()) {
            LogUtil.e(TAG, "Autotracker do not initialized successfully");
        }
    }

    private static List<BaseViewElement.BaseElementBuilder<?>> viewNodeToElementBuilders(ViewNode viewNode) {
        List<BaseViewElement.BaseElementBuilder<?>> elementBuilders = new ArrayList<>();
        elementBuilders.add((new ViewElement.ElementBuilder())
                .setXpath(viewNode.getXPath())
                .setTimestamp(System.currentTimeMillis())
                .setIndex(viewNode.getIndex())
                .setTextValue(viewNode.getViewContent())
        );
        return elementBuilders;
    }
}
