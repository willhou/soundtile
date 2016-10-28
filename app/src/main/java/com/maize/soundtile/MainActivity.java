// Copyright 2016 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.maize.soundtile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mLabel;
    private Button mBtnPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLabel = (TextView) findViewById(R.id.tvLabel);
        mBtnPermission = (Button) findViewById(R.id.btnPermission);
        mBtnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });

        ensureUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ensureUi();
    }

    private void ensureUi() {
        boolean permissionGranted = Utils.checkNotificationPolicyAccess(this);
        mLabel.setText(permissionGranted ? R.string.all_set : R.string.permission_request);
        mBtnPermission.setVisibility(permissionGranted ? View.GONE : View.VISIBLE);
    }
}
