/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sage.libwheelview.widget.wheel.adapter;

import android.content.Context;

import com.sage.libwheelview.R;

/**
 * The simple Array wheel adapter
 * @param <T> the element type
 */
public class MArrayWheelAdapter<T> extends AbstractWheelTextAdapter {

    // items
    private T items[];
    private String select;

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public MArrayWheelAdapter(Context context, T items[],String select) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
        this.select = select;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.length) {
            T item = items[index];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length;
    }


    @Override
    public void setTextColor(int textColor) {
        for (int i=0;i<this.items.length;i++) {
            if (this.items[i].equals(this.select)) {
                setTextColor(R.color.greens);
            }else{
                setTextColor(R.color.grays);
            }
        }
    }
}
