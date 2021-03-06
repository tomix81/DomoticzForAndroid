/*
 * Copyright (C) 2015 Domoticz
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package nl.hnogames.domoticz;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nl.hnogames.domoticz.Adapter.ListAdapter;
import nl.hnogames.domoticz.Containers.ExtendedStatusInfo;
import nl.hnogames.domoticz.app.DomoticzActivity;

public class WearActivity extends DomoticzActivity
        implements WearableListView.ClickListener,
        MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks {

    private ArrayList<ExtendedStatusInfo> switches = null;
    private WearableListView listView;
    private ListAdapter adapter;

    // Sample dataset for the list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Get the list component from the layout of the activity
        listView = (WearableListView) findViewById(R.id.wearable_list);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String switchesRawData = prefs.getString(PREF_SWITCH, "");
        createListView(switchesRawData);
    }

    private  void createListView(String switchesRawData)
    {
        try {
            JSONArray jsonSwitchArray = new JSONArray(switchesRawData);
            switches = new ArrayList<>();

            for(int i=0; i<jsonSwitchArray.length();i++)
            {
                String json = jsonSwitchArray.getJSONObject(i).getString("jsonObject");
                JSONObject row = new JSONObject(json);
                switches.add(new ExtendedStatusInfo(new JSONObject(row.getString("nameValuePairs"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("WEAR", "Parsing error: " + e.getMessage());
        }

        Log.v("WEAR", "Parsing information: "+switches.toString());
        adapter = new ListAdapter(this, switches);
        listView.setAdapter(adapter);
        listView.setClickListener(this);
    }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
        Intent intent = new Intent(this, SendActivity.class);
        String sendData = "";

        try {
            JSONObject switchJSON = switches.get(tag).getJsonObject();
            if(switchJSON.has("nameValuePairs"))
                sendData = switchJSON.getString("nameValuePairs").toString();
            else
                sendData = switchJSON.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        intent.putExtra("SWITCH", sendData);
        startActivity(intent);
    }

    @Override
    public void onTopEmptyRegionClick() {}

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(SEND_DATA)) {
            String rawData = new String(messageEvent.getData());
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString(PREF_SWITCH, rawData).apply();
            createListView(rawData);
        }
    }
}