/*
 * Copyright (C) 2017 Jakub Ksiezniak
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.github.jksiezni.xpra;

import android.app.FragmentManager;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.github.jksiezni.xpra.fragments.ServersListFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import xpra.protocol.packets.SetDeflate;

public class MainActivity extends AppCompatActivity implements GlobalActivityAccessor {

	private FloatingActionButton floatingButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set the custom toolbar
		final Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setLogo(R.mipmap.ic_launcher);
		
		floatingButton = (FloatingActionButton) findViewById(R.id.floatingButton);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new ServersListFragment()).commit();
		}
		getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				shouldDisplayNavigateUp();
			}
		});
		shouldDisplayNavigateUp();
	}

  @Override
  protected void onResume() {
    super.onResume();
    new Thread(new Runnable() {
      @Override
      public void run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; ++i) {
            SetDeflate deflate = new SetDeflate(123);
        }
        Log.i("bench", "TIme: " + (System.currentTimeMillis()-start));
      }
    }).start();
  }

  @Override
	protected void onDestroy() {
		OpenHelperManager.releaseHelper();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if(!getFragmentManager().popBackStackImmediate()) {
			super.onBackPressed();
		}
	}

	@Override
	public FloatingActionButton getFloatingActionButton() {
		return floatingButton;
	}

	@Override
	public boolean onNavigateUp() {
		return getFragmentManager().popBackStackImmediate() || super.onNavigateUp();
	}

	private void shouldDisplayNavigateUp() {
		final boolean showNavigateUp = getFragmentManager().getBackStackEntryCount() > 0;
		getSupportActionBar().setDisplayHomeAsUpEnabled(showNavigateUp);
	}
}
