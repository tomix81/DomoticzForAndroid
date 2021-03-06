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


package nl.hnogames.domoticz.Containers;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("unused")
public class GraphPointInfo {

    JSONObject jsonObject;
    String dateTime;
    String hu;
    String ba;
    long te;
    String v;
    String c;
    String di;
    String gu;
    String sp;
    String uvi;

    public GraphPointInfo(JSONObject row) throws JSONException {
        this.jsonObject = row;

        if (row.has("te"))
            te = row.getLong("te");
        if (row.has("d"))
            dateTime = row.getString("d");
        if (row.has("v"))
            v = row.getString("v");
        else if (row.has("v_avg"))
            v = row.getString("v_avg");
        if (row.has("c"))
            c = row.getString("c");
        if (row.has("hu"))
            hu = row.getString("hu");
        if (row.has("ba"))
            ba = row.getString("ba");
        if (row.has("sp"))
            sp = row.getString("sp");
        if (row.has("di"))
            di = row.getString("di");
        if (row.has("gu"))
            gu = row.getString("gu");
        if (row.has("uvi"))
            uvi = row.getString("uvi");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                new Gson().toJson(this) +
                '}';
    }

    public String getPercentage() {
        return v;
    }

    public long getTemperature() {
        return te;
    }

    public String getHumidity() {
        return hu;
    }

    public String getSunPower() {
        return uvi;
    }

    public String getCounter() {
        return c;
    }

    public String getSpeed() {
        return sp;
    }

    public String getDirection() {
        return di;
    }

    public String getBarometer() {
        return ba;
    }

    public String getDateTime() {
        return dateTime;
    }

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

}